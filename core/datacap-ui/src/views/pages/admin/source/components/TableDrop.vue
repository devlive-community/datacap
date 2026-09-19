<template>
  <a-modal v-model:open="visible" width="40%" :title="title" :footer="null" @cancel="onCancel">
    <a-space direction="vertical" :style="{ width: '100%' }">
      <a-alert type="error" :message="$t('source.tip.dropTable1')"/>
      <a-alert type="error" :message="$t('source.tip.dropTable2')"/>
      <a-alert type="error" :message="$t('source.tip.dropTable3')"/>
      <a-alert type="error" :message="$t('source.tip.dropTable4')"/>
      <a-alert type="error" :message="$t('source.tip.dropTable5')"/>
    </a-space>

    <div class="relative mt-2">
      <a-spin :spinning="loading">
        <AceEditor v-if="!loading && formState.statement" :value="formState.statement" :read-only="true"/>
      </a-spin>
    </div>

    <template #footer>
      <a-space>
        <a-button @click="onCancel">
          {{ $t('common.cancel') }}
        </a-button>

        <a-button type="primary" danger :loading="submitting" :disabled="submitting" @click="onSubmit(false)">
          {{ $t('source.common.dropTable') }}
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
import MetadataService from '@/services/metadata'
import AceEditor from '@/views/components/editor/AceEditor.vue'

defineOptions({ name: 'TableDrop' })

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
const formState = ref<any>({ preview: false, statement: null })

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
    MetadataService.dropTable(code, database, table, { preview })
                   .then((response) => {
                     if (response.status) {
                       if (preview) {
                         formState.value.statement = response.data.content
                       }
                       else {
                         message.success(t('source.tip.dropTableSuccess').replace('$VALUE', table))
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
  title.value = t('source.common.dropTableInfo').replace('$VALUE', table)
  onSubmit(true)
}
</script>
