<template>
  <a-row :gutter="8">
    <a-col :span="4">
      <a-space direction="vertical" :style="{ width: '100%' }">
        <a-card class="w-full h-full">
          <template #title>
            <span class="text-sm font-semibold">{{ $t('dataset.common.columnModeMetric') }}</span>
          </template>

          <div class="relative p-1" style="min-height: 200px;">
            <a-spin :spinning="initialize">
              <div v-if="!initialize">
                <Draggable item-key="id"
                           :group="{ name: 'metrics', pull: 'clone', put: false }"
                           :list="originalMetrics"
                           :clone="onClone"
                           @start="visibleHighlight(true, ColumnType.METRIC)"
                           @end="visibleHighlight(false, ColumnType.METRIC)">
                  <template #item="{ element }">
                    <a-tag class="my-1 mx-0.5 cursor-pointer" @dblclick="onClone(element)">{{ element.aliasName ? element.aliasName : element.name }}</a-tag>
                  </template>
                </Draggable>
              </div>
            </a-spin>
          </div>
        </a-card>

        <a-card class="w-full">
          <template #title>
            <span class="text-sm font-semibold">{{ $t('dataset.common.columnModeDimension') }}</span>
          </template>

          <div class="relative p-1" style="min-height: 200px;">
            <a-spin :spinning="initialize">
              <div v-if="!initialize">
                <Draggable item-key="id"
                           :group="{ name: 'dimensions', pull: 'clone', put: false }"
                           :list="originalDimensions"
                           :clone="onClone"
                           @start="visibleHighlight(true, ColumnType.DIMENSION)"
                           @end="visibleHighlight(false, ColumnType.DIMENSION)">
                  <template #item="{ element }">
                    <a-tag class="my-1 mx-0.5 cursor-pointer" @dblclick="onClone(element)">{{ element.aliasName ? element.aliasName : element.name }}</a-tag>
                  </template>
                </Draggable>
              </div>
            </a-spin>
          </div>
        </a-card>
      </a-space>
    </a-col>

    <a-col :span="20">
      <div class="relative">
        <a-spin :spinning="loading">
          <div class="flex items-center space-x-2 text-sm">
            <div>{{ $t('dataset.common.columnModeMetric') }}:</div>

            <div :class="cn('w-full flex-1 p-1',
                        (highlight.active && highlight.type === ColumnType.METRIC) && 'border-2 border-green-400 rounded-sm min-h-8'
                 )">
              <Draggable group="metrics"
                         item-key="id"
                         :list="metrics"
                         class="flex flex-wrap gap-2">
                <template #item="{ element, index }">
                  <a-tag class="inline-flex items-center whitespace-nowrap">
                    <span class="flex items-center">
                      <DatasetColumnMetric :element="element"/>
                    </span>

                    <span class="ml-2 flex items-center space-x-1">
                      <a-tooltip :title="$t('common.configure')">
                        <SettingOutlined class="cursor-pointer hover:text-primary" @click="onColumnConfigure(true, element, ColumnType.METRIC)" :style="{ fontSize: '15px' }"/>
                      </a-tooltip>

                      <a-tooltip :title="$t('common.remove')">
                        <DeleteOutlined class="cursor-pointer text-red-400 hover:text-red-500" @click="onRemove(index, metrics)" :style="{ fontSize: '15px' }"/>
                      </a-tooltip>
                    </span>
                  </a-tag>
                </template>
              </Draggable>
            </div>
          </div>

          <a-divider class="my-2"/>

          <div class="flex items-center space-x-2 text-sm">
            <div>{{ $t('dataset.common.columnModeDimension') }}:</div>

            <div :class="cn('w-full flex-1 p-1',
                         (highlight.active && highlight.type === ColumnType.DIMENSION) && 'border-2 border-blue-400 rounded-sm min-h-8')
                 ">
              <Draggable group="dimensions"
                         item-key="id"
                         :list="dimensions"
                         class="flex flex-wrap gap-2">
                <template #item="{ element, index}">
                  <a-tag class="inline-flex items-center whitespace-nowrap">
                    <span class="flex items-center">
                        {{ element.aliasName ? element.aliasName : element.name }}
                    </span>

                    <span class="ml-2 flex items-center space-x-1">
                      <a-tooltip :title="$t('common.configure')">
                        <SettingOutlined class="cursor-pointer hover:text-primary" @click="onColumnConfigure(true, element, ColumnType.DIMENSION)" :style="{ fontSize: '15px' }"/>
                      </a-tooltip>

                      <a-tooltip :title="$t('common.remove')">
                        <DeleteOutlined class="cursor-pointer text-red-400 hover:text-red-500" @click="onRemove(index, dimensions)" :style="{ fontSize: '15px' }"/>
                      </a-tooltip>
                    </span>
                  </a-tag>
                </template>
              </Draggable>
            </div>
          </div>

          <a-divider class="my-2"/>

          <div class="flex items-center space-x-2 text-sm">
            <div>{{ $t('dataset.common.columnModeFilter') }}:</div>

            <div :class="cn('w-full flex-1 p-1',
                        (highlight.active && highlight.type === ColumnType.DIMENSION) && 'border-2 border-yellow-400 rounded-sm min-h-8')
                 ">
              <Draggable group="dimensions"
                         item-key="id"
                         :list="filters"
                         class="flex flex-wrap gap-2">
                <template #item="{ element, index}">
                  <a-tag class="inline-flex items-center whitespace-nowrap">
                    <span class="flex items-center">
                        {{ element.aliasName ? element.aliasName : element.name }}
                    </span>

                    <span class="ml-2 flex items-center space-x-1">
                      <a-tooltip :title="$t('common.configure')">
                        <SettingOutlined class="cursor-pointer hover:text-primary" @click="onColumnConfigure(true, element, ColumnType.FILTER)" :style="{ fontSize: '15px' }"/>
                      </a-tooltip>

                      <a-tooltip :title="$t('common.remove')">
                        <DeleteOutlined class="cursor-pointer text-red-400 hover:text-red-500" @click="onRemove(index, filters)" :style="{ fontSize: '15px' }"/>
                      </a-tooltip>
                    </span>
                  </a-tag>
                </template>
              </Draggable>
            </div>
          </div>

          <a-divider class="my-2"/>

          <div class="flex justify-between items-center">
            <div class="flex items-center space-x-4">
              <span>{{ $t('dataset.common.showPageSize') }}</span>
              <a-input-number v-model:value="configure.limit" :min="1"/>
            </div>

            <div class="flex items-center space-x-4 text-sm">
              <a-button type="primary" :disabled="loading" :loading="loading" @click="onApplyAdhoc">
                <template #icon>
                  <PlayCircleOutlined :style="{ fontSize: '16px' }"/>
                </template>
              </a-button>

              <a-button :disabled="!showSql.content || loading" @click="visibleShowSql(true)">
                <template #icon>
                  <EyeOutlined :style="{ fontSize: '16px' }"/>
                </template>
              </a-button>

              <a-button type="primary" @click="publishVisible = true">
                {{ $t('common.publish') }}
              </a-button>
            </div>
          </div>

          <a-divider class="my-2"/>
        </a-spin>
      </div>

      <VisualEditor :loading="loading" :configuration="configuration as any" @commitOptions="visibleCommitOptions"/>
    </a-col>
  </a-row>

  <SqlInfo v-if="showSql.visible"
           :is-visible="showSql.visible"
           :content="showSql.content"
           @close="visibleShowSql(false)"/>

  <DatasetColumnConfigure v-if="columnContent.visible"
                          :is-visible="columnContent.visible"
                          :column-type="columnContent.type"
                          :content="columnContent.content"
                          :configure="columnContent.configure"
                          @close="onColumnConfigure(false, null, null)"
                          @commit="onCommitColumnConfigure"/>

  <DatasetReport v-if="publishVisible"
                 :info="dataInfo as any"
                 :code="reportCode"
                 :dimension="originalDimensions[0] as any"
                 :visible="publishVisible"
                 :commit-options="commitOptions as any"
                 :configure="configure as any"
                 @close="visiblePublish(false)"/>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import Draggable from 'vuedraggable'
import DatasetService from '@/services/dataset'
import { Type as ColumnType } from './Type'
import ReportService from '@/services/report'
import { cloneDeep } from 'lodash'
import { Configuration } from '@/views/components/visual/Configuration'
import VisualEditor from '@/views/components/visual/VisualEditor.vue'
import DatasetColumnMetric from '@/views/pages/admin/dataset/components/adhoc/DatasetColumnMetric.vue'
import DatasetColumnConfigure from '@/views/pages/admin/dataset/components/adhoc/DatasetColumnConfigure.vue'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'
import { cn } from '@/lib/utils.ts'
import DatasetReport from '@/views/pages/admin/dataset/components/DatasetReport.vue'
import { DeleteOutlined, EyeOutlined, PlayCircleOutlined, SettingOutlined } from '@ant-design/icons-vue'

defineOptions({ name: 'DatasetAdhoc' })

const route = useRoute()

const loading = ref(false)
const code = ref<string | null>(null)
const reportCode = ref<any>(null)
const originalMetrics = ref<any[]>([])
const originalDimensions = ref<any[]>([])
const originalData = ref<any[]>([])
const metrics = ref<any[]>([])
const dimensions = ref<any[]>([])
const filters = ref<any[]>([])
const configure = reactive({
  columns: [] as any[],
  limit: 1000
})
const configuration = ref<Configuration | null>(null)
const showSql = reactive({
  visible: false,
  content: null as string | null
})
const columnContent = reactive({
  visible: false,
  type: null as ColumnType | null,
  content: [] as never[],
  configure: null as any | null
})
const commitOptions = ref<any>(null)
const publishVisible = ref(false)
const initialize = ref(false)
const highlight = reactive({
  active: false,
  type: 'METRIC' as any
})
const dataInfo = reactive({ name: null as string | null, description: null as string | null })

const splitColumns = (original: any[]): any[] => {
  const array: any[] = []
  original.forEach((item: any) => array.push({
    id: item.id,
    mode: item.mode,
    alias: item.alias,
    expression: item.expression,
    name: item.name,
    function: item.function,
    value: item.value,
    order: item.order
  }))
  return array
}

const onAdhoc = () => {
  loading.value = true
  DatasetService.adhoc(code.value as string, configure)
                .then((response) => {
                  if (response.status) {
                    if (configuration.value) {
                      if (response.data.isSuccessful) {
                        configuration.value.headers = response.data.headers
                        configuration.value.columns = response.data.columns
                        showSql.content = response.data.content
                        configuration.value.message = null
                      }
                      else {
                        configuration.value.headers = []
                        configuration.value.columns = []
                        configuration.value.message = response.data.message
                      }
                    }
                  }
                  else {
                    message.error(response.message)
                  }
                })
                .finally(() => (loading.value = false))
}

const onApplyAdhoc = () => {
  // Set the mode to: FILTER
  filters.value.forEach((item: any) => (item.mode = ColumnType.FILTER))
  configure.columns = [...splitColumns(metrics.value), ...splitColumns(dimensions.value), ...splitColumns(filters.value)]
  onAdhoc()
}

const mergeColumns = (originalColumns: any[], array: any[], type?: ColumnType) => {
  originalColumns.filter((item: any) => {
    const column = originalData.value.filter((value: any) => value.id === item.id)[0]
    if (type) {
      if (item.mode === type) {
        Object.assign(column, item)
        array.push(column)
      }
    }
    else {
      Object.assign(column, item)
      array.push(column)
    }
  })
}

const handleInitialize = () => {
  setTimeout(() => {
    initialize.value = true
    const routeCode = route.params.code as string
    code.value = routeCode
    const id = route.params.id
    reportCode.value = id
    DatasetService.getColumnsByCode(code.value)
                  .then((response) => {
                    if (response.status) {
                      originalData.value = response.data
                      originalMetrics.value = response.data.filter((item: any) => item.mode === 'METRIC')
                      originalDimensions.value = response.data.filter((item: any) => item.mode === 'DIMENSION')
                      if (id) {
                        ReportService.getByCode(id as string)
                                     .then((reportResponse) => {
                                       if (reportResponse.status) {
                                         dataInfo.name = reportResponse.data.name
                                         dataInfo.description = reportResponse.data.description
                                         const query = JSON.parse(reportResponse.data.query)
                                         mergeColumns(query.columns, metrics.value, ColumnType.METRIC)
                                         mergeColumns(query.columns, dimensions.value, ColumnType.DIMENSION)
                                         mergeColumns(query.columns, filters.value, ColumnType.FILTER)
                                         configure.columns = query.columns
                                         configure.limit = query.limit
                                         configuration.value = JSON.parse(reportResponse.data.configure)
                                         onApplyAdhoc()
                                       }
                                     })
                      }
                    }
                    else {
                      message.error(response.message)
                    }
                  })
                  .finally(() => (initialize.value = false))
  }, 0)
}

const onClone = (value: any) => cloneDeep(value)

const onRemove = (index: number, array: any[]) => {
  array.splice(index, 1)
  onApplyAdhoc()
}

const onColumnConfigure = (opened: boolean, record: any, type: ColumnType | null) => {
  columnContent.visible = opened
  columnContent.type = type
  if (record) {
    columnContent.content = originalData.value.find((item: any) => item.id === record.id) as unknown as never[]
    const foundIndex = configure.columns.findIndex((item: any) => item.id === record.id)
    if (foundIndex !== -1) {
      const column = configure.columns[foundIndex]
      column.type = record.type
      columnContent.configure = column
    }
    else {
      columnContent.configure = { id: record.id, type: record.type }
    }
  }
  else {
    columnContent.configure = null
  }
}

const replaceColumn = (originalColumns: any[], originalValue: any) => {
  const index = originalColumns.findIndex((item: any) => item.id === originalValue.id)
  if (index !== -1) {
    const cloneValue = cloneDeep(originalValue)
    originalColumns[index] = Object.assign(originalValue, originalColumns[index], cloneValue)
  }
}

const onCommitColumnConfigure = (value: any) => {
  const clonedValue = cloneDeep(value)
  if (clonedValue.mode === ColumnType.METRIC) {
    replaceColumn(metrics.value, clonedValue)
  }
  else if (clonedValue.mode === ColumnType.DIMENSION) {
    replaceColumn(dimensions.value, clonedValue)
  }
  else if (clonedValue.mode === ColumnType.FILTER) {
    replaceColumn(filters.value, clonedValue)
  }
  onApplyAdhoc()
}

const visibleShowSql = (opened: boolean) => {
  showSql.visible = opened
}

const visibleCommitOptions = (value: any) => {
  commitOptions.value = value
}

const visibleHighlight = (opened: boolean, type: any) => {
  highlight.active = opened
  highlight.type = type
}

const visiblePublish = (opened: boolean) => {
  publishVisible.value = opened
}

configuration.value = new Configuration()
handleInitialize()
</script>
