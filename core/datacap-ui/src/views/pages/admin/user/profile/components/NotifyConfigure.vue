<template>
  <a-modal width="40%"
           :open="modelValue"
           :mask-closable="false"
           :title="$t('user.common.notify')"
           @cancel="onCancel">
    <a-form :model="formState">
      <a-form-item name="dataset" class="space-y-3" :label="$t('common.dataset')">
        <a-checkbox-group v-model:value="formState.services">
          <a-checkbox value="CREATED">{{ $t('notify.text.created') }}</a-checkbox>
          <a-checkbox value="UPDATED">{{ $t('notify.text.updated') }}</a-checkbox>
          <a-checkbox value="DELETED">{{ $t('notify.text.deleted') }}</a-checkbox>
          <a-checkbox value="SYNCDATA">{{ $t('notify.text.syncData') }}</a-checkbox>
        </a-checkbox-group>
      </a-form-item>
    </a-form>

    <template #footer>
      <a-button type="primary" @click="handleSave">
        {{ $t('common.confirm') }}
      </a-button>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

defineOptions({ name: 'NotifyConfigure' })

const props = defineProps<{
  modelValue: boolean
  item: any
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'update:configure', value: any): void
}>()

const formState = reactive({ ...props.item })

const onCancel = () => {
  emit('update:modelValue', false)
}

const handleSave = () => {
  emit('update:configure', formState)
  emit('update:modelValue', false)
}
</script>
