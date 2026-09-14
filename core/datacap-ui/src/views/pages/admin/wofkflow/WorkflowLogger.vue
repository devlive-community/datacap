<template>
  <a-modal v-model:open="visible"
           width="60%"
           :title="$t('workflow.text.logger')"
           :footer="null"
           @cancel="onCancel">
    <a-spin :spinning="loading">
      <ShadcnLogger v-if="!loading"
                    height="380"
                    toolbar
                    :items="logs"
                    :custom-patterns="customPatterns"/>
    </a-spin>

    <template #footer>
      <a-button @click="onCancel">
        {{ $t('common.cancel') }}
      </a-button>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { message } from 'ant-design-vue'
import { WorkflowModel } from '@/model/workflow'
import WorkflowService from '@/services/workflow'

defineOptions({ name: 'WorkflowLogger' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: WorkflowModel | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const logs = ref<string[]>([])
const customPatterns = {
  timestamp: [/^(\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2},\d{3})/],
  level: [/\b(INFO|ERROR|WARN|DEBUG)\b/],
  thread: [/\[(pool-\d+-thread-\d+)\]/],
  file: [/\[([^[\]]+\.java:\d+)\]/]
}

const onCancel = () => {
  visible.value = false
}

const handleInitialize = () => {
  if (props.info) {
    loading.value = true
    WorkflowService.getLogger(props.info.code)
                   .then((response) => {
                     if (response.status) {
                       logs.value = response.data
                     }
                     else {
                       message.error(response.message)
                     }
                   })
                   .finally(() => (loading.value = false))
  }
}

handleInitialize()
</script>
