<template>
  <a-modal v-model:open="visible"
           width="80%"
           :title="$t('dataset.common.visual')"
           :footer="null"
           @cancel="onCancel">
    <a-alert v-if="message" type="error" :message="message" class="mb-3"/>

    <a-form :model="formState" layout="vertical" @finish="onSubmit">
      <a-form-item name="name"
                   :label="$t('common.name')"
                   :rules="[
                         { required: true, message: $t('report.validator.name.required') }
                   ]">
        <a-input v-model:value="formState.name" :placeholder="$t('report.placeholder.name')"/>
      </a-form-item>

      <a-form-item name="description" :label="$t('common.description')">
        <a-textarea v-model:value="formState.description" :placeholder="$t('report.placeholder.description')"/>
      </a-form-item>

      <a-form-item>
        <VisualEditor :loading="loading" :configuration="configuration as any" @commitOptions="onCommitOptions"/>
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
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message as antdMessage } from 'ant-design-vue'
import VisualEditor from '@/views/components/visual/VisualEditor.vue'
import { Configuration } from '@/views/components/visual/Configuration.ts'
import { GridConfigure } from '@/views/components/grid/GridConfigure.ts'
import router from '@/router'
import ReportService from '@/services/report'

defineOptions({ name: 'GridVisual' })

const props = withDefaults(defineProps<{ isVisible?: boolean; configure?: GridConfigure }>(), {
  isVisible: false,
  configure: () => null as unknown as GridConfigure
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const configuration = ref<Configuration | null>(null)
const published = ref(false)
const message = ref<string | null>(null)
const formState = ref({
  name: '',
  realtime: true,
  type: 'QUERY',
  configure: null as string | null,
  query: props.configure?.query,
  description: '',
  source: {
    code: props.configure.code
  }
})

if (props.configure) {
  configuration.value = new Configuration()
  configuration.value.headers = props.configure?.headers as never[]
  configuration.value.columns = props.configure?.columns as never[]
}

const onCommitOptions = (value: any) => {
  formState.value.configure = JSON.stringify(value)
}

const validator = () => {
  if (!formState.value.name) {
    message.value = t('report.validator.name').toString()
  }
  else {
    message.value = null
  }
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  validator()
  if (!message.value) {
    published.value = true
    ReportService.saveOrUpdate(formState.value)
                 .then((response) => {
                   if (response.status) {
                     antdMessage.success(t('report.tip.publishSuccess').replace('$VALUE', formState.value.name))
                     router.push('/admin/report')
                   }
                   else {
                     antdMessage.error(response.message)
                   }
                 })
                 .finally(() => (published.value = false))
  }
}
</script>
