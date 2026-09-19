<template>
  <div class="relative">
    <a-form :model="formState" layout="vertical" @finish="onSubmit">
      <a-form-item name="oldPassword"
                   class="w-[40%]"
                   :extra="$t('user.tip.oldPassword')"
                   :label="$t('user.common.oldPassword')"
                   :rules="[
                     { required: true, message: $t('user.auth.passwordTip') },
                     { min: 6, message: $t('user.auth.passwordSizeTip') },
                     { max: 20, message: $t('user.auth.passwordSizeTip') }
                   ]">
        <a-input-password v-model:value="formState.oldPassword" :placeholder="$t('user.auth.passwordTip')"/>
      </a-form-item>

      <a-form-item name="newPassword"
                   class="w-[40%]"
                   :extra="$t('user.tip.newPassword')"
                   :label="$t('user.common.newPassword')"
                   :rules="[
                     { required: true, message: $t('user.auth.newPasswordTip') },
                     { min: 6, message: $t('user.auth.passwordSizeTip') },
                     { max: 20, message: $t('user.auth.passwordSizeTip') }
                   ]">
        <a-input-password v-model:value="formState.newPassword" :placeholder="$t('user.auth.passwordTip')"/>
      </a-form-item>

      <a-form-item name="confirmPassword"
                   class="w-[40%]"
                   :extra="$t('user.tip.confirmPassword')"
                   :label="$t('user.common.confirmPassword')"
                   :rules="[
                     { required: true, message: $t('user.auth.confirmPasswordTip') },
                     { validator: validatePassword }
                   ]">
        <a-input-password v-model:value="formState.confirmPassword" :placeholder="$t('user.auth.passwordTip')"/>
      </a-form-item>

      <a-button type="primary" html-type="submit" :loading="loading" :disabled="loading">
        {{ $t('common.save') }}
      </a-button>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import UserService from '@/services/user'
import { UserPasswordModel } from '@/model/user'
import Common from '@/utils/common'
import router from '@/router'

defineOptions({ name: 'PasswordForm' })

const { t } = useI18n()

const loading = ref(false)
const formState = ref<{ oldPassword: string | null; newPassword: string | null; confirmPassword: string | null }>({
  oldPassword: null,
  newPassword: null,
  confirmPassword: null
})

const validatePassword = (_rule: any, value: string) => {
  if (value !== formState.value.newPassword) {
    return Promise.reject(new Error(t('user.auth.passwordNotMatchTip')))
  }
  return Promise.resolve(true)
}

const onSubmit = () => {
  loading.value = true
  UserService.changePassword(formState.value as UserPasswordModel)
             .then((response) => {
               if (response.status) {
                 message.success(t('user.tip.changePasswordSuccessfully') as string)
                 localStorage.removeItem(Common.token)
                 localStorage.removeItem(Common.menu)
                 router.push('/auth/signin')
               }
               else {
                 message.error(response.message)
               }
             })
             .finally(() => (loading.value = false))
}
</script>
