<template>
  <a-modal v-model:open="visible"
           width="60%"
           :title="$t('workflow.text.configure')"
           :footer="null"
           @cancel="onCancel">
    <a-spin :spinning="loading">
      <ShadcnWorkflowView v-if="!loading && configuration"
                          :nodes="configuration.nodes"
                          :canvas="{ height: 500 }"
                          :data="data"/>
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
import ConfigurationService from '@/services/configure.ts'
import WorkflowService from '@/services/workflow.ts'
import HttpUtils from '@/utils/http.ts'

export interface Configuration
{
  categories: any[]
  nodes: any[]
}

defineOptions({ name: 'WorkflowFlow' })

const props = withDefaults(defineProps<{ isVisible?: boolean; code?: string }>(), { isVisible: false })
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const configuration = ref<Configuration | null>(null)
const data = ref<any>(null)

const onCancel = () => {
  visible.value = false
}

HttpUtils.all([ConfigurationService.getExecutor(), WorkflowService.getByCode(props.code)])
         .then(HttpUtils.spread((...responses: any[]) => {
           const [executor, workflow] = responses

           if (executor.status && executor.data) {
             configuration.value = executor.data
           }

           if (workflow.status && workflow.data) {
             data.value = workflow.data.configure
           }
           else {
             message.error(workflow.message)
           }
         }))
         .catch((error: any) => {
           message.error(error.message || 'Failed to initialize workflow')
         })
         .finally(() => {
           loading.value = false
         })
</script>
