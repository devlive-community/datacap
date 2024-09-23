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
        <CircularLoading v-if="loading" :show="loading"/>
        <form v-else @submit="onSubmit">
          <CardHeader class="space-y-1">
            <CardTitle class="text-2xl text-center">{{ $t('user.common.signin') }}</CardTitle>
            <CardDescription class="text-center">{{ $t('user.auth.signinTip') }}</CardDescription>
          </CardHeader>
          <CardContent class="grid gap-4">
            <div class="grid gap-2">
              <FormField v-slot="{ componentField }" name="username">
                <FormItem>
                  <FormLabel>{{ $t('user.common.username') }}</FormLabel>
                  <FormControl>
                    <Input type="text" v-model="formState.username" v-bind="componentField" :placeholder="$t('user.auth.usernameTip')"/>
                  </FormControl>
                  <FormMessage/>
                </FormItem>
              </FormField>
            </div>
            <div class="grid gap-2">
              <FormField v-slot="{ componentField }" name="password">
                <FormItem>
                  <FormLabel>{{ $t('user.common.password') }}</FormLabel>
                  <FormControl>
                    <Input v-model="formState.password" v-bind="componentField" type="password" :placeholder="$t('user.auth.passwordTip')"/>
                  </FormControl>
                  <FormMessage/>
                </FormItem>
              </FormField>
            </div>
            <div v-if="showCaptcha" class="grid gap-2">
              <FormField v-slot="{ componentField }" name="captcha">
                <FormItem>
                  <FormLabel>{{ $t('user.common.captcha') }}</FormLabel>
                  <FormControl>
                    <div class="flex w-full items-center gap-2">
                      <Input v-model="formState.captcha" v-bind="componentField" type="text" :placeholder="$t('user.auth.captchaTip')"/>
                      <Button variant="secondary" :loading="captchaLoading" :disabled="captchaLoading" style="padding: 0" @click="refererCaptcha">
                        <img v-if="!captchaLoading" style="min-width: 120px; height: 100%;" :src="'data:image/png;base64,' + captchaImage"/>
                      </Button>
                    </div>
                  </FormControl>
                  <FormMessage/>
                </FormItem>
              </FormField>
            </div>
          </CardContent>
          <CardFooter class="flex flex-col">
            <Button class="w-full" type="submit" :disabled="submitting">
              <Loader2 v-if="submitting" class="w-4 h-4 mr-2 animate-spin"/>
              {{ $t('user.common.signin') }}
            </Button>
          </CardFooter>
        </form>
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
import { defineComponent, inject, ref, watch } from 'vue'
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import Button from '@/views/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Checkbox } from '@/components/ui/checkbox'
import UserService from '@/services/user'
import CaptchaService from '@/services/captcha'
import { UserRequest } from '@/model/user/request/user'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Loader2 } from 'lucide-vue-next'
import * as z from 'zod'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
import { ToastUtils } from '@/utils/toast'
import CommonUtils from '@/utils/common'
import { HttpUtils } from '@/utils/http'
import router from '@/router'
import { createDefaultRouter } from '@/router/default'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'

export default defineComponent({
  name: 'AuthSignin',
  components: {
    CircularLoading,
    FormField, FormControl, FormMessage, FormLabel, FormItem,
    AvatarImage, AvatarFallback, Avatar,
    Checkbox, Label, Input, Button,
    CardFooter, CardContent, CardDescription, CardTitle, CardHeader, Card,
    Loader2
  },
  setup()
  {
    let loading = ref(false)
    let showCaptcha = ref(false)
    let captchaImage = ref(null)
    const $t: any = inject('$t')
    let submitting = ref(false)
    const captchaLoading = ref(false)
    const formState = ref<UserRequest>({ username: null, password: null })

    const validationSchema = ref(
        z.object({
          username: z.string({ required_error: $t('user.auth.usernameTip') })
                     .min(2, $t('user.auth.usernameSizeTip'))
                     .max(20, $t('user.auth.usernameSizeTip')),
          password: z.string({ required_error: $t('user.auth.passwordTip') })
                     .min(6, $t('user.auth.passwordSizeTip'))
                     .max(20, $t('user.auth.passwordSizeTip'))
        })
    )

    const initializeForm = () => {
      const formSchema = toTypedSchema(validationSchema.value)
      return useForm({ validationSchema: formSchema })
    }

    let { handleSubmit } = initializeForm()

    watch(showCaptcha, (newValue) => {
      if (newValue) {
        validationSchema.value = validationSchema.value.extend({
          captcha: z.string({ required_error: $t('user.auth.captchaTip') })
                    .min(1, $t('user.auth.captchaSizeTip'))
                    .max(6, $t('user.auth.captchaSizeTip'))
        })
        const { handleSubmit: newHandleSubmit } = initializeForm()
        handleSubmit = newHandleSubmit
      }
    })

    const refererCaptcha = () => {
      captchaLoading.value = true
      formState.value.timestamp = Date.parse(new Date().toString())
      CaptchaService.getCaptcha(formState.value.timestamp)
                    .then(response => {
                      if (response.data !== false) {
                        showCaptcha.value = true
                        captchaImage.value = response.data.image
                      }
                    })
                    .finally(() => {
                      captchaLoading.value = false
                      loading.value = false
                    })
    }

    const onSubmit = handleSubmit(() => {
      submitting.value = true
      UserService.signin(formState.value)
                 .then(response => {
                   if (response.status) {
                     localStorage.setItem(CommonUtils.token, JSON.stringify(response.data))
                     // Get user information and user menus
                     const client = new HttpUtils().getAxios()
                     client.all([UserService.getMenus(), UserService.getInfo()])
                           .then(client.spread((fetchMenu, fetchInfo) => {
                             if (fetchMenu.status && fetchInfo.status) {
                               localStorage.setItem(CommonUtils.menu, JSON.stringify(fetchMenu.data))
                               createDefaultRouter(router)
                               localStorage.setItem(CommonUtils.userEditorConfigure, JSON.stringify(fetchInfo.data.editorConfigure))
                               router.push('/home')
                             }
                             else {
                               if (!fetchMenu.status) {
                                 ToastUtils.error(fetchMenu.message)
                               }
                               if (!fetchInfo.status) {
                                 ToastUtils.error(fetchInfo.message)
                               }
                             }
                           }))
                   }
                   else {
                     ToastUtils.error(response.message)
                     refererCaptcha()
                   }
                 })
                 .finally(() => submitting.value = false)
    })

    return {
      submitting,
      loading,
      showCaptcha,
      formState,
      captchaImage,
      onSubmit,
      refererCaptcha,
      captchaLoading
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      this.refererCaptcha()
    }
  }
})
</script>
