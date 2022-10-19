<template>
  <div class="main">
    <a-row :gutter="[8,8]">
      <a-col :span="8"/>
      <a-col :span="8">
        <a-result :title="'DataCap ' + $t('common.login')">
          <template #icon>
            <smile-twoTone/>
          </template>
          <template #extra>
            <a-alert v-if="formState.message" :message="formState.message" type="error" show-icon style="margin-bottom: 10px;"/>
            <a-form :model="formState" name="basic" :label-col="{ span: 6 }"
                    :wrapper-col="{ span: 16 }" @finish="handlerAuthSignin">
              <a-form-item :label="$t('common.username')" name="username"
                           :rules="[{ required: true, message: $t('required.username') }]">
                <a-input v-model:value="formState.username"/>
              </a-form-item>
              <a-form-item :label="$t('common.password')" name="password"
                           :rules="[{ required: true, message: $t('required.password') }]">
                <a-input-password v-model:value="formState.password"/>
              </a-form-item>
              <a-form-item :wrapper-col="{ offset: 6, span: 16 }">
                <a-button :disabled="disabled" :loading="formState.loading" type="primary" html-type="submit">{{ $t('common.login') }}</a-button>
                <a-button type="dashed" style="margin-left: 10px;" @click="handlerGoSignup">{{ $t('common.register') }}</a-button>
              </a-form-item>
            </a-form>
          </template>
        </a-result>
      </a-col>
      <a-col :span="8"/>
    </a-row>
  </div>
</template>

<script lang="ts">
import {computed, defineComponent, reactive} from 'vue';
import {AuthUser} from "@/model/AuthUser";
import {AuthService} from "@/services/AuthService";
import Common from "@/common/Common";
import router from "@/router";

export default defineComponent({
  setup()
  {
    const formState = reactive<AuthUser>({
      username: '',
      password: ''
    });

    const disabled = computed(() => {
      return !(formState.username && formState.password);
    });

    const handlerAuthSignin = (values: any) => {
      formState.loading = true;
      new AuthService().signin(values)
        .then(response => {
          if (response.status) {
            localStorage.setItem(Common.token, JSON.stringify(response.data));
            router.push('/');
          }
          else {
            formState.message = response.message;
          }
        })
        .finally(() => {
          formState.loading = undefined;
        });
    };

    const handlerGoSignup = () => {
      router.push('/auth/signup');
    }
    return {
      formState,
      disabled,
      handlerAuthSignin,
      handlerGoSignup
    };
  },
});
</script>
