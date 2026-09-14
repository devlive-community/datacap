<template>
  <a-modal v-model:open="visible" width="40%" :title="title" :footer="null" @cancel="onCancel">
    <div class="px-4 py-4">
      <a-skeleton v-if="loading" active/>

      <a-form v-else-if="!loading && formState" :model="formState" layout="vertical" @finish="onSubmit">
        <a-form-item name="format" :label="$t('source.common.exportDataFormat')">
          <a-radio-group v-model:value="formState.format">
            <a-radio-button v-for="item in formats" :key="item.name" :value="item.name">{{ item.name }}</a-radio-button>
          </a-radio-group>
        </a-form-item>

        <a-row>
          <a-col :span="12">
            <a-form-item name="count" :label="$t('source.common.exportDataCount')">
              <a-input-number v-model:value="formState.pagination.size" :style="{ width: '100%' }"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item name="path" :label="$t('source.common.downloadPath')">
          <div class="flex items-center space-x-1">
            <a-input v-model:value="formState.path" disabled/>
            <a-button :disabled="!formState.path" @click="onDownload()">
              {{ $t('source.common.downloadFile') }}
            </a-button>
          </div>
        </a-form-item>

        <a-space>
          <a-button @click="onCancel">{{ $t('common.cancel') }}</a-button>

          <a-button type="primary" html-type="submit" :loading="submitting" :disabled="submitting">
            {{ $t('source.common.generateData') }}
          </a-button>
        </a-space>
      </a-form>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import PluginService from '@/services/plugin'
import MetadataService from '@/services/metadata'

defineOptions({ name: 'TableExport' })

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
const formats = ref<any[]>([])
const formState = ref<any>({
  format: 'JsonConvert',
  pagination: {
    page: 1,
    size: 5000
  },
  path: null
})

const onSubmit = () => {
  const code = route.params?.source as string
  const database = route.params?.database as string
  const table = route.params?.table as string

  if (code && database && table) {
    submitting.value = true
    MetadataService.exportData(code, database, table, formState.value)
                   .then((response) => {
                     if (response.status) {
                       formState.value.path = response.data
                     }
                     else {
                       message.error(response.message)
                     }
                   })
                   .finally(() => (submitting.value = false))
  }
}

const onDownload = () => {
  if (formState.value) {
    window.open(formState.value.path, '_target')
  }
}

const onCancel = () => {
  visible.value = false
}

const table = route.params?.table as string
if (table) {
  title.value = t('source.common.exportDataTable').replace('$VALUE', table)

  loading.value = true
  PluginService.getPlugins()
               .then((response) => {
                 if (response.status && response.data) {
                   formats.value = response.data.filter((item: any) => item.type === 'CONVERT')
                 }
               })
               .finally(() => (loading.value = false))
}
</script>
