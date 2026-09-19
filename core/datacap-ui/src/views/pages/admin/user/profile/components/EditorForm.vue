<template>
  <div class="relative">
    <a-spin :spinning="defaultLoading">
      <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
        <a-form-item name="fontSize"
                     class="w-[40%]"
                     :label="$t('user.common.fontSize')"
                     :extra="$t('user.tip.fontSize')"
                     :rules="[{ pattern: /^[0-9]*$/, message: 'Please enter number!' }]">
          <a-input v-model:value="formState.fontSize"/>
        </a-form-item>

        <a-button type="primary" html-type="submit" :loading="submitting" :disabled="submitting">
          {{ $t('common.save') }}
        </a-button>
      </a-form>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import UserService from '@/services/user'
import { UserEditor } from '@/model/user'
import Common from '@/utils/common'

defineOptions({ name: 'EditorForm' })

const { t } = useI18n()

const defaultLoading = ref(false)
const submitting = ref(false)
const formState = ref<UserEditor | null>(null)

const handlerInitialize = () => {
  defaultLoading.value = true
  UserService.getInfo()
             .then(response => {
               if (response.status) {
                 const configure = response.data.editorConfigure
                 if (response.data && configure) {
                   formState.value = configure as UserEditor
                 }
               }
             })
             .finally(() => (defaultLoading.value = false))
}

const onSubmit = () => {
  submitting.value = true
  UserService.changeEditor(formState.value as UserEditor)
             .then((response) => {
               if (response.status) {
                 message.success(t('common.successfully') as string)
                 localStorage.setItem(Common.userEditorConfigure, JSON.stringify(formState.value))
               }
               else {
                 message.error(response.message)
               }
             })
             .finally(() => (submitting.value = false))
}

handlerInitialize()
</script>
