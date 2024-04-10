<template>
  <div class="hidden space-y-6 pb-16 w-full md:block">
    <div class="flex flex-col space-y-8 lg:flex-row lg:space-x-6 lg:space-y-0">
      <aside class="-mx-4 w-[200px]">
        <Card title-class="p-2" body-class="p-2">
          <template #title>{{ $t('common.chat') }}</template>
          <template #extra>
            <Button size="icon" class="w-6 h-6 rounded-full" @click="handlerInfo(true)">
              <Plus :size="15"/>
            </Button>
          </template>
          <CircularLoading v-if="loading" :show="loading"/>
          <div v-else>
            <FormField type="radio" name="theme">
              <FormItem class="space-y-1">
                <RadioGroup class="grid gap-3 pt-2 cursor-pointer" @update:modelValue="handlerChange">
                  <FormItem v-for="item of data" :key="item.id">
                    <FormLabel class="[&:has([data-state=checked])>div]:border-primary cursor-pointer">
                      <FormControl>
                        <RadioGroupItem :value="item.id as unknown as string" class="sr-only cursor-pointer"/>
                      </FormControl>
                      <div class="items-center rounded-md border-4 border-muted p-1 hover:border-accent">
                        <div class="flex flex-row items-center justify-between">
                          <div class="flex items-center space-x-4">
                            <Avatar :src="item.avatar" :alt="item.name"/>
                            <div>
                              <p class="text-sm font-medium leading-none">{{ item.name }}</p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </FormLabel>
                  </FormItem>
                </RadioGroup>
              </FormItem>
            </FormField>
          </div>
        </Card>
      </aside>
      <div class="flex-1">
        <div v-if="dataInfo">
          <Card title-class="p-2" body-class="p-2">
            <template #title>
              <div class="flex flex-row items-center justify-between">
                <div class="flex items-center space-x-4">
                  <Avatar :src="dataInfo.avatar" :alt="dataInfo.name"/>
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
                <Separator orientation="vertical"/>
                <div>
                  Completion Tokens: {{ completionTokens }}
                </div>
                <Separator orientation="vertical"/>
                <div>
                  Total Tokens: {{ totalTokens }}
                </div>
              </div>
            </template>
            <CircularLoading v-if="loadingMessages" :show="loadingMessages"/>
            <div ref="scrollDiv" class="space-y-4 h-[500px] overflow-y-auto">
              <div v-for="(item, index) in messages" :key="index">
                <div
                    :class="cn( 'flex w-max max-w-[75%] flex-col gap-2 rounded-lg px-3 py-2 text-sm', item.type === 'question' ? 'ml-auto bg-primary text-primary-foreground' : 'bg-muted')">
                  {{ item.content }}
                </div>
                <div v-if="item.type === 'answer'" class="flex text-sm text-muted-foreground mt-0.5 space-x-2">
                  <div>Model: {{ item.model }}</div>
                  <Separator orientation="vertical"/>
                  <div>Prompt Tokens: {{ item.promptTokens }}</div>
                  <Separator orientation="vertical"/>
                  <div>Completion Tokens: {{ item.completionTokens }}</div>
                  <Separator orientation="vertical"/>
                  <div>Total Tokens: {{ item.totalTokens }}</div>
                </div>
              </div>
            </div>
            <template #footer>
              <div class="flex w-full items-center space-x-2">
                <Input v-model="inputValue" :disabled="submitting" placeholder="Type a message ..." class="flex-1"/>
                <Button class="p-2.5 flex items-center justify-center" :loading="submitting" :disabled="!inputValue || submitting" @click="handlerSubmit">
                  <Send v-if="!submitting" class="w-4 h-4"/>
                  <span class="sr-only"></span>
                </Button>
              </div>
            </template>
          </Card>
        </div>
      </div>
    </div>
  </div>
  <ChatInfo v-if="dataVisible" :is-visible="dataVisible" @close="handlerInfo($event)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { FilterModel } from '@/model/filter.ts'
import ChatService from '@/services/chat.ts'
import { ToastUtils } from '@/utils/toast.ts'
import Card from '@/views/ui/card'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group'
import Button from '@/views/ui/button'
import { Plus, Send } from 'lucide-vue-next'
import ChatInfo from '@/views/pages/admin/chat/ChatInfo.vue'
import Avatar from '@/views/ui/avatar'
import { ChatModel } from '@/model/chat.ts'
import { toNumber } from 'lodash'
import { cn } from '@/lib/utils.ts'
import { MessageModel } from '@/model/message.ts'
import { Input } from '@/components/ui/input'
import MessageService from '@/services/message.ts'
import { Separator } from '@/components/ui/separator'

export default defineComponent({
  name: 'ChatHome',
  components: {
    Separator,
    Input,
    Avatar,
    ChatInfo,
    Button,
    CircularLoading,
    Card,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage,
    RadioGroup, RadioGroupItem,
    Plus, Send
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    return {
      filter,
      cn
    }
  },
  data()
  {
    return {
      loading: true,
      data: [] as ChatModel[],
      dataInfo: null as ChatModel | null,
      dataVisible: false,
      loadingMessages: false,
      messages: [] as MessageModel[],
      promptTokens: 0,
      completionTokens: 0,
      totalTokens: 0,
      submitting: false,
      inputValue: ''
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      ChatService.getAll(this.filter)
                 .then(response => {
                   if (response.status) {
                     this.data = response.data.content
                   }
                   else {
                     ToastUtils.error(response.message)
                   }
                 })
                 .finally(() => this.loading = false)
    },
    handlerInfo(opened: boolean)
    {
      this.dataVisible = opened
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerChange(value: string)
    {
      this.dataInfo = this.data.find(item => item.id === toNumber(value)) as unknown as ChatModel
      this.loadingMessages = true
      ChatService.getMessages(toNumber(value))
                 .then(response => {
                   this.messages = response.data
                 })
                 .finally(() => {
                   this.loadingMessages = false
                   this.counterToken()
                   this.handlerGoBottom()
                 })
    },
    handlerSubmit()
    {
      this.submitting = true
      const message = {
        content: this.inputValue,
        chat: this.dataInfo,
        type: 'question'
      }
      this.messages.push(message)
      this.handlerGoBottom()
      MessageService.saveOrUpdate(message)
                    .then(response => {
                      if (response.status) {
                        this.messages.push(response.data)
                        this.inputValue = ''
                      }
                      else {
                        this.$Message.error(response.message)
                      }
                    })
                    .finally(() => {
                      this.submitting = false
                      this.counterToken()
                      this.handlerGoBottom()
                    })
    },
    handlerGoBottom()
    {
      const scrollElem = this.$refs.scrollDiv as any
      setTimeout(() => {
        scrollElem.scrollTo({ top: scrollElem.scrollHeight, behavior: 'smooth' })
      }, 0)
    },
    counterToken()
    {
      const answers = this.messages.filter(message => message.type === 'answer')
      this.promptTokens = answers.reduce((sum, message) => sum + toNumber(message.promptTokens), 0)
      this.completionTokens = answers.reduce((sum, message) => sum + toNumber(message.completionTokens), 0)
      this.totalTokens = answers.reduce((sum, message) => sum + toNumber(message.totalTokens), 0)
    }
  }
})
</script>
