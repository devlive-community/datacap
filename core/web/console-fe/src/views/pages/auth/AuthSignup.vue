<template>
  <div class="main">
    <Result>
      <template #title>
        <Avatar src="/static/images/logo.png" size="70" style="background-color: #87d068"/>
      </template>
      <template #desc>
        {{ $t('common.register') }}
      </template>
      <template #actions>
        <div class="datacap-register">
          <Login ref="form" @on-submit="handlerSubmit">
            <UserName name="username"/>
            <Poptip trigger="focus" placement="right" width="240">
              <Password name="password" :rules="passwordRule" :placeholder="$t('signup.passwordSizeEquals')" @on-change="handlerChangePassword"/>
              <template #content>
                <div class="datacap-register-tip">
                  <div class="datacap-register-tip-title" :class="passwordTip.class">
                    强度：{{ passwordTip.strong }}
                  </div>
                  <Progress :percent="passwordTip.percent" hide-info :stroke-width="6" :stroke-color="passwordTip.color"/>
                  <div class="datacap-register-tip-desc">
                    {{ $t('signup.passwordSizeTip') }}
                  </div>
                </div>
              </template>
            </Poptip>
            <Password name="passwordConfirm" :rules="passwordConfirmRule" placeholder="确认密码"/>
            <Submit>{{ $t('common.register') }}</Submit>
            <Button style="width: 100%; margin-top: 5px;" type="dashed" @click="handlerGoSignIn">{{ $t('common.login') }}</Button>
          </Login>
        </div>
      </template>
    </Result>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import {AuthService} from "@/services/AuthService";
import router from "@/router";
import {AuthUser} from "@/model/AuthUser";

export default defineComponent({
  data()
  {
    const validatePassCheck = (rule, value, callback) => {
      // @ts-ignore
      if (value !== this.$refs.form.formValidate.password) {
        callback(new Error('两次输入的密码不匹配！'));
      }
      else {
        callback();
      }
    };
    return {
      passwordRule: [
        {
          required: true, message: '密码不能为空！', trigger: 'change'
        },
        {
          min: 6, message: '密码不能少于6位！', trigger: 'change'
        }
      ],
      passwordConfirmRule: [
        {
          required: true, message: '确认密码不能为空！', trigger: 'change'
        },
        {validator: validatePassCheck, trigger: 'change'}
      ],
      passwordLen: 0
    }
  },
  computed: {
    passwordTip()
    {
      let strong = '强';
      let className = 'strong';
      let percent = this.passwordLen > 10 ? 10 : this.passwordLen;
      let color = '#19be6b';

      if (this.passwordLen < 6) {
        strong = '太短';
        className = 'low';
        color = '#ed4014';
      }
      else if (this.passwordLen < 10) {
        strong = '中';
        className = 'medium';
        color = '#ff9900';
      }
      return {
        strong,
        class: 'datacap-register-tip-' + (this.passwordLen < 6 ? 'low' : (this.passwordLen < 10 ? 'medium' : 'strong')),
        percent: percent * 10,
        color
      }
    }
  },
  methods: {
    handlerChangePassword(val)
    {
      this.passwordLen = val.length;
    },
    handlerSubmit(valid, {username, password, passwordConfirm})
    {
      if (valid) {
        const authUser: AuthUser = {
          username: username,
          password: password
        }
        new AuthService().signup(authUser)
          .then(response => {
            if (response.status) {
              router.push('/auth/signin');
            }
            else {
              this.$Message.error(response.message);
            }
          });
      }
    },
    handlerGoSignIn()
    {
      router.push('/auth/signin');
    }
  }
});
</script>
<style>
.datacap-register {
  width: 400px;
  margin: 0 auto !important;
}

.datacap-register .ivu-poptip, .datacap-register .ivu-poptip-rel {
  display: block;
}

.datacap-register-tip {
  text-align: left;
}

.datacap-register-tip-low {
  color: #ed4014;
}

.datacap-register-tip-medium {
  color: #ff9900;
}

.datacap-register-tip-strong {
  color: #19be6b;
}

.datacap-register-tip-title {
  font-size: 14px;
}

.datacap-register-tip-desc {
  white-space: initial;
  font-size: 14px;
  margin-top: 6px;
}
</style>
