<template>
  <div class="relative flex flex-col justify-center items-center min-h-screen overflow-hidden">
    <div class="w-full m-auto bg-white lg:max-w-lg">
      <div class='mb-4 flex items-center justify-center mb-10'>
        <Avatar>
          <AvatarImage src="/static/images/logo.png"></AvatarImage>
          <AvatarFallback>DataCap</AvatarFallback>
        </Avatar>
        <h1 class='text-xl font-medium ml-5'>DataCap Console</h1>
      </div>
      <Card>
        <CardHeader class="space-y-1">
          <CardTitle class="text-2xl text-center">{{ $t('user.common.signin') }}</CardTitle>
          <CardDescription class="text-center">{{ $t('user.auth.signinTip') }}</CardDescription>
        </CardHeader>
        <CardContent class="grid gap-4">
          <div class="grid gap-2">
            <Label htmlFor="email">{{ $t('user.common.username') }}</Label>
            <Input v-model="formState.username!" id="email" type="email" :placeholder="$t('user.auth.usernameTip')"/>
          </div>
          <div class="grid gap-2">
            <Label htmlFor="password">{{ $t('user.common.password') }}</Label>
            <Input v-model="formState.password!" id="password" type="password" :placeholder="$t('user.auth.passwordTip')"/>
          </div>
          <div class="grid gap-2">
            <Label htmlFor="password">{{ $t('user.common.captcha') }}</Label>
            <div class="flex w-full items-center gap-2">
              <Input v-model="formState.captcha" id="captcha" type="text" :placeholder="$t('user.auth.captchaTip')"/>
              <Button variant="secondary" style="padding: 0" @click="handlerRefererCaptcha">
                <img style="min-width: 120px; height: 100%;" :src="'data:image/png;base64,' + captchaImage"/>
              </Button>
            </div>
          </div>
        </CardContent>
        <CardFooter class="flex flex-col">
          <Button class="w-full" @click="handlerSignIn">{{ $t('user.common.signin') }}</Button>
        </CardFooter>
        <p class='mt-4 px-8 text-center text-sm text-muted-foreground mb-10'>
          {{ $t('user.auth.notUserTip') }}
          <RouterLink to="/auth/signup">
            <span class=" text-blue-600 hover:underline">{{ $t('user.common.signup') }}</span>
          </RouterLink>
        </p>
      </Card>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Checkbox } from '@/components/ui/checkbox'
import UserService from '@/services/user'
import CaptchaService from '@/services/captcha'
import { UserRequest } from '@/model/user/request/user'
import { useToast } from '@/components/ui/toast'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'

const {toast} = useToast()
export default defineComponent({
  name: 'AuthSignin',
  components: {AvatarImage, AvatarFallback, Avatar, Checkbox, Label, Input, Button, CardFooter, CardContent, CardDescription, CardTitle, CardHeader, Card},
  data()
  {
    return {
      formState: {} as UserRequest,
      captchaImage: null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.handlerRefererCaptcha()
    },
    handlerRefererCaptcha()
    {
      this.formState.timestamp = Date.parse(new Date().toString());
      CaptchaService.getCaptcha(this.formState.timestamp)
          .then(response => {
            if (response.data !== false) {
              this.captchaImage = response.data?.image
            }
          })
    },
    handlerSignIn()
    {
      UserService.signin(this.formState)
          .then(response => {
            if (response.status) {
              console.log(response)
            }
            else {
              toast({
                description: response.message,
                variant: 'destructive'
              })
              this.handlerRefererCaptcha()
            }
          })
    }
  }
})
</script>
