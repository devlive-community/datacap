<template>
  <div class="main">
    <Result>
      <template #title>
        <Avatar src="/static/images/logo.png" size="70" style="background-color: #87d068"/>
      </template>
      <template #desc>
        {{ $t('common.login') }}
      </template>
      <template #actions>
        <div class="datacap-login">
          <Login @on-submit="handlerAuthSignIn">
            <UserName name="username"/>
            <Password name="password"/>
            <Captcha v-if="showCaptcha"
                     class="datacap-login-captcha"
                     name="captcha"
                     :count-down="0"
                     @on-get-captcha="handlerRefererCaptcha">
              <template #text>
                <img :src="'data:image/png;base64,' + captchaImage">
              </template>
            </Captcha>
            <Submit>{{ $t('common.login') }}</Submit>
            <Button style="width: 100%; margin-top: 5px;" type="dashed" @click="handlerGoSignup">{{ $t('common.register') }}</Button>
          </Login>
          <Spin fix
                size="large"
                :show="loading">
          </Spin>
        </div>
      </template>
    </Result>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import {AuthUser} from "@/model/AuthUser";
import {AuthService} from "@/services/AuthService";
import Common from "@/common/Common";
import router from "@/router";
import UserService from "@/services/UserService";
import {createDefaultRouter, createRemoteRouter} from "@/router/default";
import {useI18n} from "vue-i18n";
import CaptchaService from '@/services/CaptchaService';

export default defineComponent({
  setup()
  {
    const i18n = useI18n();
    const handlerGoSignup = () => {
      router.push('/auth/signup');
    }
    return {
      i18n,
      handlerGoSignup
    };
  },
  data()
  {
    return {
      loading: false,
      showCaptcha: false,
      captchaImage: null,
      timestamp: null
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods:
    {
      handlerInitialize()
      {
        this.loading = true
        this.timestamp = Date.parse(new Date().toString());
        CaptchaService.getCaptcha(this.timestamp)
          .then(response => {
            if (response.data !== false) {
              this.showCaptcha = true
              this.captchaImage = response.data?.image
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
      handlerRefererCaptcha()
      {
        this.timestamp = Date.parse(new Date().toString());
        CaptchaService.getCaptcha(this.timestamp)
          .then(response => {
            if (response.data !== false) {
              this.captchaImage = response.data?.image
            }
          })
      },
      handlerAuthSignIn(valid, {username, password, captcha})
      {
        if (valid) {
          const authUser: AuthUser = {
            username: username,
            password: password,
            captcha: captcha,
            timestamp: this.timestamp
          }
          new AuthService().signin(authUser)
            .then(response => {
              if (response.status) {
                localStorage.setItem(Common.token, JSON.stringify(response.data));
                UserService.getMenus()
                  .then(menuResponse => {
                    if (menuResponse.status) {
                      localStorage.setItem(Common.menu, JSON.stringify(menuResponse.data))
                      createDefaultRouter(router)
                      createRemoteRouter(menuResponse.data, router)
                      router.push('/dashboard/index');
                    }
                    else {
                      this.$Message.error(menuResponse.message)
                    }
                  })
              }
              else {
                this.$Message.error(response.message);
                this.handlerRefererCaptcha()
              }
            })
        }
      }
    }
});
</script>
<style scoped>
.datacap-login {
  width: 400px;
  margin: 0 auto !important;
  display: inline-block;
  position: relative;
}

.datacap-auto-login a {
  float: right;
}

.datacap-login-captcha .ivu-btn {
  padding: 0;
}

.datacap-login-captcha .ivu-btn img {
  height: 28px;
  width: 100px;
  position: relative;
  top: 4px;
}
</style>
