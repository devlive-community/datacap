<template>
  <a-modal v-model:open="visible"
           width="40%"
           :mask-closable="false"
           :title="$t('workflow.text.stop')"
           :footer="null"
           @cancel="onCancel">
    <a-space direction="vertical" :style="{ width: '100%' }">
      <a-alert type="error" :message="$t('workflow.tip.stopAlert1')"/>
      <a-alert type="error" :message="$t('workflow.tip.stopAlert2')"/>
      <a-alert :message="$t('workflow.tip.stopAlert3').replace('$VALUE', String(info?.name))"/>
    </a-space>

    <a-form :model="formState" class="mt-3" @finish="onSubmit">
      <a-form-item name="name"
                   :rules="[
                         { required: true, message: $t('workflow.validator.name.required') },
                         { validator: validateMatch }
                   ]">
        <a-input v-model:value="formState.name" :placeholder="$t('workflow.placeholder.name')"/>
      </a-form-item>

      <div class="flex justify-end">
        <a-space>
          <a-button @click="onCancel">
            {{ $t('common.cancel') }}
          </a-button>

          <a-button type="primary" danger html-type="submit" :disabled="loading" :loading="loading">
            {{ $t('workflow.text.stop') }}
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
import WorkflowService from '@/services/workflow'
import { WorkflowModel } from '@/model/workflow'

defineOptions({ name: 'WorkflowStop' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: WorkflowModel | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const formState = ref({ name: '' })

const validateMatch = (_rule: any, value: string) => {
  if (value !== String(props.info?.name)) {
    return Promise.reject(new Error(t('workflow.validator.name.match').replace('$VALUE', String(props.info?.name))))
  }
  return Promise.resolve(true)
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  if (props.info) {
    loading.value = true
    WorkflowService.stop(props.info.code!)
                   .then((response) => {
                     if (response.status) {
                       message.success(t('workflow.tip.stopSuccess').replace('$VALUE', String(props.info?.name)))
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
