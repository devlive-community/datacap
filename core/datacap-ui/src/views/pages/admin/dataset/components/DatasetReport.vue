<template>
  <a-modal v-model:open="localVisible" :title="$t('common.configure')" :footer="null" @cancel="onCancel">
    <a-form :model="formState" layout="vertical" @finish="onSubmit" @finishFailed="(e: any) => console.log(e)">
      <a-form-item name="name"
                   :label="$t('common.name')"
                   :rules="[{ required: true, message: $t('common.name') }]">
        <a-input v-model:value="formState.name"/>
      </a-form-item>

      <a-form-item name="description" :label="$t('common.description')">
        <a-textarea v-model:value="formState.description"/>
      </a-form-item>

      <a-form-item name="build" :label="$t('dataset.common.continuousBuild')">
        <a-switch v-model:checked="formState.build"/>
      </a-form-item>

      <div class="flex justify-end">
        <a-space>
          <a-button @click="onCancel">
            {{ $t('common.cancel') }}
          </a-button>
          <a-button type="primary" html-type="submit" :disabled="published" :loading="published">
            {{ $t('common.publish') }}
          </a-button>
        </a-space>
      </div>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import router from '@/router'
import ReportService from '@/services/report.ts'

defineOptions({ name: 'DatasetReport' })

const props = defineProps<{
  visible: boolean
  info?: { name: string; description: string; build: boolean }
  code?: string
  dimension?: any
  commitOptions?: any
  configure?: any
}>()

const emit = defineEmits(['close'])

const { t } = useI18n()

const localVisible = ref(props.visible)
const published = ref(false)
const formState = ref<{ name?: string; description?: string; build: boolean }>(
  props.info || { name: undefined, description: undefined, build: false }
)

const onCancel = () => {
  emit('close', !props.visible)
}

const onSubmit = () => {
  published.value = true
  const obj = props.dimension
  const configure = {
    name: formState.value.name,
    code: props.code,
    realtime: true,
    type: 'DATASET',
    configure: JSON.stringify(props.commitOptions),
    dataset: {
      code: obj.dataset.code
    },
    query: JSON.stringify(props.configure),
    description: formState.value.description
  }
  ReportService.saveOrUpdate(configure)
               .then(response => {
                 if (response.status) {
                   message.success(t('report.tip.publishSuccess').replace('$VALUE', String(formState.value.name)))
                   if (formState.value.build) {
                     router.push('/admin/report')
                   }
                   onCancel()
                 }
               })
               .finally(() => (published.value = false))
}
</script>
