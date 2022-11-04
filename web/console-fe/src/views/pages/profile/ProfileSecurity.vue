<template>
  <a-layout-content>
    <a-card>
      <template #title>
        {{ $t('setting.security') }}
      </template>
      <a-spin :spinning="loading">
        <a-layout style="background-color: #ffffff">
          <a-layout-content>
            <a-form :model="formState" v-bind="layout">
              <a-form-item :name="['password']" :label="$t('common.password')">
                <a-button type="link" style="float: right;" @click="handlerShowModal('password')">
                  {{ $t('setting.changePassword') }}
                </a-button>
              </a-form-item>
            </a-form>
          </a-layout-content>
          <a-layout-sider style="background-color: #ffffff; margin-left: 60px;">
          </a-layout-sider>
        </a-layout>
      </a-spin>
    </a-card>
    <a-modal v-if="changePasswordVisible" v-model:visible="changePasswordVisible"
             :closable="false" :mask-closable="false"
             :title="$t('setting.changePassword')">
      <a-form :model="formState" :label-col="{ span: 5 }">
        <a-form-item :label="$t('setting.oldPassword')" name="oldPassword">
          <a-input type="password" v-model:value="formState.oldPassword"/>
        </a-form-item>
        <a-form-item :label="$t('setting.newPassword')" name="newPassword">
          <a-input type="password" v-model:value="formState.newPassword"/>
        </a-form-item>
        <a-form-item :label="$t('setting.confirmPassword')" name="confirmPassword">
          <a-input type="password" v-model:value="formState.confirmPassword"/>
        </a-form-item>
      </a-form>
      <template #footer>
        <a-button danger @click="handlerCloseModal('password')">{{ $t('common.cancel') }}</a-button>
        <a-button type="primary" :loading="loading" @click="handlerChangePassword()">{{ $t('common.save') }}</a-button>
      </template>
    </a-modal>
  </a-layout-content>
</template>
<script lang="ts">
import {defineComponent, reactive} from 'vue';
import UserService from "@/services/UserService";
import {UserPassword} from "@/model/UserPassword";
import {message} from "ant-design-vue";
import Common from "@/common/Common";
import router from "@/router";

const layout = {
  labelCol: {span: 8},
  wrapperCol: {span: 16},
};

export default defineComponent({
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
      loading: false,
      layout
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
            message.success('Success');
            localStorage.removeItem(Common.token);
            this.changePasswordVisible = false;
            router.push('/auth/signin');
          }
          else {
            message.error(response.message);
          }
        })
        .finally(() => {
          this.loading = false;
        });
    }
  }
});
</script>
