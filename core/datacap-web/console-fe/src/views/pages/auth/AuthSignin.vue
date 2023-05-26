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
            <Submit>{{ $t('common.login') }}</Submit>
            <Button style="width: 100%; margin-top: 5px;" type="dashed" @click="handlerGoSignup">{{ $t('common.register') }}</Button>
          </Login>
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

export default defineComponent({
  setup()
  {
    const handlerGoSignup = () => {
      router.push('/auth/signup');
    }
    return {
      handlerGoSignup
    };
  },
  methods:
    {
      handlerAuthSignIn(valid, {username, password})
      {
        if (valid) {
          const authUser: AuthUser = {
            username: username,
            password: password
          }
          new AuthService().signin(authUser)
            .then(response => {
              if (response.status) {
                localStorage.setItem(Common.token, JSON.stringify(response.data));
                UserService.getMenus()
                  .then(menuResponse => {
                    if (menuResponse.status) {
                      localStorage.setItem(Common.menu, JSON.stringify(menuResponse.data))
                      router.push('/');
                    }
                    else {
                      this.$Message.error(menuResponse.message)
                    }
                  })
              }
              else {
                this.$Message.error(response.message);
              }
            })
        }
      }
    }
});
</script>
<style>
.datacap-login {
  width: 400px;
  margin: 0 auto !important;
}

.datacap-auto-login a {
  float: right;
}
</style>
