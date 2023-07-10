<template>
  <div>
    <Card
      dis-hover
      style="width:100%; minHeight: 150px;">
      <template #title>
        {{ $t('setting.accountSetting') }}
      </template>
      <Spin fix v-if="loading"/>
      <div v-else>
        <Layout>
          <Layout>
            <Content class="content" style="padding: 0 80px 0 40px;">
              <Form :label-width="80">
                <FormItem :label="$t('common.username')">
                  <Button type="text" style="float: right;" @click="changeUsername = true">
                    {{ $t('setting.changeUsername') }}
                  </Button>
                </FormItem>
                <FormItem :label="$t('common.password')">
                  <Button type="text" style="float: right;" @click="handlerShowModal('password')">
                    {{ $t('setting.changePassword') }}
                  </Button>
                </FormItem>
                <FormItem :label="$t('common.chatgpt')">
                  <Button type="text" style="float: right;" @click="handlerChatGPT(true)">
                    {{ $t('setting.changeChatGpt') }}
                  </Button>
                </FormItem>
              </Form>
            </Content>
          </Layout>
        </Layout>
      </div>
    </Card>
    <Modal v-if="changePasswordVisible" v-model="changePasswordVisible" :title="$t('setting.changePassword')" :closable="false" :mask-closable="false">
      <Form ref="formCustom" :model="formState" :label-width="80">
        <FormItem :label="$t('setting.oldPassword')" prop="oldPassword">
          <Input type="password" v-model="formState.oldPassword"></Input>
        </FormItem>
        <FormItem :label="$t('setting.newPassword')" name="newPassword">
          <Input type="password" v-model="formState.newPassword"></Input>
        </FormItem>
        <FormItem :label="$t('setting.confirmPassword')" name="confirmPassword">
          <Input type="password" v-model="formState.confirmPassword"></Input>
        </FormItem>
      </Form>
      <template #footer>
        <Button danger @click="handlerCloseModal('password')">{{ $t('common.cancel') }}</Button>
        <Button type="primary" :loading="loading" @click="handlerChangePassword()">{{ $t('common.save') }}</Button>
      </template>
    </Modal>
    <ChangeUsername v-if="changeUsername"
                    :isVisible="changeUsername"
                    @close="changeUsername = false">
    </ChangeUsername>
    <ChangeChatGpt v-if="changeChatGpt"
                   :is-visible="changeChatGpt"
                   @close="handlerChatGPT(false)">
    </ChangeChatGpt>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive} from 'vue';
import UserService from "@/services/UserService";
import {UserPassword} from "@/model/UserPassword";
import Common from "@/common/Common";
import router from "@/router";
import ChangeUsername from "@/views/user/profile/components/ChangeUsername.vue";
import ChangeChatGpt from "@/views/user/profile/components/ChangeChatGpt.vue";

export default defineComponent({
  components: {ChangeChatGpt, ChangeUsername},
  setup()
  {
    const formState = reactive<UserPassword>({
      confirmPassword: '',
      newPassword: '',
      oldPassword: ''
    });
    return {
      formState
    }
  },
  data()
  {
    return {
      changePasswordVisible: false,
      changeUsername: false,
      changeChatGpt: false,
      loading: false
    }
  },
  methods: {
    handlerShowModal(type: string)
    {
      if (type === 'password') {
        this.changePasswordVisible = true;
      }
    },
    handlerCloseModal(type: string)
    {
      if (type === 'password') {
        this.changePasswordVisible = false;
      }
    },
    handlerChangePassword()
    {
      this.loading = true;
      UserService.changePassword(this.formState)
        .then((response) => {
          if (response.status) {
            // message.success('Success');
            localStorage.removeItem(Common.token);
            localStorage.removeItem(Common.menu)
            this.changePasswordVisible = false;
            router.push('/auth/signin');
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handlerChatGPT(opened: boolean)
    {
      this.changeChatGpt = opened
    }
  }
});
</script>
<style scoped>
.content {
  background-color: #FFFFFF;
}
</style>
