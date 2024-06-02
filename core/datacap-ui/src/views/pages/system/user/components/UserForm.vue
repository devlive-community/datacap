<template>
  <form class="space-y-4" @submit="onSubmit" v-if="formState">
    <FormField v-slot="{ componentField }" name="username">
      <FormItem>
        <FormLabel>{{ $t('user.common.username') }}</FormLabel>
        <FormControl>
          <Input type="text" v-model="formState.username" v-bind="componentField"/>
        </FormControl>
        <FormMessage/>
      </FormItem>
    </FormField>
    <FormField v-slot="{ componentField }" name="password">
      <FormItem class="space-y-1">
        <FormLabel>{{ $t('user.common.password') }}</FormLabel>
        <FormControl>
          <Input type="password" v-model="formState.password" v-bind="componentField"/>
        </FormControl>
        <FormMessage/>
      </FormItem>
    </FormField>
    <FormField v-slot="{ componentField }" name="confirmPassword">
      <FormItem class="space-y-1">
        <FormLabel>{{ $t('user.common.confirmPassword') }}</FormLabel>
        <FormControl>
          <Input v-model="formState.confirmPassword" v-bind="componentField"/>
        </FormControl>
        <FormMessage/>
      </FormItem>
    </FormField>
    <div class="flex justify-end">
      <Button type="submit" :disabled="loading">
        <Loader2 v-if="loading" :size="20" class="w-full justify-center animate-spin mr-3"/>
        {{ $t('common.save') }}
      </Button>
    </div>
  </form>
</template>

<script lang="ts">
import { defineComponent, inject, ref } from 'vue'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { UserModel } from '@/model/user'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { Loader2 } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import * as z from 'zod'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
import UserService from '@/services/user'
import { ToastUtils } from '@/utils/toast.ts'

export default defineComponent({
  name: 'UserForm',
  components: {
    Button,
    Loader2,
    CircularLoading,
    Input,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage
  },
  setup(props, { emit })
  {
    console.debug(props)
    let loading = ref(false)
    const $t: any = inject('$t')
    const formState = ref<UserModel>({
      username: undefined,
      password: undefined,
      confirmPassword: undefined
    })
    const validator = z
        .object({
          username: z.string({ required_error: $t('user.validate.username') })
                     .min(3, $t('user.validate.usernameSize'))
                     .max(20, $t('user.validate.usernameSize')),
          password: z.string({ required_error: $t('user.validate.newPassword') })
                     .min(6, $t('user.validate.passwordSize'))
                     .max(20, $t('user.validate.passwordSize')),
          confirmPassword: z.string({ required_error: $t('user.validate.confirmPassword') })
                            .min(6, $t('user.validate.passwordSize'))
                            .max(20, $t('user.validate.passwordSize'))
        })
        .refine((data) => data.password === data.confirmPassword, {
          message: $t('user.validate.passwordNotMatch'),
          path: ['confirmPassword']
        })
    const formSchema = toTypedSchema(validator)

    const { handleSubmit } = useForm({
      validationSchema: formSchema
    })

    const onSubmit = handleSubmit(() => {
      loading.value = true
      UserService.saveOrUpdate(formState.value)
                 .then((response) => {
                   if (response.status) {
                     emit('close', true)
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
})
</script>