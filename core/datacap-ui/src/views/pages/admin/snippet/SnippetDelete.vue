<template>
  <a-modal v-model:open="visible" :title="title" :footer="null">
    <a-space direction="vertical" :style="{ width: '100%' }">
      <a-alert type="error" :message="$t('snippet.tip.deleteAlert1')"/>
      <a-alert type="error" :message="$t('snippet.tip.deleteAlert2')"/>
      <a-alert type="error" :message="$t('snippet.tip.deleteAlert3')"/>
      <a-alert :message="$t('snippet.tip.deleteAlert4').replace('$VALUE', info?.name as string)"/>
    </a-space>

    <a-form :model="formState" class="mt-3" @finish="onSubmit" @finishFailed="(e: any) => console.log(e)">
      <a-form-item name="name"
                   :rules="[
                     { required: true, message: $t('snippet.validator.name.required') },
                     { validator: validateMatch }
                   ]">
        <a-input v-model:value="formState.name" :placeholder="$t('snippet.placeholder.name')"/>
      </a-form-item>

      <div class="flex justify-end">
        <a-space>
          <a-button @click="onCancel">
            {{ $t('common.cancel') }}
          </a-button>
          <a-button type="primary" danger html-type="submit" :loading="loading">
            {{ $t('snippet.common.delete') }}
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
import { SnippetModel } from '@/model/snippet'
import SnippetService from '@/services/snippet'

defineOptions({ name: 'SnippetDelete' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: SnippetModel | null }>(), {
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
const title = ref<string | null>(null)
const formState = ref<{ name: string }>({ name: '' })

if (props.info) {
  title.value = t('snippet.common.deleteInfo').replace('$VALUE', props.info.name as string)
}

const validateMatch = (_rule: any, value: string) => {
  if (value !== String(props.info?.name)) {
    return Promise.reject(new Error(t('snippet.validator.name.match').replace('$VALUE', String(props.info?.name))))
  }
  return Promise.resolve(true)
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  if (props.info) {
    loading.value = true
    SnippetService.deleteByCode(props.info.code!)
                  .then(response => {
                    if (response.status) {
                      message.success(t('snippet.tip.deleteSuccess').replace('$VALUE', props.info?.name as string))
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
