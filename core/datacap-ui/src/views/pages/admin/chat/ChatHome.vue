<template>
  <a-layout>
    <a-layout has-sider>
      <a-layout-sider theme="light" :width="280">
        <a-card :title="$t('common.chat')">
          <template #extra>
            <a-button shape="circle" size="small" @click="visibleInfo(true)">
              <template #icon>
                <ShadcnIcon icon="Plus" :size="15"/>
              </template>
            </a-button>
          </template>

          <div class="relative min-h-screen">
            <a-spin :spinning="loading">
              <a-radio-group v-model:value="toggleValue" class="dc-chat-radio p-1" :style="{ width: '100%' }" @change="onChange">
                <a-row :gutter="8">
                  <a-col v-for="item of data" :key="item.id" :span="24">
                    <a-radio-button class="w-full" :value="item.id">
                      <div class="flex items-center space-x-4">
                        <a-avatar size="small" :src="item.avatar" :alt="item.name"/>

                        <div class="text-sm font-medium leading-none">{{ item.name }}</div>
                      </div>
                    </a-radio-button>
                  </a-col>
                </a-row>
              </a-radio-group>
            </a-spin>
          </div>
        </a-card>
      </a-layout-sider>

      <a-layout-content>
        <div v-if="dataInfo">
          <a-card>
            <template #title>
              <div class="flex flex-row items-center justify-between">
                <div class="flex items-center space-x-4">
                  <a-avatar :src="dataInfo.avatar" :alt="dataInfo.name"/>
                  <div>
                    <p class="text-sm font-medium leading-none">{{ dataInfo.name }}</p>
                    <p class="text-sm text-muted-foreground">{{ dataInfo.description }}</p>
                  </div>
                </div>
              </div>
            </template>

            <template #extra>
              <div class="flex h-5 items-center space-x-4 text-sm">
                <div>
                  Prompt Tokens: {{ promptTokens }}
                </div>
                <a-divider type="vertical"/>
                <div>
                  Completion Tokens: {{ completionTokens }}
                </div>
                <a-divider type="vertical"/>
                <div>
                  Total Tokens: {{ totalTokens }}
                </div>
              </div>
            </template>

            <div class="relative min-h-screen">
              <a-spin :spinning="loadingMessages">
                <div class="w-full overflow-y-auto overflow-x-hidden h-full flex flex-col">
                  <div ref="messagesContainer" class="w-full overflow-y-auto overflow-x-hidden flex flex-col" style="height: 550px;">
                    <template v-for="(item, index) in messages" :key="index">
                      <div :class="cn('flex flex-col gap-2 p-4 whitespace-pre-wrap',
                                  item.type === 'question' ? 'items-end' : 'items-start'
                            )">
                        <div class="flex gap-3 items-center">
                          <span class="bg-accent p-3 rounded-md max-w-lg">{{ item.content }}</span>
                        </div>

                        <div v-if="item.type === 'answer'" class="flex text-sm text-muted-foreground mt-0.5 space-x-2">
                          <div>Model: {{ item.model }}</div>
                          <a-divider type="vertical"/>
                          <div>Prompt Tokens: {{ item.promptTokens }}</div>
                          <a-divider type="vertical"/>
                          <div>Completion Tokens: {{ item.completionTokens }}</div>
                          <a-divider type="vertical"/>
                          <div>Total Tokens: {{ item.totalTokens }}</div>
                        </div>
                      </div>
                    </template>
                  </div>
                </div>
              </a-spin>
            </div>

            <template #footer>
              <div class="p-2 flex justify-between w-full items-center gap-2">
                <a-textarea v-model:value="inputValue" placeholder="Type a message ..."/>

                <a-button type="primary" :loading="submitting" :disabled="!inputValue || submitting" @click="onSubmit">
                  <template #icon>
                    <ShadcnIcon icon="Send" :size="18"/>
                  </template>
                </a-button>
              </div>
            </template>
          </a-card>
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout>

  <ChatInfo v-if="dataVisible" :is-visible="dataVisible" @close="visibleInfo($event)"/>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { message as antdMessage } from 'ant-design-vue'
import { FilterModel } from '@/model/filter.ts'
import ChatService from '@/services/chat.ts'
import ChatInfo from '@/views/pages/admin/chat/ChatInfo.vue'
import { ChatModel } from '@/model/chat.ts'
import { toNumber } from 'lodash'
import { cn } from '@/lib/utils.ts'
import { MessageModel } from '@/model/message.ts'
import MessageService from '@/services/message.ts'

defineOptions({ name: 'ChatHome' })

const filter: FilterModel = new FilterModel()

const loading = ref(true)
const data = ref<ChatModel[]>([])
const dataInfo = ref<ChatModel | null>(null)
const dataVisible = ref(false)
const loadingMessages = ref(false)
const messages = ref<MessageModel[]>([])
const promptTokens = ref(0)
const completionTokens = ref(0)
const totalTokens = ref(0)
const submitting = ref(false)
const toggleValue = ref<any>(null)
const inputValue = ref('')
const messagesContainer = ref<any>(null)

const handleInitialize = () => {
  loading.value = true
  ChatService.getAll(filter)
             .then((response) => {
               if (response.status) {
                 data.value = response.data.content
               }
               else {
                 antdMessage.error(response.message)
               }
             })
             .finally(() => (loading.value = false))
}

const visibleInfo = (opened: boolean) => {
  dataVisible.value = opened
  if (!opened) {
    handleInitialize()
  }
}

const handlerGoBottom = () => {
  const scrollElem = messagesContainer.value as any
  setTimeout(() => {
    scrollElem?.scrollTo({ top: scrollElem.scrollHeight, behavior: 'smooth' })
  }, 0)
}

const counterToken = () => {
  const answers = messages.value.filter(message => message.type === 'answer')
  promptTokens.value = answers.reduce((sum, message) => sum + toNumber(message.promptTokens), 0)
  completionTokens.value = answers.reduce((sum, message) => sum + toNumber(message.completionTokens), 0)
  totalTokens.value = answers.reduce((sum, message) => sum + toNumber(message.totalTokens), 0)
}

const onChange = (event: any) => {
  const value = event?.target ? event.target.value : event
  dataInfo.value = data.value.find(item => item.id === toNumber(value)) as unknown as ChatModel
  loadingMessages.value = true
  ChatService.getMessages(toNumber(value))
             .then((response) => {
               messages.value = response.data
             })
             .finally(() => {
               loadingMessages.value = false
               counterToken()
               handlerGoBottom()
             })
}

const onSubmit = () => {
  submitting.value = true
  const msg = {
    content: inputValue.value,
    chat: dataInfo.value,
    type: 'question'
  } as any
  messages.value.push(msg)
  handlerGoBottom()
  MessageService.saveOrUpdate(msg)
                .then((response) => {
                  if (response.status) {
                    messages.value.push(response.data)
                    inputValue.value = ''
                  }
                  else {
                    antdMessage.error(response.message)
                  }
                })
                .finally(() => {
                  submitting.value = false
                  counterToken()
                  handlerGoBottom()
                })
}

handleInitialize()
</script>

<style scoped>
/* 让聊天列表的单选按钮铺满整行、左对齐 */
.dc-chat-radio :deep(.ant-radio-button-wrapper) {
    width: 100%;
    height: auto;
    padding: 8px 12px;
    margin-bottom: 6px;
    border-radius: 6px;
    border-left-width: 1px;
    display: flex;
    align-items: center;
}

.dc-chat-radio :deep(.ant-radio-button-wrapper::before) {
    display: none;
}
</style>
