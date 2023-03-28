<template>
  <div>
    <Card style="width:100%; minHeight: 150px;">
      <template #title>
        {{ $t('common.chatgpt') }}
      </template>
      <template #extra>
        <Tooltip :content="$t('common.settings')" transfer>
          <Button type="primary" size="small" shape="circle" icon="ios-cog" @click="handlerVisibleDetail(true)"></Button>
        </Tooltip>
      </template>
      <div>
        <Layout style="background-color: #FFFFFF;">
          <Content style="padding: 0px 0px 0px 10px;">
            <div ref="scrollDiv" style="height: 300px; max-height: 300px; overflow: auto;">
              <List item-layout="vertical">
                <ListItem v-for="item in userQuestionItems" :key="item">
                  <ListItemMeta style="margin-bottom: 0px;">
                    <template #title>
                      {{ item.isSelf ? username : 'ChatGPT' }}
                    </template>
                    <template #avatar>
                      <Avatar v-if="item.isSelf" icon="ios-person"></Avatar>
                      <Avatar v-else icon="md-ionitron" style="background-color: #87d068;"></Avatar>
                    </template>
                  </ListItemMeta>
                  <div style="margin: 0px 10px;">
                    <VMarkdownView v-if="item.content" :mode="'light'" :content="item.content"></VMarkdownView>
                  </div>
                </ListItem>
              </List>
            </div>
          </Content>
          <Footer style="background-color: #FFFFFF;">
            <Row>
              <Col span="20">
                <Input v-model="userQuestionContext" :disabled="!userInfo?.thirdConfigure?.chatgpt?.token" type="textarea" :autosize="{minRows: 2,maxRows: 5}"/>
              </Col>
              <Col span="2" offset="1">
                <Button :disabled="!userInfo?.thirdConfigure?.chatgpt?.token" type="primary" icon="md-send"
                        :loading="startChatLoading" @click="handlerStartChat()">{{ $t('common.send') }}
                </Button>
              </Col>
            </Row>
          </Footer>
        </Layout>
      </div>
      <Spin size="large" fix :show="loading"></Spin>
    </Card>

    <Modal v-if="visibleModel" v-model="visibleModel" :title="$t('common.chatgpt') +' ' + $t('common.settings')"
           :mask-closable="false" :closable="false">
      <Form :label-width="80">
        <FormItem :label="$t('common.token')">
          <Input v-model="userInfo.thirdConfigure.chatgpt.token" type="text" placeholder="example: sk-xxxx"/>
        </FormItem>
        <FormItem :label="$t('common.proxy')">
          <Switch v-model="userInfo.thirdConfigure.chatgpt.proxy"/>
        </FormItem>
        <FormItem v-if="userInfo.thirdConfigure.chatgpt.proxy" :label="$t('common.host')">
          <Input v-model="userInfo.thirdConfigure.chatgpt.host" type="text"/>
        </FormItem>
        <FormItem v-if="userInfo.thirdConfigure.chatgpt.proxy" :label="$t('common.port')">
          <InputNumber v-model="userInfo.thirdConfigure.chatgpt.port"/>
        </FormItem>
      </Form>
      <template #footer>
        <Space>
          <Button type="error" @click="handlerVisibleDetail(false)">{{ $t('common.cancel') }}</Button>
          <Button type="primary" icon="md-document" @click="handlerSave()">{{ $t('common.save') }}</Button>
        </Space>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import UserService from "@/services/UserService";
import {ThirdConfigure, User, UserQuestion, UserQuestionItem} from '@/model/User';
import Common from "@/common/Common";
import {AuthResponse} from "@/model/AuthResponse";
import {VMarkdownView} from 'vue3-markdown'
import 'vue3-markdown/dist/style.css'

export default defineComponent({
  setup()
  {
    let username;
    const authUser = JSON.parse(localStorage.getItem(Common.token) || '{}') as AuthResponse;
    if (authUser) {
      username = authUser.username;
    }
    return {
      username
    }
  },
  components: {VMarkdownView},
  created()
  {
    this.handlerInitialize()
  },
  data()
  {
    return {
      loading: false,
      visibleModel: false,
      startChatLoading: false,
      userInfo: null as User,
      userQuestionContext: null,
      userQuestionItems: null as UserQuestionItem[]
    }
  },
  methods: {
    handlerInitialize()
    {
      this.userQuestionItems = [];
      this.loading = true;
      UserService.getInfo()
        .then(response => {
          if (response.status) {
            this.userInfo = response.data;
            // If the initialization property has not been set
            if (!this.userInfo.thirdConfigure) {
              this.userInfo.thirdConfigure = {
                chatgpt: new ThirdConfigure()
              }
            }
            else {
              this.userInfo.thirdConfigure = JSON.parse(this.userInfo.thirdConfigure);
            }
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handlerVisibleDetail(value: boolean)
    {
      this.visibleModel = value;
      if (!value) {
        this.handlerInitialize();
      }
    },
    handlerSave()
    {
      UserService.changeThirdConfigure(this.userInfo.thirdConfigure)
        .then(response => {
          this.$Message.success(response.message);
          this.handlerVisibleDetail(false);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handlerStartChat()
    {
      const userQuestion = new UserQuestion();
      userQuestion.type = 'ChatGPT';
      userQuestion.content = this.userQuestionContext;
      userQuestion.from = 'chat';
      userQuestion.newChat = this.userQuestionItems.length > 0 ? false : true;
      const question = new UserQuestionItem();
      question.content = this.userQuestionContext;
      question.isSelf = true;
      this.userQuestionItems.push(question);
      this.handlerGoBottom();
      this.startChatLoading = true;
      this.userQuestionContext = null;
      UserService.startChat(userQuestion)
        .then(response => {
          if (response.status) {
            const answer = new UserQuestionItem();
            answer.content = response.data.toString();
            answer.isSelf = false;
            this.userQuestionItems.push(answer);
            this.handlerGoBottom();
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.startChatLoading = false;
        });
    },
    handlerGoBottom()
    {
      let scrollElem = this.$refs.scrollDiv;
      setTimeout(() => {
        scrollElem.scrollTo({top: scrollElem.scrollHeight, behavior: 'smooth'});
      }, 0);
    }
  }
});
</script>
<style scoped>
.content {
  background-color: #FFFFFF;
}
</style>
