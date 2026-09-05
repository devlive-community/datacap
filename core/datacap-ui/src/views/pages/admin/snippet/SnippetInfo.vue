<template>
  <a-drawer v-model:open="visible" :title="title" width="40%">
    <a-spin :spinning="loading">
      <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
        <a-form-item name="name"
                     :label="$t('common.name')"
                     :rules="[{ required: true, message: $t('common.name') }]">
          <a-input v-model:value="formState.name"/>
        </a-form-item>

        <a-form-item name="description" :label="$t('common.description')">
          <a-textarea v-model:value="formState.description"/>
        </a-form-item>

        <a-form-item name="content" :label="$t('common.content')">
          <AceEditor :value="formState.context" @update:value="formState.context = $event"/>
        </a-form-item>

        <div class="flex justify-end">
          <a-space>
            <a-button danger @click="onCancel()">
              {{ $t('common.cancel') }}
            </a-button>
            <a-button type="primary" html-type="submit" :loading="loading" :disabled="loading">
              {{ $t('common.save') }}
            </a-button>
          </a-space>
        </div>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { cloneDeep } from 'lodash'
import { SnippetModel, SnippetRequest } from '@/model/snippet'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import SnippetService from '@/services/snippet'

defineOptions({ name: 'SnippetInfo' })

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
const formState = ref<SnippetModel>(null as unknown as SnippetModel)
const title = ref<string | null>(null)

const handleInitialize = () => {
  title.value = t('snippet.common.create')
  if (props.info) {
    formState.value = cloneDeep(props.info)
    if (props.info.id) {
      title.value = `${ t('snippet.common.modify').replace('$VALUE', props.info.name as string) }`
    }
  }
  else {
    formState.value = SnippetRequest.of()
  }
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  loading.value = true
  SnippetService.saveOrUpdate(formState.value)
                .then((response) => {
                  if (response.status) {
                    message.success(t('snippet.tip.createSuccess').replace('$VALUE', formState.value.name as string))
                    onCancel()
                  }
                })
                .finally(() => (loading.value = false))
}

handleInitialize()
</script>
