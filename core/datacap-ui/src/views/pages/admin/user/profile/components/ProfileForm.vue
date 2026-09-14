<template>
  <div class="relative">
    <a-spin :spinning="loading">
      <a-form v-if="formState" :model="formState" layout="vertical">
        <a-form-item name="avatar"
                     class="w-[40%]"
                     :label="$t('user.common.avatar')"
                     :extra="$t('user.tip.avatar')">
          <CropperHome :pic="formState.avatarConfigure?.path" @update:value="onCropper"/>
        </a-form-item>

        <a-form-item name="username"
                     class="w-[40%]"
                     :label="$t('user.common.username')"
                     :extra="$t('user.tip.username')">
          <a-input v-model:value="formState.username" disabled/>
        </a-form-item>

        <a-form-item name="createTime"
                     class="w-[40%]"
                     :label="$t('user.common.createTime')"
                     :extra="$t('user.tip.createTime')">
          <a-input v-model:value="formState.createTime" disabled/>
        </a-form-item>
      </a-form>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import UserService from '@/services/user'
import { UserModel } from '@/model/user'
import CropperHome from '@/views/components/cropper/CropperHome.vue'

defineOptions({ name: 'ProfileForm' })

const { t } = useI18n()

const loading = ref(false)
const formState = ref<UserModel | null>(null)

const handlerInitialize = () => {
  loading.value = true
  UserService.getInfo()
             .then(response => {
               if (response.status) {
                 formState.value = response.data
               }
             })
             .finally(() => (loading.value = false))
}

const onCropper = (value: any) => {
  const configure = {
    mode: 'AVATAR',
    file: value
  }
  UserService.uploadAvatar(configure)
             .then(response => {
               if (response.status) {
                 if (formState.value) {
                   formState.value.avatar = response.data
                 }
                 message.success(t('common.successfully'))
               }
               else {
                 message.error(response.message)
               }
             })
}

handlerInitialize()
</script>
