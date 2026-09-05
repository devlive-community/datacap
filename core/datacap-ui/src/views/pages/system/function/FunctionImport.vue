<template>
  <a-drawer v-model:open="visible" :title="$t('function.common.import')" width="40%">
    <a-form v-if="formState" :model="formState" layout="vertical">
      <a-tabs v-model:activeKey="formState.mode">
        <a-tab-pane :key="FunctionMode.txt" :tab="$t('common.content')">
          <a-form-item name="content" :label="$t('common.content')">
            <a-textarea v-model:value="formState.content" :placeholder="$t('common.content')"/>
          </a-form-item>
        </a-tab-pane>

        <a-tab-pane :key="FunctionMode.url" :tab="$t('function.common.importFromUrl')">
          <a-space direction="vertical" :style="{ width: '100%' }">
            <a-alert type="warning"
                     show-icon
                     :message="$t('function.common.keyword')"
                     description="(http|https)://datacap.devlive.org/resources/functions/plugin/keywords.txt"/>
            <a-alert type="warning"
                     show-icon
                     :message="$t('function.common.operator')"
                     description="(http|https)://datacap.devlive.org/resources/functions/plugin/operators.txt"/>
            <a-alert type="warning"
                     show-icon
                     :message="$t('function.common.function')"
                     description="(http|https)://datacap.devlive.org/resources/functions/plugin/functions.txt"/>
          </a-space>

          <a-form-item name="url" :label="$t('common.url')" class="mt-2">
            <a-input v-model:value="formState.content"/>
          </a-form-item>
        </a-tab-pane>
      </a-tabs>

      <a-form-item name="plugin"
                   :label="$t('common.plugin')"
                   :rules="[{ required: true, message: $t('common.plugin') }]">
        <a-select v-model:value="formState.plugin" mode="multiple" :options="plugins"/>
      </a-form-item>

      <a-form-item name="type"
                   :label="$t('common.type')"
                   :rules="[{ required: true, message: $t('common.type') }]">
        <a-select v-model:value="formState.type"
                  :placeholder="$t('function.tip.selectTypeHolder')"
                  :options="types"/>
      </a-form-item>
    </a-form>

    <div class="flex justify-end mt-2">
      <a-space>
        <a-button danger :loading="loading" :disabled="loading" @click="onCancel()">
          {{ $t('common.cancel') }}
        </a-button>
        <a-button type="primary" :loading="loading" :disabled="loading" @click="onSubmit()">
          {{ $t('function.common.import') }}
        </a-button>
      </a-space>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { message } from 'ant-design-vue'
import { useHeaders } from '@/views/pages/system/function/FunctionUtils'
import { FunctionImportModel, FunctionMode } from '@/model/function'
import FunctionService from '@/services/function'
import SourceService from '@/services/source'

defineOptions({ name: 'FunctionImport' })

const props = withDefaults(defineProps<{ isVisible?: boolean }>(), {
  isVisible: false
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { typeHeaders: types } = useHeaders()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const formState = ref<FunctionImportModel>(null as unknown as FunctionImportModel)
const loading = ref(false)
const plugins = ref<any[]>([])

const handlerInitialize = () => {
  formState.value = {
    mode: FunctionMode.txt,
    plugin: undefined,
    content: undefined,
    type: undefined
  } as unknown as FunctionImportModel

  SourceService.getPlugins()
               .then(response => {
                 if (response.status) {
                   plugins.value = Array.from(
                       new Set(
                           (Object.values(response.data)
                                  .reduce((acc: any, curr) => acc.concat(curr), []) as any[])
                               .map((value: { name: string }) => ({ label: value.name, value: value.name }))
                       )
                   )
                 }
               })
}

const onSubmit = () => {
  loading.value = true
  FunctionService.import(formState.value)
                 .then((response) => {
                   if (response.status) {
                     message.success('Create successfully')
                     onCancel()
                   }
                   else {
                     message.error(response.message)
                   }
                 })
                 .finally(() => (loading.value = false))
}

const onCancel = () => {
  emit('close', false)
}

handlerInitialize()
</script>
