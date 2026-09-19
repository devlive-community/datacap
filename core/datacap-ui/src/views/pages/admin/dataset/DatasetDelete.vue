<template>
  <a-modal v-model:open="visible" :title="title" :footer="null">
    <a-space direction="vertical" :style="{ width: '100%' }">
      <a-alert type="error" :message="$t('dataset.tip.deleteAlert1')"/>
      <a-alert type="error" :message="$t('dataset.tip.deleteAlert2')"/>
      <a-alert type="info" :message="$t('dataset.tip.deleteAlert3').replace('$VALUE', String(info?.name))"/>
    </a-space>

    <a-form :model="formState" class="mt-3" @finish="onSubmit">
      <a-form-item name="name"
                   :rules="[
                     { required: true, message: $t('dataset.validator.name.required') },
                     { validator: validateMatch }
                   ]">
        <a-input v-model:value="formState.name" :placeholder="$t('dataset.placeholder.name')"/>
      </a-form-item>

      <div class="flex justify-end">
        <a-space>
          <a-button @click="onCancel">
            {{ $t('common.cancel') }}
          </a-button>
          <a-button type="primary" danger html-type="submit" :loading="loading" :disabled="loading">
            {{ title }}
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
import { DatasetModel } from '@/model/dataset.ts'
import DatasetService from '@/services/dataset.ts'

defineOptions({ name: 'DatasetDelete' })

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

const title = ref<string | null>(null)
const loading = ref(false)
const formState = ref<{ name: string }>({ name: '' })

if (props.info) {
  title.value = `${ t('dataset.common.deleteInfo').replace('$VALUE', String(props.info.name)) }`
}

const validateMatch = (_rule: any, value: string) => {
  if (value !== String(props.info?.name)) {
    return Promise.reject(new Error(t('dataset.validator.name.match').replace('$VALUE', String(props.info?.name))))
  }
  return Promise.resolve(true)
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  if (props.info) {
    loading.value = true
    DatasetService.deleteByCode(props.info.code!)
                  .then((response) => {
                    if (response.status) {
                      message.success(t('dataset.tip.deleteSuccess').replace('$VALUE', String(props.info?.name)))
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
