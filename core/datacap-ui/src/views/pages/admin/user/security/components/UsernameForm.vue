<template>
  <div class="relative">
    <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
      <a-form-item name="oldUsername"
                   class="w-[40%]"
                   :extra="$t('user.tip.oldUsername')"
                   :label="$t('user.common.oldUsername')">
        <a-input v-model:value="formState.oldUsername" disabled :placeholder="$t('user.auth.oldUsername')"/>
      </a-form-item>

      <a-form-item name="newUsername"
                   class="w-[40%]"
                   :extra="$t('user.tip.newUsername')"
                   :label="$t('user.common.newUsername')"
                   :rules="[
                     { required: true, message: $t('user.auth.usernameTip') },
                     { min: 3, message: $t('user.auth.usernameSizeTip') },
                     { max: 20, message: $t('user.auth.usernameSizeTip') }
                   ]">
        <a-input v-model:value="formState.newUsername" :placeholder="$t('user.auth.usernameTip')"/>
      </a-form-item>

      <a-form-item name="password"
                   class="w-[40%]"
                   :label="$t('user.common.password')"
                   :extra="$t('user.tip.password')"
                   :rules="[
                     { required: true, message: $t('user.auth.passwordTip') },
                     { min: 6, message: $t('user.auth.passwordSizeTip') },
                     { max: 20, message: $t('user.auth.passwordSizeTip') }
                   ]">
        <a-input-password v-model:value="formState.password" :placeholder="$t('user.auth.passwordTip')"/>
      </a-form-item>

      <a-button type="primary" html-type="submit" :loading="submitting" :disabled="submitting">
        {{ $t('common.save') }}
      </a-button>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import UserService from '@/services/user'
import { UsernameModel } from '@/model/user'
import Common from '@/utils/common'

defineOptions({ name: 'UsernameForm' })

const { t } = useI18n()
const router = useRouter()

const defaultLoading = ref(false)
const submitting = ref(false)
const formState = ref<UsernameModel | null>(null)

const handlerInitialize = () => {
  defaultLoading.value = true
  UserService.getInfo()
             .then(response => {
               if (response.status && response.data) {
                 formState.value = { oldUsername: response.data.username, newUsername: undefined, password: undefined } as unknown as UsernameModel
               }
             })
             .finally(() => (defaultLoading.value = false))
}

const onSubmit = () => {
  submitting.value = true
  UserService.changeUsername(formState.value as UsernameModel)
             .then((response) => {
               if (response.status) {
                 message.success(t('user.tip.changeUsernameSuccessfully') as string)
                 localStorage.removeItem(Common.token)
                 localStorage.removeItem(Common.menu)
                 router.push('/auth/signin')
               }
               else {
                 message.error(response.message)
               }
             })
             .finally(() => (submitting.value = false))
}

handlerInitialize()
</script>
