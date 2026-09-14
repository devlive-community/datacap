<template>
  <a-drawer v-model:open="visible" :title="title" width="40%">
    <div class="relative min-h-screen">
      <a-spin :spinning="loading">
        <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
          <a-alert v-if="testInfo && testInfo.message" type="error" :message="testInfo.message" class="mb-3"/>

          <a-tabs v-model:activeKey="activeTab" @change="onChangeTab">
            <a-tab-pane key="source" :tab="$t('source.common.source')">
              <a-form-item name="type"
                           :label="$t('source.common.type')"
                           :rules="[{ required: true, message: $t('function.tip.selectPluginHolder') }]">
                <a-radio-group v-model:value="formState.type" class="flex flex-wrap gap-3" :disabled="!hasJsonConvert">
                  <a-radio-button v-for="plugin in plugins" :key="plugin.name" :value="plugin.name" class="p-1">
                    <a-tooltip :title="plugin.name">
                      <img class="h-16 w-16 object-contain" :src="'/static/images/plugin/' + plugin.name.toLowerCase() + '.svg'" :alt="plugin.name">
                    </a-tooltip>
                  </a-radio-button>
                </a-radio-group>
              </a-form-item>
            </a-tab-pane>

            <a-tab-pane v-for="tab in configureTabs"
                        :key="tab"
                        :tab="$t(`source.common.${ tab }`)"
                        :disabled="!hasJsonConvert">
              <a-form-item v-for="configure in pluginTabConfigure"
                           :key="configure.field"
                           :name="configure.field"
                           :label="$t('source.common.' + configure.field)"
                           :extra="configure.description">
                <a-input v-if="configure.type === 'String'" v-model:value="configure.value" :disabled="configure.disabled"/>

                <a-input-number v-else-if="configure.type === 'Number'"
                                v-model:value="configure.value"
                                :disabled="configure.disabled"
                                :max="configure.max"
                                :min="configure.min"
                                :style="{ width: '100%' }"/>

                <a-switch v-else-if="configure.type === 'Boolean'" v-model:checked="configure.value" :disabled="configure.disabled"/>

                <a-upload v-else-if="configure.type === 'File'"
                          action="http://localhost:9096/api/v1/source/uploadFile"
                          multiple
                          accept=".xml"
                          :headers="{ 'Authorization': auth?.type + ' ' + auth?.token, 'PluginType': (formState.type as string).split(' ')[0] }"
                          @change="onUploadChange">
                  <a-button>
                    <template #icon>
                      <UploadOutlined/>
                    </template>
                    {{ $t('common.upload') }}
                  </a-button>
                </a-upload>

                <div v-else>
                  <a-space direction="vertical" :style="{ width: '100%' }">
                    <a-button shape="circle" size="small" @click="onPlusConfigure(configure.value)">
                      <template #icon>
                        <ShadcnIcon icon="Plus"/>
                      </template>
                    </a-button>

                    <a-row v-for="(element, index) in configure.value" :key="index" :gutter="10">
                      <a-col :span="10">
                        <a-form-item :label="$t('common.field')">
                          <a-input v-model:value="element.field"/>
                        </a-form-item>
                      </a-col>

                      <a-col :span="12">
                        <a-form-item :label="$t('common.value')">
                          <a-input v-model:value="element.value"/>
                        </a-form-item>
                      </a-col>

                      <a-col :span="2">
                        <a-form-item :label="' '">
                          <a-button shape="circle" danger size="small" @click="onMinusConfigure(element, configure.value)">
                            <template #icon>
                              <ShadcnIcon icon="Minus"/>
                            </template>
                          </a-button>
                        </a-form-item>
                      </a-col>
                    </a-row>
                  </a-space>
                </div>
              </a-form-item>
            </a-tab-pane>
          </a-tabs>

          <div class="flex justify-end">
            <a-space>
              <a-button danger @click="onCancel">
                {{ $t('common.cancel') }}
              </a-button>

              <a-button ghost
                        :loading="testing"
                        :disabled="testing || !formState.type"
                        @click="onTest()">
                {{ $t('common.test') }}
              </a-button>

              <a-button type="primary" html-type="submit" :loading="saving" :disabled="!testInfo.connected || saving">
                {{ $t('common.save') }}
              </a-button>
            </a-space>
          </div>
        </a-form>
      </a-spin>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { nextTick, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { UploadOutlined } from '@ant-design/icons-vue'
import { cloneDeep, pick } from 'lodash'
import { computed } from 'vue'
import { SourceModel, SourceRequest } from '@/model/source'
import SourceService from '@/services/source'
import PluginService from '@/services/plugin'
import { TokenUtils } from '@/utils/token'
import { ResponseModel } from '@/model/response'

interface TestInfo
{
  connected: boolean
  percent: number
  successful: boolean
  message?: null | string
}

defineOptions({ name: 'SourceInfo' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: SourceModel | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()
const auth = TokenUtils.getAuthUser()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const formState = ref<SourceModel>(null as unknown as SourceModel)
const loading = ref(false)
const saving = ref(false)
const title = ref<string | null>(null)
const testing = ref(false)
const testInfo = ref<TestInfo>({ connected: false, percent: 0, successful: false })
const configureTabs = ref<any[]>([])
const activeTab = ref('source')
const plugins = ref<any[]>([])
const pluginConfigure = ref<any>(null)
const pluginTabConfigure = ref<any>(null)
const applyConfigure = ref<any>(null)
const originalSchema = ref<any>(null)
const hasJsonConvert = ref(false)

const resetConfigureTab = () => {
  if (!pluginConfigure.value || !Array.isArray(pluginConfigure.value)) {
    configureTabs.value = []
    return
  }
  const validGroups = pluginConfigure.value
                                     .map((v: { group: string }) => v.group)
                                     .filter((group: any) => group && typeof group === 'string')
  configureTabs.value = [...new Set(validGroups)]
}

const updatePluginTabConfigure = (tabValue: string) => {
  if (tabValue !== 'source' && pluginConfigure.value) {
    pluginTabConfigure.value = pluginConfigure.value.filter((field: { group: string }) => field.group === tabValue)
  }
}

const onChangeTab = (value: any) => {
  updatePluginTabConfigure(value)
}

const handleInitialize = async () => {
  try {
    loading.value = true
    testInfo.value = { connected: false, percent: 0, successful: false }
    title.value = `${ t('source.common.create') }`

    const pluginsResponse = await PluginService.getPlugins(true)
    hasJsonConvert.value = pluginsResponse.data.some((value: any) => value.type?.toLowerCase() === 'convert' && value.name === 'JsonConvert')

    if (!hasJsonConvert.value) {
      testInfo.value.message = t('plugin.text.requiredJsonConvert')
    }

    if (pluginsResponse.status) {
      plugins.value = pluginsResponse.data.filter((plugin: { type: string }) => plugin.type?.toLowerCase() === 'connector')
    }

    if (props.info) {
      title.value = `${ t('source.common.modify').replace('$NAME', String(props.info.name || '...')) }`
      const sourceResponse = await SourceService.getByCode(props.info.code)
      if (sourceResponse.status) {
        formState.value = cloneDeep(sourceResponse.data)
        applyConfigure.value = sourceResponse.data?.schema
        pluginConfigure.value = applyConfigure.value?.configures
        originalSchema.value = cloneDeep(sourceResponse.data?.schema)
        if (pluginConfigure.value) {
          resetConfigureTab()
          updatePluginTabConfigure('source')
        }
      }
    }
    else {
      formState.value = SourceRequest.of()
    }
  }
  catch (error) {
    console.error('Failed to initialize:', error)
    message.error('Failed to initialize the form')
  }
  finally {
    loading.value = false
  }
}

watch(() => formState.value?.type, (newValue: any) => {
  if (!newValue) {
    return
  }
  if (newValue instanceof Array) {
    formState.value.type = newValue[0]
  }
  try {
    activeTab.value = 'source'
    const applyPlugin = plugins.value.find((plugin: { name: string }) => plugin.name === formState.value.type) as any
    const isEditingMatchingType = props.info && props.info.type === formState.value.type

    if (isEditingMatchingType && originalSchema.value) {
      applyConfigure.value = cloneDeep(originalSchema.value)
      pluginConfigure.value = applyConfigure.value?.configures
    }
    else {
      applyConfigure.value = cloneDeep(applyPlugin?.configure)
      pluginConfigure.value = applyConfigure.value?.configures
    }

    nextTick(() => {
      resetConfigureTab()
      if (configureTabs.value.length > 0) {
        updatePluginTabConfigure('source')
      }
    })
  }
  catch (error) {
    console.error('Plugin change error:', error)
  }
})

const onCancel = () => {
  visible.value = false
}

const onTest = () => {
  testing.value = true
  const { configures } = applyConfigure.value
  const configure = {
    type: formState.value.type,
    configure: {
      configures: configures.map((item: any) => pick(item, ['field', 'required', 'type', 'min', 'max', 'message', 'value']))
    }
  }
  SourceService.testConnection(configure)
               .then((response) => {
                 testInfo.value.percent = 100
                 if (response.status) {
                   message.success('Test successful')
                   testInfo.value.connected = true
                   testInfo.value.successful = true
                   testInfo.value.message = null
                   formState.value.version = extractVersion(response.data)
                 }
                 else {
                   testInfo.value.message = response.message
                   testInfo.value.connected = false
                   testInfo.value.successful = false
                 }
               })
               .finally(() => (testing.value = false))
}

const onSubmit = () => {
  saving.value = true
  const { configures } = applyConfigure.value
  const configure = {
    code: formState.value.code,
    name: formState.value.type,
    configure: {
      configures: configures.map((item: any) => pick(item, ['field', 'required', 'type', 'min', 'max', 'message', 'value']))
    },
    type: formState.value.type,
    version: formState.value.version
  }
  SourceService.saveOrUpdate(configure as any)
               .then((response) => {
                 if (response.status) {
                   message.success('Create successful')
                   onCancel()
                 }
               })
               .finally(() => (saving.value = false))
}

const onUploadChange = (info: any) => {
  if (info.file.status === 'done') {
    handlerUploadSuccess(info.file.response)
  }
}

const handlerUploadSuccess = (response: ResponseModel) => {
  if (response.status) {
    const configure = applyConfigure.value.configures.filter((configure: { field: string }) => configure.field === 'file')
    configure[0].value.push(response.data)
  }
}

const onPlusConfigure = (array: Array<any>) => {
  if (!array) {
    array = new Array<any>()
  }
  array.push({ field: '', value: '' })
}

const onMinusConfigure = (configure: any, array: Array<any>) => {
  const index = array.indexOf(configure)
  if (index !== -1) {
    array.splice(index, 1)
  }
}

const extractVersion = (json: any) => {
  const columnName = json['headers'][0]
  const column = json['columns'][0]
  if (typeof column === 'object' && column !== null && !Array.isArray(column)) {
    const value = column[columnName]
    if (value !== undefined) {
      if (typeof value === 'object' && value.values && Array.isArray(value.values)) {
        return value.values.length === 1 ? value.values[0] : value.values
      }
      if (typeof value === 'object' && value.string) {
        return value.string
      }
      return value
    }
    return column
  }
  if (typeof column === 'string') {
    if (column.startsWith('"') && column.endsWith('"') && column.length > 1) {
      return column.slice(1, -1)
    }
    return column
  }
  return column
}

handleInitialize()
</script>
