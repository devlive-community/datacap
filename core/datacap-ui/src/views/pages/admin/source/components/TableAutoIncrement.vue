<template>
  <a-modal v-model:open="localVisible"
           :title="$t('source.common.resetAutoIncrement')"
           :mask-closable="false"
           :footer="null"
           @cancel="onCancel">
    <a-form :model="formState" layout="vertical" @finish="onSubmit">
      <a-form-item name="autoIncrement" :label="$t('source.common.resetTo')">
        <a-input-number v-model:value="formState.autoIncrement"
                        :min="1"
                        :style="{ width: '100%' }"
                        :placeholder="$t('snippet.placeholder.name')"/>
      </a-form-item>

      <div class="flex justify-end">
        <a-space>
          <a-button @click="onCancel">
            {{ $t('common.cancel') }}
          </a-button>

          <a-button type="primary" html-type="submit" :loading="loading">
            {{ $t('common.apply') }}
          </a-button>
        </a-space>
      </div>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import MetadataService from '@/services/metadata.ts'

const emit = defineEmits(['close'])
const route = useRoute()
const { t } = useI18n()

const props = withDefaults(defineProps<{
  visible: boolean
  info?: any
}>(), {
  visible: false
})

const localVisible = ref(props.visible)
const loading = ref(false)
const formState = ref({ autoIncrement: props.info?.object_auto_increment ?? 1 })

const onCancel = () => emit('close')

const onSubmit = () => {
  const code = route?.params.source as string
  const database = route?.params.database as string
  const table = route?.params.table as string

  if (props.info && code && database && table) {
    loading.value = true
    const configure = {
      autoIncrement: formState.value.autoIncrement
    }
    MetadataService.updateAutoIncrement(code, database, table, configure)
                   .then((response) => {
                     if (response.status && response.data && response.data.isSuccessful) {
                       message.success(t('source.tip.resetAutoIncrementSuccess').replace('$VALUE', String(formState.value.autoIncrement)))
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
