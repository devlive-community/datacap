<template>
  <a-modal v-model:open="visible" :title="$t('common.chat')" :footer="null">
    <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
      <a-form-item name="name"
                   :label="$t('common.name')"
                   :rules="[ { required: true, message: $t('common.name') } ]">
        <a-input v-model:value="formState.name"/>
      </a-form-item>

      <a-form-item name="avatar" :label="$t('common.avatar')">
        <a-input v-model:value="formState.avatar"/>
      </a-form-item>

      <a-form-item name="description" :label="$t('common.description')">
        <a-textarea v-model:value="formState.description"/>
      </a-form-item>

      <div class="flex justify-end">
        <a-space>
          <a-button @click="onCancel">
            {{ $t('common.cancel') }}
          </a-button>

          <a-button type="primary" html-type="submit" :disabled="loading" :loading="loading">
            {{ $t('common.save') }}
          </a-button>
        </a-space>
      </div>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import ChatService from '@/services/chat.ts'
import { ChatModel, ChatRequest } from '@/model/chat.ts'

defineOptions({ name: 'ChatInfo' })

const props = withDefaults(defineProps<{ isVisible?: boolean }>(), { isVisible: false })
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const formState = ref<ChatModel>(ChatRequest.of())

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  if (formState.value) {
    loading.value = true
    ChatService.saveOrUpdate(formState.value)
               .then((response) => {
                 if (response.status) {
                   message.success(t('common.success'))
                   onCancel()
                 }
                 else {
                   message.error(response.message)
                 }
               })
               .finally(() => (loading.value = false))
  }
}
</script>
