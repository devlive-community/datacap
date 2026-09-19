<template>
  <div class="relative">
    <a-spin :spinning="loading.default">
      <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
        <a-form-item name="host"
                     class="w-[40%]"
                     :label="$t('user.common.host')"
                     :extra="$t('user.tip.host')">
          <a-input v-model:value="formState.host"/>
        </a-form-item>

        <a-form-item name="token"
                     class="w-[40%]"
                     :label="$t('user.common.token')"
                     :extra="$t('user.tip.token')">
          <a-input v-model:value="formState.token"/>
        </a-form-item>

        <a-form-item name="timeout"
                     class="w-[40%]"
                     :label="$t('user.common.timeout')"
                     :extra="$t('user.tip.timeout')"
                     :rules="[
                         { pattern: /^[0-9]*$/, message: 'Please enter number!' }
                     ]">
          <a-input v-model:value="formState.timeout"/>
        </a-form-item>

        <a-form-item name="contentCount"
                     class="w-[40%]"
                     :label="$t('user.common.contentCount')"
                     :extra="$t('user.tip.contentCount')"
                     :rules="[
                         { pattern: /^[0-9]*$/, message: 'Please enter number!' }
                     ]">
          <a-input v-model:value="formState.contentCount"/>
        </a-form-item>

        <a-button type="primary" html-type="submit" :loading="loading.submitting" :disabled="loading.submitting">
          {{ $t('common.save') }}
        </a-button>
      </a-form>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import UserService from '@/services/user'
import { UserChatModel } from '@/model/user'
import Common from '@/utils/common'
import { isEmpty } from 'lodash'

defineOptions({ name: 'AssistantForm' })

const { t } = useI18n()

const loading = reactive({
  default: false,
  submitting: false
})
const formState = ref<UserChatModel | null>(null)

const handlerInitialize = () => {
  loading.default = true
  UserService.getInfo()
             .then((response) => {
               if (response.status) {
                 const configure = response.data.chatConfigure
                 if (response.data && configure && !isEmpty(configure)) {
                   formState.value = JSON.parse(configure) as UserChatModel
                 }
                 else {
                   formState.value = { host: undefined, token: undefined, timeout: 60, contentCount: 10 }
                 }
               }
             })
             .finally(() => (loading.default = false))
}

const onSubmit = () => {
  loading.submitting = true
  UserService.changeChart(formState.value as UserChatModel)
             .then((response) => {
               if (response.status) {
                 message.success(t('common.successfully') as string)
                 localStorage.setItem(Common.userEditorConfigure, JSON.stringify(formState.value))
               }
               else {
                 message.error(response.message)
               }
             })
             .finally(() => (loading.submitting = false))
}

handlerInitialize()
</script>
