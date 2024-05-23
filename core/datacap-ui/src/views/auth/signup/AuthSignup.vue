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
            <CardTitle class="text-2xl text-center">{{ $t('user.common.signup') }}</CardTitle>
            <CardDescription class="text-center">{{ $t('user.auth.signupTip') }}</CardDescription>
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
            <div class="grid gap-2">
              <FormField v-slot="{ componentField }" name="confirmPassword">
                <FormItem>
                  <FormLabel>{{ $t('user.common.confirmPassword') }}</FormLabel>
                  <FormControl>
                    <Input v-model="formState.confirmPassword" v-bind="componentField" type="password" :placeholder="$t('user.auth.confirmPasswordTip')"/>
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
              {{ $t('user.common.signup') }}
            </Button>
          </CardFooter>
        </form>
        <p class='mt-4 px-8 text-center text-sm text-muted-foreground mb-10'>
          {{ $t('user.auth.hasUserTip') }}
          <RouterLink to="/auth/signin">
            <span class=" text-blue-600 hover:underline">{{ $t('user.common.signin') }}</span>
          </RouterLink>
        </p>
      </Card>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, inject, ref } from 'vue'
import { Loader2 } from 'lucide-vue-next'
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import Button from '@/views/ui/button'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { UserRequest } from '@/model/user/request/user'
import { toTypedSchema } from '@vee-validate/zod'
import * as z from 'zod'
import { useForm } from 'vee-validate'
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import CaptchaService from '@/services/captcha'
import UserService from '@/services/user'
import router from '@/router'
import { ToastUtils } from '@/utils/toast'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'

export default defineComponent({
  name: 'AuthSignup',
  components: {
    CircularLoading,
    FormField, FormItem, FormMessage, FormControl, FormLabel,
    CardDescription, Card, CardContent, CardFooter, CardTitle, CardHeader,
    AvatarImage, AvatarFallback, Avatar,
    Label, Input, Button,
    Loader2
  },
  setup()
  {
    let loading = ref(false)
    let submitting = ref(false)
    let showCaptcha = ref(false)
    let captchaImage = ref(null)
    const captchaLoading = ref(false)
    const $t: any = inject('$t')
    const formState = ref<UserRequest>({ username: null, password: null })
    const validator = z
        .object({
          username: z.string({ required_error: $t('user.auth.usernameTip') })
                     .min(2, $t('user.auth.usernameSizeTip'))
                     .max(20, $t('user.auth.usernameSizeTip')),
          password: z.string({ required_error: $t('user.auth.passwordTip') })
                     .min(6, $t('user.auth.passwordSizeTip'))
                     .max(20, $t('user.auth.passwordSizeTip')),
          confirmPassword: z.string({ required_error: $t('user.auth.confirmPasswordTip') })
                            .min(6, $t('user.auth.passwordSizeTip'))
                            .max(50, $t('user.auth.passwordSizeTip')),
          captcha: z.string({ required_error: $t('user.auth.captchaTip') })
                    .min(1, $t('user.auth.captchaSizeTip'))
                    .max(6, $t('user.auth.captchaSizeTip'))
        })
        .refine((data) => data.password === data.confirmPassword, {
          message: $t('user.auth.confirmPasswordTip'),
          path: ['confirmPassword']
        })
    const formSchema = toTypedSchema(validator)

    const { handleSubmit } = useForm({
      validationSchema: formSchema
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
      UserService.signup(formState.value)
                 .then(response => {
                   if (response.status) {
                     router.push('/auth/signin')
                   }
                   else {
                     ToastUtils.error(response.message)
                     refererCaptcha()
                   }
                 })
                 .finally(() => submitting.value = false)
    })

    return {
      loading,
      submitting,
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