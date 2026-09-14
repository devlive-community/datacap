<template>
  <div class="relative">
    <a-spin :spinning="loading">
      <a-card class="h-screen">
        <template #title>
          <a-space class="items-center">
            <a-tooltip :title="$t('source.common.firstPage')">
              <a-button shape="circle"
                        size="small"
                        :disabled="!configure.pagination.hasPrevious"
                        @click="onApplyPagination(configure.operator.FIRST)">
                <template #icon>
                  <ShadcnIcon icon="ArrowLeftToLine" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('source.common.previousPage')">
              <a-button shape="circle"
                        size="small"
                        :disabled="!configure.pagination.hasPrevious"
                        @click="onApplyPagination(configure.operator.PREVIOUS)">
                <template #icon>
                  <ShadcnIcon icon="ArrowLeft" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('source.common.nextPage')">
              <a-button shape="circle"
                        size="small"
                        :disabled="!configure.pagination.hasNext"
                        @click="onApplyPagination(configure.operator.NEXT)">
                <template #icon>
                  <ShadcnIcon icon="ArrowRight" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('source.common.lastPage')">
              <a-button shape="circle"
                        size="small"
                        :disabled="!configure.pagination.hasNext"
                        @click="onApplyPagination(configure.operator.LAST)">
                <template #icon>
                  <ShadcnIcon icon="ArrowRightToLine" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('user.common.setting')">
              <a-button shape="circle" size="small" @click="visibleSettings(true)">
                <template #icon>
                  <ShadcnIcon icon="Cog" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <div class="text-sm text-muted-foreground flex gap-2 ml-4 mr-4">
              [ <span>{{ configure.pagination.startIndex }}</span> / <span>{{ configure.pagination.endIndex }}</span> ]
              <span>of</span>
              <span>{{ configure.pagination.total }}</span>
              <span>{{ $t('source.common.records') }}</span>
            </div>

            <a-tooltip :title="$t('source.common.addRows')">
              <a-button shape="circle" size="small" @click="onAddOrCloneRow(false)">
                <template #icon>
                  <ShadcnIcon icon="Plus" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('source.common.copyRows')">
              <a-button shape="circle"
                        size="small"
                        :disabled="dataSelectedChanged.columns.length === 0"
                        @click="onAddOrCloneRow(true)">
                <template #icon>
                  <ShadcnIcon icon="Copy" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('source.common.deleteRows')">
              <a-button shape="circle"
                        size="small"
                        :disabled="!dataSelectedChanged.changed"
                        @click="visibleChanged(true)">
                <template #icon>
                  <ShadcnIcon icon="Minus" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('source.common.previewPendingChanges')">
              <a-button shape="circle"
                        size="small"
                        :disabled="!dataCellChanged.changed && dataCellChanged.columns.length === 0"
                        @click="visibleCellChanged(true)">
                <template #icon>
                  <ShadcnIcon icon="RectangleEllipsis" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('common.preview')">
              <a-button shape="circle" size="small" @click="visibleContents(true)">
                <template #icon>
                  <ShadcnIcon icon="Eye" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('common.refresh')">
              <a-button shape="circle" size="small" @click="onRefresh">
                <template #icon>
                  <ShadcnIcon icon="RefreshCw" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>
          </a-space>
        </template>

        <template #extra>
          <a-space class="items-center">
            <a-tooltip :title="$t('source.common.visibleColumn')">
              <a-button shape="circle" size="small" @click="visibleColumns(null, true)">
                <template #icon>
                  <ShadcnIcon icon="Columns" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('source.common.filterData')">
              <a-button shape="circle" size="small" @click="onFilterConfigure(true)">
                <template #icon>
                  <ShadcnIcon icon="Filter" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>
          </a-space>
        </template>

        <div class="relative">
          <a-spin :spinning="refererLoading">
            <AgGridVue class="ag-theme-datacap"
                       style="width: 100%; height: calc(100vh - 40px);"
                       :gridOptions="gridOptions"
                       :columnDefs="configure.headers"
                       :rowData="configure.datasets"
                       :tooltipShowDelay="100"
                       :sortingOrder="['desc', 'asc', null]"
                       :rowSelection="'multiple'"
                       @grid-ready="onGridReady"
                       @sortChanged="onSortChanged"
                       @cellValueChanged="onCellValueChanged"
                       @selectionChanged="onSelectionChanged"
                       @columnVisible="onColumnVisible"
                       @columnMoved="onColumnMoved">
            </AgGridVue>
          </a-spin>
        </div>
      </a-card>
    </a-spin>

    <TableCellInfo v-if="dataCellChanged.pending"
                   :isVisible="dataCellChanged.pending"
                   :columns="dataCellChanged.columns"
                   :is-update="dataCellChanged.type === 'UPDATE'"
                   @close="visibleCellChanged(false)">
    </TableCellInfo>

    <TableRowDelete v-if="dataSelectedChanged.pending"
                    :isVisible="dataSelectedChanged.pending"
                    :columns="dataSelectedChanged.columns"
                    @close="visibleChanged(false)">
    </TableRowDelete>

    <TableColumn v-if="visibleColumn.show"
                 :isVisible="visibleColumn.show"
                 :columns="visibleColumn.columns"
                 @close="visibleColumns($event, false)"
                 @change="visibleColumns($event, false)">
    </TableColumn>

    <TableRowFilter v-if="filterConfigure.show"
                    :isVisible="filterConfigure.show"
                    :columns="filterConfigure.columns"
                    :types="filterConfigure.types"
                    :configure="filterConfigure.filters"
                    @apply="onApplyFilter"
                    @close="onFilterConfigure(false)">
    </TableRowFilter>

    <TablePagination v-if="visibleSetting.show"
                     :is-visible="visibleSetting.show"
                     :pagination="configure.pagination"
                     @close="visibleSettings(false)"
                     @change="configure.pagination = $event">
    </TablePagination>

    <SqlInfo v-if="visibleContent.show"
             :isVisible="visibleContent.show"
             :content="visibleContent.content"
             @close="visibleContents(false)">
    </SqlInfo>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { AgGridVue } from 'ag-grid-vue3'
import 'ag-grid-community/styles/ag-grid.css'
import '@/views/components/grid/ag-theme-datacap.css'
import { ColumnApi, ColumnState, GridApi } from 'ag-grid-community'
import { PaginationEnum, PaginationModel } from '@/model/pagination.ts'
import { createColumnDefs, createDataEditorOptions } from '@/views/pages/admin/source/components/TableUtils.ts'
import { cloneDeep } from 'lodash'
import TableRowDelete from '@/views/pages/admin/source/components/TableRowDelete.vue'
import TableCellInfo from '@/views/pages/admin/source/components/TableCellInfo.vue'
import TableColumn from '@/views/pages/admin/source/components/TableColumn.vue'
import TableRowFilter from '@/views/pages/admin/source/components/TableRowFilter.vue'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'
import TablePagination from '@/views/pages/admin/source/components/TablePagination.vue'
import MetadataService from '@/services/metadata'

defineOptions({ name: 'SourceTableData' })

const i18n = useI18n()
const route = useRoute()

const loading = ref(false)
const refererLoading = ref(false)
const gridOptions = ref<any>(null)
let gridApi = null as unknown as GridApi
let gridColumnApi = null as unknown as ColumnApi
const originalColumns = ref<any[]>([])
const originalDatasets = ref<any[]>([])
const newRows = ref<any[]>([])

const configure = reactive({
  headers: [] as any[],
  columns: [] as any[],
  datasets: [] as any[],
  pagination: null as any,
  operator: PaginationEnum
})
const visibleContent = reactive({
  show: false,
  content: null as unknown as string
})
const dataCellChanged = reactive({
  changed: false,
  pending: false,
  type: null as any,
  columns: [] as any[]
})
const dataSelectedChanged = reactive({
  changed: false,
  pending: false,
  columns: [] as any[]
})
const visibleColumn = reactive({
  show: false,
  columns: [] as any[]
})
const visibleSetting = reactive({
  show: false
})
const filterConfigure = reactive({
  show: false,
  columns: [] as any[],
  types: [] as any[],
  filters: [] as any[]
})

const clearData = () => {
  newRows.value = []
}

const handleInitialize = () => {
  clearData()
  gridOptions.value = createDataEditorOptions(i18n)
  if (!configure.pagination) {
    configure.pagination = {
      page: 1,
      size: 100
    }
  }

  const code = route?.params.source as string
  const database = route?.params.database as string
  const table = route?.params.table as string

  if (code && database && table) {
    loading.value = true
    const { pagination } = configure

    const conf = {
      pagination: pagination
    }

    MetadataService.queryTable(code, database, table, conf)
                   .then((response) => {
                     if (response.status && response.data) {
                       configure.headers = createColumnDefs(response.data.headers, response.data.types)
                       originalColumns.value = configure.headers
                       configure.datasets = response.data.columns
                       originalDatasets.value = cloneDeep(response.data.columns)
                       configure.pagination = response.data.pagination
                       visibleContent.content = response.data.content
                       filterConfigure.columns = cloneDeep(response.data.headers)
                       filterConfigure.types = cloneDeep(response.data.types)
                     }
                     else {
                       message.error(response.message)
                     }
                   })
                   .finally(() => (loading.value = false))
  }
}

const handleRefererData = (conf: any) => {
  configure.datasets = []
  gridOptions.value.overlayNoRowsTemplate = '<span></span>'
  const code = route?.params.source as string
  const database = route?.params.database as string
  const table = route?.params.table as string

  if (code && database && table) {
    refererLoading.value = true
    MetadataService.queryTable(code, database, table, conf)
                   .then((response) => {
                     if (response.status && response.data) {
                       configure.headers = createColumnDefs(response.data.headers, response.data.types)
                       configure.datasets = response.data.columns
                       originalDatasets.value = cloneDeep(response.data.columns)
                       if (configure?.datasets?.length === 0) {
                         gridOptions.value.overlayNoRowsTemplate = '<span>No Rows To Show</span>'
                       }
                       configure.pagination = response.data.pagination
                       visibleContent.content = response.data.content
                       filterConfigure.columns = cloneDeep(response.data.headers)
                       filterConfigure.types = cloneDeep(response.data.types)
                     }
                     else {
                       message.error(response.message)
                     }
                   })
                   .finally(() => (refererLoading.value = false))
  }
}

const getSortConfigure = (conf: any) => {
  const columnState = gridColumnApi.getColumnState()
  const orders = columnState.filter((item) => item.sort !== null)
                            .map((column: ColumnState) => ({
                              name: column.colId,
                              order: column.sort
                            }))
  conf.pagination = configure.pagination
  conf.orders = orders
}

const getVisibleColumn = (conf: any) => {
  conf.columns = gridColumnApi.getColumnState()
                              .filter((item) => !item.hide)
                              .map((item: { colId: any }) => ({ name: item.colId }))
}

const getConfigure = (): any => {
  clearData()
  const conf = {
    filters: filterConfigure.filters
  }
  getSortConfigure(conf)
  getVisibleColumn(conf)
  return conf
}

const onRefresh = () => {
  handleRefererData(getConfigure())
}

const onGridReady = (params: { api: GridApi; columnApi: ColumnApi }) => {
  gridApi = params.api
  gridColumnApi = params.columnApi
}

const onSortChanged = () => {
  handleRefererData(getConfigure())
}

const onCellValueChanged = (event: { data: any; colDef: { field: string }; oldValue: any; newValue: any; rowIndex: number }) => {
  // If the index is less than or equal to the length of the current data collection -1, no request type is specified for the new data
  if (event.rowIndex <= originalDatasets.value.length - 1) {
    const oldColumn = event.data
    const originalColumn = cloneDeep(oldColumn)
    originalColumn[event.colDef.field] = event.oldValue
    dataCellChanged.changed = true

    // 检查是否已经存在相同original的记录
    const existingIndex = dataCellChanged.columns.findIndex(
        (item) => item.original.id === originalColumn.id
    )

    if (existingIndex !== -1) {
      // 如果存在,则合并row对象
      dataCellChanged.columns[existingIndex].row = {
        ...dataCellChanged.columns[existingIndex].row,
        [event.colDef.field]: event.newValue
      }
    }
    else {
      // 如果不存在,则创建新记录
      const column = {
        row: {
          [event.colDef.field]: event.newValue
        },
        original: originalColumn
      }
      dataCellChanged.columns.push(column)
    }

    dataCellChanged.type = 'UPDATE'
  }
}

const onSelectionChanged = () => {
  const selectedRows = gridApi.getSelectedRows()
  dataSelectedChanged.changed = true
  dataSelectedChanged.columns = selectedRows
}

const onColumnVisible = (event: { visible: any; column: { visible: any; colId: any } }) => {
  if (!event.visible) {
    configure.headers.map((column: { field: any; checked: boolean }) => {
      if (column.field === event.column.colId) {
        column.checked = false
      }
    })
  }
}

const onColumnMoved = (event: { finished: any }) => {
  if (event.finished) {
    handleRefererData(getConfigure())
  }
}

const onApplyPagination = (operator: PaginationEnum) => {
  if (configure.pagination.page) {
    if (operator === PaginationEnum.PREVIOUS) {
      configure.pagination.page--
    }
    else if (operator === PaginationEnum.NEXT) {
      configure.pagination.page++
    }
    else if (operator === PaginationEnum.FIRST) {
      configure.pagination.page = 1
    }
    else if (operator === PaginationEnum.LAST) {
      configure.pagination.page = configure.pagination.pages
    }
    onSortChanged()
  }
}

const onApplyFilter = (value: any) => {
  filterConfigure.filters = value
}

const onFilterConfigure = (show: boolean) => {
  filterConfigure.show = show
  if (!show) {
    handleRefererData(getConfigure())
  }
}

const onAddOrCloneRow = (clone: boolean) => {
  const newData = {} as any

  if (!clone) {
    originalColumns.value.forEach((column: { field: string }) => {
      newData[column.field] = null
    })
    configure.datasets.push(newData)
    newRows.value.push(newData)
  }
  else {
    dataSelectedChanged.columns.forEach((column) => {
      configure.datasets.push(column)
      newRows.value.push(column)
    })
  }
  dataCellChanged.changed = true
  dataCellChanged.type = null
  dataCellChanged.columns = newRows.value
  gridApi.setRowData(configure.datasets)
}

const visibleCellChanged = (isOpen: boolean) => {
  dataCellChanged.pending = isOpen
  if (!isOpen) {
    dataCellChanged.changed = false
    dataCellChanged.columns = []
    onRefresh()
  }
}

const visibleChanged = (isOpen: boolean) => {
  dataSelectedChanged.pending = isOpen
  dataSelectedChanged.changed = false
  if (!isOpen) {
    handleInitialize()
  }
}

const visibleContents = (show: boolean) => {
  visibleContent.show = show
}

const visibleColumns = (event: any, show: boolean) => {
  visibleColumn.show = show
  if (event) {
    const conf = getConfigure()
    conf.columns = event.map((item: string) => ({ name: item }))
    // Remove the reduced column is not selected
    originalColumns.value.filter((item: { field: string }) => !event.includes(item.field))
                   .map((item: { checked: boolean }) => {
                     item.checked = false
                   })
    // Add new Column is selected
    originalColumns.value.filter((item: { field: string }) => event.includes(item.field))
                   .map((item: { checked: boolean }) => {
                     item.checked = true
                   })
    handleRefererData(conf)
  }
  visibleColumn.columns = originalColumns.value
}

const visibleSettings = (show: boolean) => {
  visibleSetting.show = show
  if (!show) {
    handleRefererData(getConfigure())
  }
}

watch(
    () => route?.params.table,
    () => {
      configure.pagination = null as unknown as PaginationModel
      handleInitialize()
    }
)

handleInitialize()
</script>
