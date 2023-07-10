<template>
  <div>
    <Layout>
      <Sider style="background-color: #FFFFFF">
        <Card dis-hover
              padding="0">
          <template #title>
            {{ $t('common.chat') }}
          </template>
          <template #extra>
            <Tooltip :content="$t('common.create') + $t('common.chat')">
              <Button type="primary"
                      shape="circle"
                      size="small"
                      style="margin-top: -10px;"
                      icon="md-add"
                      @click="handlerCreateChat(true)">
              </Button>
            </Tooltip>
          </template>
          <RadioGroup v-model="selectedChat"
                      vertical
                      style="margin: 3px 3px 3px 3px; height: 500px; width: 100%; overflow: auto;"
                      @on-change="handlerChangeChat">
            <Radio v-for="item in chats"
                   :key="item.id"
                   :label="item"
                   border
                   style="margin-top: 3px; padding: 3px 5px; height: 40px;">
              <Avatar v-if="item.avatar"
                      :src="item.avatar">
              </Avatar>
              <Avatar v-else>
                {{ item.name }}
              </Avatar>
              {{ item.name }}
            </Radio>
          </RadioGroup>
          <CircularLoading v-if="loadingChats"
                           :show="loadingChats">
          </CircularLoading>
        </Card>
      </Sider>
      <Content style="margin-left: 3px;">
        <Card v-if="selectedChat"
              dis-hover
              style="width:100%; minHeight: 150px;">
          <template #title>
            <Text strong>{{ $t('common.chat') }}</Text>
            : {{ selectedChat.name }} &nbsp;
          </template>
          <template #extra>
            <div style="margin-top: -13px;">
              Prompt Tokens:
              <CountUp :end="promptTokens"
                       v-font="24">
              </CountUp>
              <Divider type="vertical"/>
              Completion Tokens:
              <CountUp :end="completionTokens"
                       v-font="24">
              </CountUp>
              <Divider type="vertical"/>
              Total Tokens:
              <CountUp :end="totalTokens"
                       v-font="24">
              </CountUp>
            </div>
          </template>
          <Layout style="background-color: #FFFFFF;">
            <Content style="padding: 0px 0px 0px 10px;">
              <div ref="scrollDiv" style="height: 300px; max-height: 300px; overflow: auto;">
                <div v-for="item in messages"
                     :key="item">
                  <Divider :orientation="item.type === 'question' ? 'left' : 'right'">
                    <Avatar v-if="item.type === 'question'">
                      {{ userInfo.username }}
                    </Avatar>
                    <Avatar v-else
                            icon="md-ionitron"
                            style="background-color: #87d068;">
                    </Avatar>
                  </Divider>
                  <div :style="{margin: '0px 10px', float: item.type === 'question' ? '' : 'right'}">
                    <Card dis-hover
                          :bordered="false"
                          padding="0">
                      <MarkdownView v-if="item.content"
                                    :content="item.content">
                      </MarkdownView>
                      <div v-if="item.type === 'answer'"
                           style="margin-top: 5px; float: right;">
                        Model:
                        <Text strong>{{ item.model }}</Text>
                        <Divider type="vertical"/>
                        Prompt Tokens:
                        <CountUp :end="item.promptTokens"
                                 v-font="24">
                        </CountUp>
                        <Divider type="vertical"/>
                        Completion Tokens:
                        <CountUp :end="item.completionTokens"
                                 v-font="24">
                        </CountUp>
                        <Divider type="vertical"/>
                        Total Tokens:
                        <CountUp :end="item.totalTokens"
                                 v-font="24">
                        </CountUp>
                      </div>
                    </Card>
                  </div>
                </div>
                <CircularLoading :show="loadingMessages">
                </CircularLoading>
              </div>
            </Content>
            <Divider></Divider>
            <Footer style="background-color: #FFFFFF;">
              <Row>
                <Col span="20">
                  <Input v-model="questionContext"
                         type="textarea"
                         :disabled="sendMessage"
                         :autosize="{minRows: 2,maxRows: 5}">
                  </Input>
                </Col>
                <Col span="2" offset="1">
                  <Button type="primary"
                          icon="md-send"
                          :disabled="!questionContext"
                          :loading="sendMessage"
                          @click="handlerSendMessage()">
                    {{ $t('common.send') }}
                  </Button>
                </Col>
              </Row>
            </Footer>
          </Layout>
        </Card>
        <Card v-else
              dis-hover>
          <div style="text-align:center; margin: 248px 0px;">
            No selection chat
          </div>
        </Card>
      </Content>
    </Layout>
    <CreateChat v-if="createChatVisible"
                :is-visible="createChatVisible"
                :user-id="userInfo.id"
                @close="handlerCreateChat(false)">
    </CreateChat>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import CreateChat from "@/views/common/chat/components/CreateChat.vue";
import ChatService from '@/services/ChatService';
import {Filter} from '@/model/Filter';
import {AuthResponse} from '@/model/AuthResponse';
import {TokenCommon} from '@/common/TokenCommon';
import CircularLoading from "@/components/loading/CircularLoading.vue";
import MessageService from "@/services/MessageService";
import MarkdownView from "@/components/markdown/MarkdownView.vue";

export default defineComponent({
  components: {MarkdownView, CircularLoading, CreateChat},
  created()
  {
    this.handlerInitialize()
  },
  data()
  {
    return {
      chats: [],
      loadingChats: false,
      selectedChat: null,
      userInfo: null as AuthResponse,
      messages: [],
      loadingMessages: false,
      createChatVisible: false,
      questionContext: null,
      sendMessage: false,
      promptTokens: 0,
      completionTokens: 0,
      totalTokens: 0
    }
  },
  methods: {
    handlerInitialize()
    {
      this.userInfo = TokenCommon.getAuthUser()
      this.loadingChats = true;
      const filter = new Filter();
      filter.size = 100
      filter.orders = [{column: 'createTime', order: 'desc'}];
      ChatService.getAll(filter)
        .then(response => {
          this.chats = response.data?.content
        })
        .finally(() => {
          this.loadingChats = false;
        });
    },
    handlerCreateChat(opened: boolean)
    {
      this.createChatVisible = opened;
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerChangeChat()
    {
      this.loadingMessages = true;
      ChatService.getMessages(this.selectedChat.id)
        .then(response => {
          this.messages = response.data
        })
        .finally(() => {
          this.loadingMessages = false;
          this.counterToken()
          this.handlerGoBottom()
        });
    },
    handlerSendMessage()
    {
      this.sendMessage = true;
      const message = {
        content: this.questionContext,
        user: this.userInfo,
        chat: this.selectedChat,
        type: 'question'
      }
      this.messages.push(message)
      this.handlerGoBottom()
      this.questionContext = null;
      MessageService.saveOrUpdate(message)
        .then(response => {
          if (response.status) {
            this.messages.push(response.data)
          }
          else {
            this.$Message.error(response.message)
          }
        })
        .finally(() => {
          this.sendMessage = false
          this.counterToken()
          this.handlerGoBottom()
        })
    },
    handlerGoBottom()
    {
      let scrollElem = this.$refs.scrollDiv;
      setTimeout(() => {
        scrollElem.scrollTo({top: scrollElem.scrollHeight, behavior: 'smooth'});
      }, 0);
    },
    counterToken()
    {
      const answers = this.messages.filter(message => message.type === 'answer')
      this.promptTokens = answers.reduce((sum, message) => sum + message.promptTokens, 0)
      this.completionTokens = answers.reduce((sum, message) => sum + message.completionTokens, 0)
      this.totalTokens = answers.reduce((sum, message) => sum + message.totalTokens, 0)
    }
  }
});
</script>
<style scoped>
</style>
