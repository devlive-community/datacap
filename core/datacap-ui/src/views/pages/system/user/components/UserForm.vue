<template>
  <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit" @finishFailed="onError">
    <a-form-item name="username"
                 :label="$t('user.common.username')"
                 :rules="[
                   { required: true, message: $t('user.auth.usernameTip') },
                   { min: 3, message: $t('user.auth.usernameSizeTip') },
                   { max: 20, message: $t('user.auth.usernameSizeTip') }
               ]">
      <a-input v-model:value="formState.username"/>
    </a-form-item>

    <a-form-item name="password"
                 :label="$t('user.common.password')"
                 :rules="[
                   { required: true, message: $t('user.auth.passwordTip') },
                   { min: 6, message: $t('user.auth.passwordSizeTip') },
                   { max: 20, message: $t('user.auth.passwordSizeTip') }
               ]">
      <a-input-password v-model:value="formState.password"
                        :placeholder="$t('user.auth.passwordTip')"/>
    </a-form-item>

    <a-form-item name="confirmPassword"
                 :label="$t('user.common.confirmPassword')"
                 :rules="[
                   { required: true, message: $t('user.auth.passwordTip') },
                   { validator: validatePassword }
               ]">
      <a-input-password v-model:value="formState.confirmPassword"
                        :placeholder="$t('user.auth.confirmPasswordTip')"/>
    </a-form-item>

    <div class="flex justify-end">
      <a-button type="primary" html-type="submit" :loading="loading" :disabled="loading">
        {{ $t('common.save') }}
      </a-button>
    </div>
  </a-form>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { UserModel } from '@/model/user'
import UserService from '@/services/user'

defineOptions({ name: 'UserForm' })

const props = defineProps<{ info?: UserModel }>()
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()

const loading = ref(false)
const formState = ref<UserModel>({
  code: undefined,
  username: undefined,
  password: undefined,
  confirmPassword: undefined
} as UserModel)

if (props.info) {
  const { code, username } = props.info as UserModel
  formState.value = { code, username } as UserModel
}

const validatePassword = (_rule: any, value: string) => {
  if (value !== formState.value.password) {
    return Promise.reject(new Error(t('user.auth.passwordNotMatchTip')))
  }
  return Promise.resolve(true)
}

const onError = (error: any) => {
  const names = (error?.errorFields || []).map((field: any) => (Array.isArray(field.name) ? field.name.join('.') : field.name))
  message.error(`Validation error field: [ ${ names.join(', ') } ]`)
}

const onSubmit = () => {
  loading.value = true
  UserService.saveOrUpdate(formState.value)
             .then((response) => {
               if (response.status) {
                 emit('close', true)
               }
               else {
                 message.error(response.message)
               }
             })
             .finally(() => (loading.value = false))
}
</script>
