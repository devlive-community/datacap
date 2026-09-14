<template>
  <a-modal v-model:open="visible" :title="title" width="60%" :footer="null" @cancel="onCancel">
    <a-space direction="vertical" :style="{ width: '100%' }">
      <a-alert type="error" :message="$t('source.tip.truncateTable1')"/>
      <a-alert type="error" :message="$t('source.tip.truncateTable2')"/>
      <a-alert type="error" :message="$t('source.tip.truncateTable3')"/>
      <a-alert type="error" :message="$t('source.tip.truncateTable4')"/>
      <a-alert type="error" :message="$t('source.tip.truncateTable5')"/>
    </a-space>

    <div class="relative">
      <a-spin :spinning="loading">
        <AceEditor v-if="!loading && formState.statement"
                   class="mt-2"
                   :value="formState.statement"
                   :read-only="true"/>
      </a-spin>
    </div>

    <template #footer>
      <a-space>
        <a-button @click="onCancel">
          {{ $t('common.cancel') }}
        </a-button>

        <a-button type="primary" danger :loading="submitting" :disabled="submitting" @click="onSubmit(false)">
          {{ $t('source.common.truncateTable') }}
        </a-button>
      </a-space>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import MetadataService from '@/services/metadata.ts'

defineOptions({ name: 'TableTruncate' })

const props = withDefaults(defineProps<{ isVisible?: boolean }>(), { isVisible: false })
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const route = useRoute()
const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const submitting = ref(false)
const title = ref<string | null>(null)
const formState = ref<any>({ statement: null })

const onCancel = () => {
  visible.value = false
}

const onSubmit = (preview: boolean) => {
  const code = route.params?.source as string
  const database = route.params?.database as string
  const table = route.params?.table as string

  if (code && database && table) {
    if (preview) {
      loading.value = true
    }
    else {
      submitting.value = true
    }

    MetadataService.truncateTable(code, database, table, { preview })
                   .then((response) => {
                     if (response.status) {
                       if (preview) {
                         formState.value.statement = response.data.content
                       }
                       else {
                         message.success(t('source.tip.truncateTableSuccess').replace('$VALUE', table))
                         onCancel()
                       }
                     }
                     else {
                       message.error(response.message)
                     }
                   })
                   .finally(() => {
                     if (preview) {
                       loading.value = false
                     }
                     else {
                       submitting.value = false
                     }
                   })
  }
}

const table = route.params?.table as string
if (table) {
  title.value = t('source.common.truncateTable').replace('$VALUE', table)
  onSubmit(true)
}
</script>
