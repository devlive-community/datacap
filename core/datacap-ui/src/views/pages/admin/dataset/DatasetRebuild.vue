<template>
  <a-modal v-model:open="visible" :title="$t('dataset.common.rebuild') + ' [ ' + data?.name + ' ]'">
    <a-alert :message="$t('dataset.tip.rebuildProgress')"/>

    <template #footer>
      <a-button type="primary" :disabled="loading" :loading="loading" @click="onRebuild">
        {{ $t('dataset.common.rebuild') }}
      </a-button>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import DatasetService from '@/services/dataset'
import { DatasetModel } from '@/model/dataset'

defineOptions({ name: 'DatasetRebuild' })

const props = withDefaults(defineProps<{ isVisible?: boolean; data?: DatasetModel | null }>(), {
  isVisible: false,
  data: null
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

const onRebuild = () => {
  if (props.data) {
    loading.value = true
    DatasetService.rebuild(props.data.code)
                  .then((response) => {
                    if (response.status) {
                      message.success(`${ t('dataset.common.rebuild') } [ ${ props.data?.name } ] ${ t('common.successfully') }`)
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
