<template>
  <a-modal v-model:open="visible"
           width="60%"
           :title="$t('dataset.history.logger')"
           :footer="null"
           :body-style="{ maxHeight: '60vh', overflow: 'auto' }">
    <a-spin :spinning="loading">
      <div class="flex items-center gap-3 pb-2">
        <a-switch v-model:checked="autoRefresh" :disabled="!isRunning" @change="onAutoRefreshChange"/>
        <span class="text-xs">{{ $t('dataset.history.autoRefresh') }}</span>
        <span v-if="isRunning && autoRefresh" class="text-xs text-gray-500">
          ({{ $t('dataset.history.refreshInterval', { seconds: refreshSeconds }) }})
        </span>
      </div>

      <ShadcnLogger height="380"
                    toolbar
                    :items="logs"
                    :custom-patterns="customPatterns"/>
    </a-spin>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import DatasetService from '@/services/dataset'

defineOptions({ name: 'DatasetHistoryLogger' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: any | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const isRunning = computed<boolean>(() => {
  const state = props.info?.state
  return state === 'RUNNING' || state === 'CREATED' || state === 'QUEUE'
})

const loading = ref(false)
const logs = ref<string[]>([])
const autoRefresh = ref(false)
const refreshSeconds = ref(3)
let timer: any = null
const customPatterns = {
  timestamp: [/^(\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2},\d{3})/],
  level: [/\b(INFO|ERROR|WARN|DEBUG)\b/],
  thread: [/\[(pool-\d+-thread-\d+)\]/],
  file: [/\[([^[\]]+\.kt:\d+)\]/, /\[([^[\]]+\.java:\d+)\]/]
}

const stopTimer = () => {
  if (timer) {
    clearTimeout(timer)
    timer = null
  }
}

const scheduleNext = () => {
  stopTimer()
  timer = setTimeout(() => handleInitialize(false), refreshSeconds.value * 1000)
}

const handleInitialize = (showSpin: boolean) => {
  if (!props.info?.id) {
    return
  }
  if (showSpin) {
    loading.value = true
  }
  DatasetService.getHistoryLog(props.info.id)
                .then(response => {
                  if (response.status) {
                    logs.value = response.data || []
                  }
                  else if (showSpin) {
                    message.error(response.message)
                  }
                })
                .finally(() => {
                  loading.value = false
                  if (autoRefresh.value && isRunning.value) {
                    scheduleNext()
                  }
                })
}

const onAutoRefreshChange = (value: boolean) => {
  autoRefresh.value = value
  if (value && isRunning.value) {
    scheduleNext()
  }
  else {
    stopTimer()
  }
}

watch(() => props.isVisible, (value) => {
  if (!value) {
    stopTimer()
  }
})

onBeforeUnmount(() => stopTimer())

// RUNNING 任务默认打开自动刷新
autoRefresh.value = isRunning.value
handleInitialize(true)
</script>
