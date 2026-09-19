<template>
  <a-card :title="$t('common.dataset')" :bordered="false">
    <template #extra>
      <a-button :disabled="!data?.data.columns" @click="configureVisible = true">
        {{ $t('common.configure') }}
      </a-button>
    </template>

    <div class="relative h-full space-y-4" style="min-height: 200px;">
      <a-spin :spinning="loading">
        <a-card v-if="sourceInfo" class="w-full">
          <template #title>
            <a-button type="primary" :loading="running" :disabled="running" @click="onRun()">
              {{ $t('query.common.execute') }}
            </a-button>
          </template>

          <div class="relative">
            <a-spin :spinning="running">
              <AceEditor :value="value" @update:value="value = $event"/>
            </a-spin>
          </div>
        </a-card>

        <div v-if="data || code" class="relative">
          <a-spin :spinning="running">
            <AgGridVue v-if="data?.data.columns"
                       :style="{height: '300px'}"
                       class="ag-theme-datacap"
                       :pagination="true"
                       :columnDefs="columnDefs"
                       :rowData="data.data.columns"
                       :gridOptions="gridOptions as any"/>
          </a-spin>
        </div>

        <div v-if="!sourceInfo" class="mt-3 justify-center items-center">
          <div class="flex flex-col items-center space-y-4">
            <a-alert :message="i18n.t('dataset.common.onlyPreviewCreate')"/>

            <a-button>
              <router-link to="/admin/query">
                {{ i18n.t('dataset.common.returnQuery') }}
              </router-link>
            </a-button>
          </div>
        </div>
      </a-spin>
    </div>
  </a-card>

  <a-drawer v-model:open="configureVisible"
            height="70%"
            placement="bottom"
            :closable="false"
            :title="$t('common.configure')">
    <template #extra>
      <div class="flex items-center justify-end space-x-2">
        <a-button type="primary" @click="onSubmit">
          {{ code ? $t('dataset.common.modify') : $t('dataset.common.create') }}
        </a-button>

        <a-button @click="configureVisible = false">
          {{ $t('common.cancel') }}
        </a-button>
      </div>
    </template>

    <a-alert v-if="validator" type="error" class="mt-2" :message="validatorMessage"/>

    <a-tabs v-model:activeKey="activeTab" class="mt-1">
      <a-tab-pane key="columns" :tab="$t('dataset.common.dataColumn')" class="space-y-2">
        <a-spin :spinning="loading">
          <a-row :gutter="8">
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnName') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnAlias') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnType') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnMode') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnDefaultValue') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnIsNullable') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnIsOrderByKey') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnIsPartitionKey') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnIsPrimaryKey') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnIsSampling') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('dataset.common.columnLength') }}</a-col>
            <a-col :span="2" class="text-center">{{ $t('common.action') }}</a-col>
          </a-row>

          <a-row :gutter="8">
            <template v-for="(item, index) in formState.columns" :key="index">
              <a-col :span="2" class="flex justify-center">
                <a-input v-model:value="item.name"/>
              </a-col>

              <a-col :span="2" class="flex justify-center">
                <a-input v-model:value="item.aliasName"/>
              </a-col>

              <a-col :span="2" class="flex justify-center">
                <a-select v-model:value="item.type" placeholder="Options" class="w-full">
                  <a-select-option value="STRING">{{ $t('dataset.common.columnTypeString') }}</a-select-option>
                  <a-select-option value="NUMBER">{{ $t('dataset.common.columnTypeNumber') }}</a-select-option>
                  <a-select-option value="NUMBER_SIGNED">{{ $t('dataset.common.columnTypeNumberSigned') }}</a-select-option>
                  <a-select-option value="BOOLEAN">{{ $t('dataset.common.columnTypeBoolean') }}</a-select-option>
                  <a-select-option value="DATETIME">{{ $t('dataset.common.columnTypeDateTime') }}</a-select-option>
                </a-select>
              </a-col>

              <a-col :span="2" class="flex justify-center items-center">
                <a-switch v-model:checked="item.mode" checked-value="METRIC" un-checked-value="DIMENSION">
                  <template #checkedChildren>{{ $t('dataset.common.columnModeDimension') }}</template>
                  <template #unCheckedChildren>{{ $t('dataset.common.columnModeMetric') }}</template>
                </a-switch>
              </a-col>

              <a-col :span="2" class="flex justify-center">
                <a-input v-model:value="item.defaultValue" :disabled="item.virtualColumn"/>
              </a-col>

              <a-col :span="2" class="flex justify-center items-center">
                <a-switch v-model:checked="item.nullable" :disabled="item.virtualColumn" @change="setNullable(item, $event)"/>
              </a-col>

              <a-col :span="2" class="flex justify-center items-center">
                <a-switch v-model:checked="item.orderByKey" :disabled="item.virtualColumn" @change="setOrderByKey(item, $event)"/>
              </a-col>

              <a-col :span="2" class="flex justify-center items-center">
                <a-switch v-model:checked="item.partitionKey" :disabled="item.virtualColumn" @change="setPartitionKey(item, $event)"/>
              </a-col>

              <a-col :span="2" class="flex justify-center items-center">
                <a-switch v-model:checked="item.primaryKey" :disabled="item.virtualColumn" @change="setPrimaryKey(item, $event)"/>
              </a-col>

              <a-col :span="2" class="flex justify-center items-center">
                <a-switch v-model:checked="item.samplingKey" :disabled="item.virtualColumn" @change="setSamplingKey(item, $event)"/>
              </a-col>

              <a-col :span="2" class="flex justify-center">
                <a-input-number v-model:value="item.length" :disabled="item.type === 'BOOLEAN' || item.type === 'DATETIME' || item.virtualColumn"/>
              </a-col>

              <a-col :span="2" class="flex justify-center items-center space-x-1">
                <a-popover placement="bottom">
                  <template #content>
                    <div class="p-2 space-y-2">
                      <div class="text-base font-medium">{{ $t('dataset.common.columnComment') }}</div>
                      <a-textarea v-model:value="item.comment"/>
                    </div>
                  </template>

                  <a-button type="text" shape="circle" size="small">
                    <template #icon>
                      <EditOutlined :style="{ fontSize: '10px' }"/>
                    </template>
                  </a-button>
                </a-popover>

                <a-button type="text" shape="circle"
                          danger
                          size="small"
                          :disabled="!item.customColumn"
                          @click="onRemoveColumn(index)">
                  <template #icon>
                    <DeleteOutlined :style="{ fontSize: '10px' }"/>
                  </template>
                </a-button>

                <a-button type="text" shape="circle" size="small" @click="onAddColumn(index)">
                  <template #icon>
                    <PlusOutlined :style="{ fontSize: '10px' }"/>
                  </template>
                </a-button>
              </a-col>
            </template>
          </a-row>
        </a-spin>
      </a-tab-pane>

      <a-tab-pane key="configure" :tab="$t('dataset.common.dataConfigure')">
        <a-form class="w-[40%] mx-auto items-center" :model="formState" layout="vertical">
          <a-row :gutter="8">
            <a-col :span="12">
              <a-form-item name="name" :label="$t('common.name')" :rules="[{ required: true, message: $t('common.name') }]">
                <a-input v-model:value="formState.name"/>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item name="executor" :label="$t('common.executor')">
                <a-select v-model:value="formState.executor">
                  <a-select-option v-for="item in executors" :key="item.name" :value="item.name">{{ item.name }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item name="syncMode" :label="$t('dataset.common.syncMode')">
                <a-select v-model:value="formState.syncMode">
                  <a-select-option value="MANUAL">{{ $t('dataset.common.syncModeManual') }}</a-select-option>
                  <a-select-option value="TIMING">{{ $t('dataset.common.syncModeTiming') }}</a-select-option>
                  <a-select-option value="OUT_SYNC">{{ $t('dataset.common.syncModeOutSync') }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item name="expression" :label="$t('dataset.common.columnExpression')">
                <a-input v-model:value="formState.expression" :disabled="formState.syncMode !== 'TIMING'"/>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item name="scheduler" :label="$t('common.scheduler')">
                <a-select v-model:value="formState.scheduler" :disabled="formState.syncMode !== 'TIMING'">
                  <a-select-option v-for="item in schedulers" :key="item.name" :value="item.name">{{ item.name }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="24">
              <a-divider/>
            </a-col>

            <a-col :span="24">
              <a-alert v-if="formState.columns.filter(item => item.type === 'DATETIME').length === 0" type="error"
                       :message="$t('dataset.tip.lifeCycleMustDateColumn')"/>
            </a-col>

            <a-col :span="24">
              <a-form-item name="lifeCycleColumn" :label="$t('dataset.common.lifeCycleColumn')" :extra="$t('dataset.tip.lifeCycle')">
                <a-select v-model:value="formState.lifeCycleColumn"
                          :disabled="formState.columns.filter(item => item.type === 'DATETIME').length === 0">
                  <a-select-option v-for="item in formState.columns.filter(v => v.type === 'DATETIME')" :key="item.name" :value="item.name">{{ item.name }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item name="lifeCycleType" :label="$t('dataset.common.lifeCycleType')">
                <a-select v-model:value="formState.lifeCycleType" :disabled="!formState.lifeCycleColumn">
                  <a-select-option value="MONTH">{{ $t('dataset.common.lifeCycleMonth') }}</a-select-option>
                  <a-select-option value="WEEK">{{ $t('dataset.common.lifeCycleWeek') }}</a-select-option>
                  <a-select-option value="DAY">{{ $t('dataset.common.lifeCycleDay') }}</a-select-option>
                  <a-select-option value="HOUR">{{ $t('dataset.common.lifeCycleHour') }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item name="lifeCycle" :label="$t('dataset.common.lifeCycleNumber')">
                <a-input-number v-model:value="formState.lifeCycle" :min="1" :disabled="!formState.lifeCycleColumn" :style="{ width: '100%' }"/>
              </a-form-item>
            </a-col>

            <a-col :span="24">
              <a-divider/>
            </a-col>

            <a-col :span="24">
              <a-form-item name="description" :label="$t('common.description')">
                <a-textarea v-model:value="formState.description"/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-tab-pane>
    </a-tabs>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import GridOptions from '@/views/components/grid/GridOptions'
import DatasetService from '@/services/dataset'
import { HttpUtils } from '@/utils/http'
import { GridColumn } from '@/views/components/grid/GridColumn'
import PluginService from '@/services/plugin'
import { AgGridVue } from 'ag-grid-vue3'
import 'ag-grid-community/styles/ag-grid.css'
import '@/views/components/grid/ag-theme-datacap.css'
import { DatasetModel } from '@/model/dataset'
import { ResponseModel } from '@/model/response.ts'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import { SourceModel } from '@/model/source.ts'
import SourceService from '@/services/source'
import ExecuteService from '@/services/execute'
import { ExecuteModel } from '@/model/execute.ts'
import { ArrayUtils } from '@/utils/array.ts'
import { join } from 'lodash'
import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons-vue'

defineOptions({ name: 'DatasetInfo' })

const i18n = useI18n()
const route = useRoute()
const router = useRouter()
const gridOptions = GridOptions.createDefaultOptions(i18n)

const code = ref<string | null>(null)
const loading = ref(false)
const saving = ref(false)
const validator = ref(false)
const validatorMessage = ref<string | null>(null)
const columnDefs = ref<GridColumn[]>([])
const schedulers = ref<any[]>([])
const executors = ref<any[]>([])
const configureVisible = ref(false)
const formState = ref<any>({
  id: null,
  name: null as string | null | undefined,
  description: null as string | null | undefined,
  query: null as string | null,
  syncMode: 'MANUAL',
  columns: [] as any[],
  source: { code: null },
  expression: null as string | null,
  scheduler: 'Default',
  executor: 'Local',
  lifeCycle: null as number | null,
  lifeCycleColumn: null as string | null,
  lifeCycleType: null as string | null
})
const data = ref<ResponseModel | null>(null)
const sourceInfo = ref<SourceModel | null>(null)
const value = ref('')
const running = ref(false)
const activeTab = ref('columns')

const onRun = () => {
  const configure: ExecuteModel = {
    content: value.value,
    name: sourceInfo.value?.code as unknown as string,
    mode: 'DATASET'
  }
  running.value = true
  ExecuteService.execute(configure, null)
                .then((response) => {
                  if (response.status) {
                    data.value = response
                    columnDefs.value = []
                    response.data?.headers.forEach((header: any) => {
                      const columnDef: GridColumn = { headerName: header, field: header }
                      columnDefs.value.push(columnDef)
                    })
                    if (formState.value.columns.length === 0) {
                      response.data?.headers.map((header: any, index: number) => {
                        const column = {
                          id: null,
                          name: `column_${ index + 1 }`,
                          aliasName: header.replace('(', '_').replace(')', ''),
                          type: 'STRING',
                          comment: header,
                          defaultValue: null,
                          position: index,
                          nullable: false,
                          length: 0,
                          original: header,
                          orderByKey: false,
                          partitionKey: false,
                          primaryKey: false,
                          samplingKey: false,
                          mode: 'DIMENSION',
                          virtualColumn: false,
                          customColumn: false
                        }
                        formState.value.columns.push(column)
                      })
                    }
                  }
                  else {
                    message.error(response.message)
                  }
                })
                .finally(() => (running.value = false))
}

const handleInitialize = () => {
  setTimeout(() => {
    PluginService.getPlugins()
                 .then((response) => {
                   if (response.status) {
                     schedulers.value = response.data.filter((v: { type: string }) => v.type === 'SCHEDULER')
                     executors.value = response.data.filter((v: { type: string }) => v.type === 'EXECUTOR')
                   }
                 })
    const routeCode = route.params.code
    const sourceCode = route.params.sourceCode

    if (routeCode) {
      loading.value = true
      code.value = routeCode as string
      const axios = new HttpUtils().getAxios()
      axios.all([DatasetService.getByCode(code.value), DatasetService.getColumnsByCode(code.value)])
           .then(axios.spread((info: any, column: any) => {
             if (info.status) {
               formState.value = info.data
               formState.value.source.code = info.data.source.code
               sourceInfo.value = info.data.source
               value.value = info.data.query
               onRun()
             }
             if (column.status) {
               formState.value.columns = column.data
             }
           }))
           .finally(() => (loading.value = false))
    }
    else if (sourceCode) {
      const tempId = route.query.tempId
      if (tempId) {
        value.value = localStorage.getItem(`QueryContent_${ tempId }`) || ''
        localStorage.removeItem(`QueryContent_${ tempId }`)
      }
      loading.value = true
      SourceService.getByCode(sourceCode as string)
                   .then((response) => {
                     if (response.status) {
                       sourceInfo.value = response.data
                       formState.value.source.code = response.data.code
                     }
                   })
                   .finally(() => (loading.value = false))
    }
  })
}

const validatorSampling = () => {
  const samplingColumns = formState.value.columns.filter((item: any) => item.samplingKey)
  if (samplingColumns.length === 0) {
    validator.value = false
    validatorMessage.value = null
    return
  }

  const orderByColumns = formState.value.columns.filter((item: any) => item.orderByKey)
  const isNameInOrderByColumns = samplingColumns.every((samplingItem: any) => {
    return orderByColumns.some((orderByItem: any) => orderByItem.name === samplingItem.name)
  })
  if (!isNameInOrderByColumns) {
    validator.value = true
    validatorMessage.value = i18n.t('dataset.tip.validatorSampling') as string
  }
  else {
    validator.value = false
    validatorMessage.value = null
  }
}

const beforeCheck = (): boolean => {
  const duplicateColumns = ArrayUtils.findDuplicates(formState.value.columns)
  if (duplicateColumns.length > 0) {
    validator.value = true
    validatorMessage.value = i18n.t('dataset.validator.duplicateColumn').replace('$VALUE', join(duplicateColumns, ','))
    return true
  }

  const orderByColumns = formState.value.columns.filter((item: any) => item.orderByKey)
  const primaryKeyColumns = formState.value.columns.filter((item: any) => item.primaryKey)
  if (orderByColumns.length === 0 && primaryKeyColumns.length === 0) {
    validator.value = true
    validatorMessage.value = i18n.t('dataset.validator.specifiedColumn')
    return true
  }

  if (!formState.value.name) {
    validator.value = true
    validatorMessage.value = i18n.t('dataset.validator.specifiedName')
    return true
  }
  return false
}

const onSubmit = () => {
  if (!beforeCheck()) {
    saving.value = true
    formState.value.query = value.value
    DatasetService.saveOrUpdate(formState.value as unknown as DatasetModel)
                  .then((response) => {
                    if (response.status) {
                      message.success(i18n.t('dataset.tip.publishSuccess').replace('$VALUE', formState.value.name as string))
                      router.push('/admin/dataset')
                    }
                  })
                  .finally(() => (saving.value = false))
  }
}

const onAddColumn = (index: number) => {
  formState.value.columns.splice(index + 1, 0, {
    id: null,
    name: null,
    aliasName: null,
    type: 'STRING',
    comment: null,
    defaultValue: null,
    position: index + 1,
    nullable: false,
    length: 0,
    original: null,
    orderByKey: false,
    partitionKey: false,
    primaryKey: false,
    samplingKey: false,
    mode: 'DIMENSION',
    virtualColumn: true,
    customColumn: true
  })
}

const onRemoveColumn = (index: number) => {
  formState.value.columns.splice(index, 1)
}

const setNullable = (item: any, checked: any) => {
  item.nullable = checked
}

const setOrderByKey = (item: any, checked: any) => {
  item.orderByKey = checked
  validatorSampling()
}

const setPartitionKey = (item: any, checked: any) => {
  item.partitionKey = checked
  validatorSampling()
}

const setPrimaryKey = (item: any, checked: any) => {
  item.primaryKey = checked
  validatorSampling()
}

const setSamplingKey = (item: any, checked: any) => {
  item.samplingKey = checked
  validatorSampling()
}

handleInitialize()
</script>
