<template>
  <div>
    <form class="space-y-8" @submit="onSubmit" v-if="formState">
      <FormField v-slot="{ componentField }" name="oldPassword">
        <FormItem>
          <FormLabel>{{ $t('user.common.oldPassword') }}</FormLabel>
          <FormControl>
            <Input type="password" v-model="formState.oldPassword" v-bind="componentField"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.oldPassword') }}</FormDescription>
          <FormMessage/>
        </FormItem>
      </FormField>
      <FormField v-slot="{ componentField }" name="newPassword">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('user.common.newPassword') }}</FormLabel>
          <FormControl>
            <Input type="password" v-model="formState.newPassword" v-bind="componentField"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.newPassword') }}</FormDescription>
          <FormMessage/>
        </FormItem>
      </FormField>
      <FormField v-slot="{ componentField }" name="confirmPassword">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('user.common.confirmPassword') }}</FormLabel>
          <FormControl>
            <Input v-model="formState.confirmPassword" v-bind="componentField"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.confirmPassword') }}</FormDescription>
          <FormMessage/>
        </FormItem>
      </FormField>
      <div class="flex justify-start">
        <Button type="submit">
          <Loader2 v-if="loading" class="w-full justify-center animate-spin mr-3"/>
          {{ $t('common.save') }}
        </Button>
      </div>
    </form>
  </div>
</template>

<script lang="ts">
import { defineComponent, inject, ref } from 'vue'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import UserService from '@/services/user'
import { UserPasswordModel } from '@/model/user'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { ToastUtils } from '@/utils/toast'
import Common from '@/utils/common'
import { Loader2 } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import * as z from 'zod'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
import router from '@/router';

export default defineComponent({
  name: 'PasswordForm',
  components: {
    Button,
    Loader2,
    CircularLoading,
    Input,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage
  },
  setup()
  {
    let loading = ref(false)
    const $t: any = inject('$t')
    const formState = ref<UserPasswordModel>({oldPassword: null, newPassword: null, confirmPassword: null})
    const validator = z
        .object({
          oldPassword: z.string({required_error: $t('user.auth.passwordTip')})
              .min(6, $t('user.auth.passwordSizeTip'))
              .max(20, $t('user.auth.passwordSizeTip')),
          confirmPassword: z.string({required_error: $t('user.auth.confirmPasswordTip')})
              .min(6, $t('user.auth.passwordSizeTip'))
              .max(50, $t('user.auth.passwordSizeTip')),
          newPassword: z.string({required_error: $t('user.auth.confirmPasswordTip')})
              .min(6, $t('user.auth.passwordSizeTip'))
              .max(50, $t('user.auth.passwordSizeTip'))
        })
        .refine((data) => data.newPassword === data.confirmPassword, {
          message: $t('user.auth.confirmPasswordTip'),
          path: ['confirmPassword']
        })
    const formSchema = toTypedSchema(validator)

    const {handleSubmit} = useForm({
      validationSchema: formSchema
    })

    const onSubmit = handleSubmit(() => {
      loading.value = true
      UserService.changePassword(formState.value as UserPasswordModel)
          .then((response) => {
            if (response.status) {
              ToastUtils.success($t('user.tip.changePasswordSuccessfully') as string)
              localStorage.removeItem(Common.token)
              localStorage.removeItem(Common.menu)
              router.push('/auth/signin')
            }
            else {
              ToastUtils.error(response.message)
            }
          })
          .finally(() => loading.value = false)
    })

    return {
      loading,
      formState,
      onSubmit
    }
  }
});
</script>
