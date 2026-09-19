<template>
  <a-modal v-model:open="visible" :title="`[ ${ info?.name } ] ${ $t('dataset.common.clearData') }`">
    <a-alert :message="$t('dataset.tip.clearData')"/>

    <a-row class="mt-2.5" :gutter="8">
      <a-col :span="12">
        <a-card :title="$t('dataset.common.totalRows')">
          <div class="mt-3 flex items-center p-4 text-center">{{ info?.totalRows }}</div>
        </a-card>
      </a-col>

      <a-col :span="12">
        <a-card :title="$t('dataset.common.totalSize')">
          <div class="mt-3 flex items-center p-4 text-center">{{ info?.totalSize }}</div>
        </a-card>
      </a-col>
    </a-row>

    <template #footer>
      <a-space>
        <a-button @click="onCancel">{{ $t('common.cancel') }}</a-button>
        <a-button type="primary" danger :disabled="loading" :loading="loading" @click="onSubmit">
          {{ $t('dataset.common.clearData') }}
        </a-button>
      </a-space>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import DatasetService from '@/services/dataset'
import { DatasetModel } from '@/model/dataset'

defineOptions({ name: 'DatasetClear' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: DatasetModel | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  if (props.info) {
    loading.value = true
    DatasetService.clearData(props.info.code)
                  .then((response: { status: boolean }) => {
                    if (response.status) {
                      message.success(`${ t('dataset.common.clearData') } [ ${ props.info?.name } ] ${ t('common.successfully') }`)
                      onCancel()
                    }
                    else {
                      message.error(`${ t('dataset.common.clearData') } [ ${ props.info?.name } ] ${ t('common.fail') }`)
                    }
                  })
                  .finally(() => (loading.value = false))
  }
}
</script>
