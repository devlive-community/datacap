<template>
  <a-modal v-model:open="visible" :title="$t('source.common.syncMetadata')" :footer="null">
    <a-space direction="vertical" :style="{ width: '100%' }">
      <a-alert type="error" :message="$t('source.tip.syncMetadata1')"/>
      <a-alert type="error" :message="$t('source.tip.syncMetadata2')"/>
      <a-alert :message="$t('source.tip.syncMetadata3').replace('$NAME', String(info?.name))"/>
    </a-space>

    <a-form :model="formState" class="mt-3" @finish="onSubmit">
      <a-form-item name="name"
                   :rules="[
                     { required: true, message: $t('source.validator.name.required') },
                     { validator: validateMatch }
                   ]">
        <a-input v-model:value="formState.name" :placeholder="$t('source.placeholder.name')"/>
      </a-form-item>

      <div class="flex justify-end">
        <a-space>
          <a-button @click="onCancel">{{ $t('common.cancel') }}</a-button>
          <a-button type="primary" danger html-type="submit" :loading="loading">
            {{ $t('source.common.syncMetadata') }}
          </a-button>
        </a-space>
      </div>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { SourceModel } from '@/model/source'
import SourceService from '@/services/source'

defineOptions({ name: 'SourceMetadata' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: SourceModel | null }>(), {
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
const formState = ref<{ name: string }>({ name: '' })

const validateMatch = (_rule: any, value: string) => {
  if (value !== String(props.info?.name)) {
    return Promise.reject(new Error(t('source.validator.name.match').replace('$VALUE', String(props.info?.name))))
  }
  return Promise.resolve(true)
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  if (props.info) {
    loading.value = true
    SourceService.syncMetadata(props.info.code!)
                 .then((response) => {
                   if (response.status) {
                     message.success(t('source.tip.syncMetadata4').replace('$NAME', String(props.info?.name)))
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
