<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.chatgpt')"
           :closable="false"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Form :model="chatConfigure"
            :label-width="100">
        <FormItem prop="host"
                  :label="$t('common.proxy')">
          <Input type="text"
                 v-model="chatConfigure.host">
          </Input>
        </FormItem>
        <FormItem name="token"
                  :label="$t('common.token')">
          <Input type="password"
                 v-model="chatConfigure.token">
          </Input>
        </FormItem>
        <FormItem name="timeout"
                  :label="$t('common.timeout')">
          <InputNumber v-model="chatConfigure.timeout"
                       min="1">
          </InputNumber>
        </FormItem>
        <FormItem name="contentCount"
                  :label="$t('setting.contentCount')">
          <InputNumber v-model="chatConfigure.contentCount"
                       min="1"
                       max="10">
          </InputNumber>
        </FormItem>
        <CircularLoading v-if="loadingUserInfo"
                         :show="loadingUserInfo">
        </CircularLoading>
      </Form>
      <template #footer>
        <Button danger
                @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button type="primary"
                :loading="loadingChange"
                @click="handlerSave()">
          {{ $t('common.save') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import UserService from "@/services/UserService";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {ThirdConfigure} from "@/model/User";

export default defineComponent({
  name: 'ChangeChatGpt',
  components: {CircularLoading},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    }
  },
  data()
  {
    return {
      loadingUserInfo: false,
      loadingChange: false,
      chatConfigure: {} as ThirdConfigure
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loadingUserInfo = true
      UserService.getInfo()
        .then(response => {
          if (response.data.chatConfigure) {
            this.chatConfigure = JSON.parse(response.data.chatConfigure)
          }
          else {
            this.chatConfigure = new ThirdConfigure()
          }
        })
        .finally(() => {
          this.loadingUserInfo = false
        })
    },
    handlerSave()
    {
      this.loadingChange = true;
      UserService.changeThirdConfigure(this.chatConfigure)
        .then((response) => {
          if (response.status) {
            this.$Message.success('Success');
            this.handlerCancel()
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.loadingChange = false;
        });
    },
    handlerCancel()
    {
      this.visible = false;
    },
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible;
      },
      set(value: boolean)
      {
        this.$emit('close', value);
      }
    }
  }
});
</script>
<style scoped>
.content {
  background-color: #FFFFFF;
}
</style>
