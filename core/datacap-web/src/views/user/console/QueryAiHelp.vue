<template>
  <div>
    <Modal title="AI" width="80%" :closable="false" v-model="visible" :maskClosable="false" :z-index="9">
      <div style="height: 350px; max-height: 350px;">
        <Tabs v-if="!loading" :model-value="tabValue" :animated="false" @on-click="handlerTab($event)">
          <TabPane :label="$t('common.' + support.toLowerCase())" v-for="support of aiSupports" :name="support" :key="support" :disabled="actionLoading">
            <div style="height: 300px; max-height: 350px; overflow: auto;">
              <Skeleton v-if="actionLoading" loading :title="false" :animated="true"
                        :paragraph="{ rows: 5, width: [100, 200, '300px', '50%', '62%'] }"/>
              <VMarkdownView v-else :mode="'light'" :content="finalContent"></VMarkdownView>
            </div>
          </TabPane>
        </Tabs>
      </div>
      <div class="datacap-drawer-footer">
        <Button type="error" :disabled="actionLoading" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
      </div>
      <Spin size="large" fix :show="loading"></Spin>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import UserService from "@/services/UserService";
import {User, UserQuestion} from "@/model/User";
import {VMarkdownView} from 'vue3-markdown'
import 'vue3-markdown/dist/style.css'
import {Skeleton} from "view-ui-plus";

export default defineComponent({
  name: 'QueryAiHelp',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    content: {
      type: String,
      default: () => ''
    },
    engine: {
      type: String,
      default: () => ''
    },
    error: {
      type: String,
      default: () => ''
    },
    aiSupports: {
      type: Array,
      default: () => []
    }
  },
  components: {Skeleton, VMarkdownView},
  data()
  {
    return {
      loading: false,
      actionLoading: false,
      userInfo: null as User,
      finalContent: null,
      tabValue: 'ANALYSIS'
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      UserService.getInfo()
        .then(response => {
          if (response.status) {
            this.userInfo = response.data;
            this.handlerTab(this.tabValue);
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handlerTab(value: string)
    {
      this.finalContent = null;
      this.actionLoading = true;
      const userQuestion = new UserQuestion();
      userQuestion.type = 'ChatGPT';
      userQuestion.content = this.content;
      userQuestion.transType = value;
      userQuestion.engine = this.engine;
      userQuestion.error = this.error;
      userQuestion.locale = this.$i18n.locale;
      UserService.startChat(userQuestion)
        .then(response => {
          if (response.status) {
            this.finalContent = response.data.answer;
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.actionLoading = false;
        });
    },
    handlerCancel()
    {
      this.visible = false;
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
});
</script>
<style scoped>
.datacap-drawer-footer {
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 0px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: #fff;
}
</style>
