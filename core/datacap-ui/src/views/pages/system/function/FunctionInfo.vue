<template>
  <a-drawer v-model:open="visible" :title="title" width="40%">
    <a-spin :spinning="loading">
      <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
        <a-form-item name="name"
                     :label="$t('common.name')"
                     :rules="[{ required: true, message: $t('common.name') }]">
          <a-input v-model:value="formState.name"/>
        </a-form-item>

        <a-form-item name="plugin"
                     :label="$t('common.plugin')"
                     :rules="[{ required: true, message: $t('common.plugin') }]">
          <a-select v-model:value="formState.plugin" mode="multiple" :options="plugins"/>
        </a-form-item>

        <a-form-item name="content"
                     :label="$t('common.content')"
                     :rules="[{ required: true, message: $t('common.content') }]">
          <a-textarea v-model:value="formState.content"/>
        </a-form-item>

        <a-form-item name="description"
                     :label="$t('common.description')"
                     :rules="[{ required: true, message: $t('common.description') }]">
          <a-textarea v-model:value="formState.description"/>
        </a-form-item>

        <a-form-item name="type"
                     :label="$t('common.type')"
                     :rules="[{ required: true, message: $t('common.type') }]">
          <a-select v-model:value="formState.type"
                    :placeholder="$t('function.tip.selectTypeHolder')"
                    :options="types"/>
        </a-form-item>

        <div class="flex justify-end">
          <a-button type="primary" html-type="submit" :loading="saving" :disabled="saving">
            {{ $t('common.save') }}
          </a-button>
        </div>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { cloneDeep, omit } from 'lodash'
import { useHeaders } from '@/views/pages/system/function/FunctionUtils'
import { FunctionModel } from '@/model/function'
import FunctionService from '@/services/function'
import PluginService from '@/services/plugin'

defineOptions({ name: 'FunctionInfo' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: FunctionModel | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()
const { typeHeaders: types } = useHeaders()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const formState = ref<FunctionModel>(null as unknown as FunctionModel)
const loading = ref(false)
const saving = ref(false)
const title = ref<string | null>(null)
const plugins = ref<any[]>([])

const handlerInitialize = () => {
  title.value = `${ t('function.common.create') }`
  if (props.info) {
    formState.value = cloneDeep(omit(props.info, ['createTime', 'updateTime'])) as FunctionModel
    title.value = `${ t('function.common.modify').replace('$NAME', props.info.name as string) }`
  }
  else {
    formState.value = {
      name: undefined,
      plugin: undefined,
      content: undefined,
      description: undefined,
      type: undefined,
      example: undefined
    } as unknown as FunctionModel
  }

  loading.value = true
  PluginService.getPlugins()
               .then(response => {
                 if (response.status) {
                   plugins.value = response.data
                                           .filter((value: { type: string }) => value.type === 'CONNECTOR')
                                           .map((value: { name: string }) => ({ label: value.name, value: value.name }))
                 }
               })
               .finally(() => (loading.value = false))
}

const onSubmit = () => {
  saving.value = true
  FunctionService.saveOrUpdate(formState.value)
                 .then((response) => {
                   if (response.status) {
                     visible.value = false
                     message.success('Save successfully')
                   }
                   else {
                     message.error(response.message)
                   }
                 })
                 .finally(() => (saving.value = false))
}

handlerInitialize()
</script>
