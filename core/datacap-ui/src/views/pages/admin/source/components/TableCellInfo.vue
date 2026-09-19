<template>
  <a-modal v-model:open="visible"
           width="40%"
           :title="$t('source.common.previewDML')"
           :footer="null"
           @cancel="onCancel">
    <div class="relative h-full">
      <a-spin :spinning="loading">
        <AceEditor v-if="!loading && contentDML" :value="contentDML" :read-only="true"/>
      </a-spin>
    </div>

    <template #footer>
      <a-space>
        <a-button @click="onCancel">
          {{ $t('common.cancel') }}
        </a-button>

        <a-button type="primary" :disabled="submitting" :loading="submitting" @click="onSubmit">
          {{ $t('common.submit') }}
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

defineOptions({ name: 'TableCellInfo' })

const props = withDefaults(defineProps<{ isVisible?: boolean; columns?: any[]; isUpdate?: boolean }>(), {
  isVisible: false,
  isUpdate: false
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const route = useRoute()
const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const submitting = ref(false)
const contentDML = ref<string | null>(null)

const onChange = (preview: boolean) => {
  const code = route?.params.source as string
  const database = route?.params.database as string
  const table = route?.params.table as string

  if (code && database && table) {
    if (preview) {
      loading.value = true
    }
    else {
      submitting.value = true
    }

    const configure = {
      preview: preview,
      rows: props.columns
    }

    const apiCall = props.isUpdate
        ? MetadataService.updateData(code, database, table, configure)
        : MetadataService.insertData(code, database, table, configure)

    apiCall.then((response) => {
      if (response.status && response.data && response.data.isSuccessful) {
        if (preview) {
          contentDML.value = response.data.content
        }
        else {
          message.success(t('source.tip.updateSuccess'))
          onCancel()
        }
      }
      else {
        message.error(response.data.message)
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

const onSubmit = () => onChange(false)

const onCancel = () => {
  visible.value = false
}

onChange(true)
</script>
