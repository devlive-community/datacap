<template>
  <CircularLoading v-if="loading" :show="loading"/>
  <div v-else>
    <Card :title-class="'p-2'" :body-class="'p-0'">
      <template #title>
        <div class="flex space-x-5">
          <div class="space-x-1">
            <Tooltip :content="$t('source.common.firstPage')">
              <Button size="icon" class="w-6 h-6" :disabled="!configure?.pagination?.hasPreviousPage" @click="handlerApplyPagination(configure.operator.FIRST)">
                <ArrowLeftToLine :size="14"/>
              </Button>
            </Tooltip>
            <Tooltip :content="$t('source.common.previousPage')">
              <Button size="icon" class="w-6 h-6" :disabled="!configure?.pagination?.hasPreviousPage" @click="handlerApplyPagination(configure.operator.PREVIOUS)">
                <ArrowLeft :size="14"/>
              </Button>
            </Tooltip>
            <Tooltip :content="$t('source.common.nextPage')">
              <Button size="icon" class="w-6 h-6" :disabled="!configure?.pagination?.hasNextPage" @click="handlerApplyPagination(configure.operator.NEXT)">
                <ArrowRight :size="14"/>
              </Button>
            </Tooltip>
            <Tooltip :content="$t('source.common.lastPage')">
              <Button size="icon" class="w-6 h-6" :disabled="!configure.pagination.hasNextPage" @click="handlerApplyPagination(configure.operator.LAST)">
                <ArrowRightToLine :size="14"/>
              </Button>
            </Tooltip>
            <Popover>
              <PopoverTrigger as-child>
                <Button size="icon" class="w-6 h-6">
                  <Cog :size="14"/>
                </Button>
              </PopoverTrigger>
              <PopoverContent>
                <div class="grid gap-4">
                  <div class="space-y-2">
                    <h4 class="font-medium leading-none">{{ $t('source.common.jumpPage') }}</h4>
                    <p class="text-sm text-muted-foreground flex gap-2">
                      <Input type="number" v-model="configure.pagination.currentPage" min="1" :max="configure.pagination.totalPages"/>
                      <Button @click="handlerApplyPagination(configure.operator.JUMP)">
                        {{ $t('common.apply') }}
                      </Button>
                    </p>
                  </div>
                  <div class="grid gap-2">
                    <div class="text-sm text-muted-foreground flex gap-2">
                      <div>{{ $t('source.common.showPageSize') }}</div>
                      <Input type="number" v-model="configure.pagination.pageSize" min="1" :max="10000"/>
                    </div>
                  </div>
                </div>
              </PopoverContent>
            </Popover>
          </div>
          <div class="space-x-1 mt-0.5">
            <div class="text-sm text-muted-foreground flex gap-2">
              [ <span>{{ configure?.pagination?.startIndex as number + 1 }}</span> / <span>{{ configure?.pagination?.endIndex as number + 1 }}</span> ]
              <span>of</span>
              <span>{{ configure.pagination.totalRecords }}</span>
              <span>{{ $t('source.common.records') }}</span>
            </div>
          </div>
          <div class="space-x-1">
            <Tooltip :content="$t('source.common.addRows')">
              <Button size="icon" class="w-6 h-6" @click="handlerAddOrCloneRow(false)">
                <Plus :size="15"/>
              </Button>
            </Tooltip>
            <Tooltip :content="$t('source.common.copyRows')">
              <Button size="icon" class="w-6 h-6" :disabled="dataSelectedChanged.columns.length === 0" @click="handlerAddOrCloneRow(true)">
                <Copy :size="15"/>
              </Button>
            </Tooltip>
            <Tooltip :content="$t('source.common.deleteRows')">
              <Button size="icon" class="w-6 h-6" :disabled="!dataSelectedChanged.changed" @click="handlerSelectedChangedPreview(true)">
                <Minus :size="15"/>
              </Button>
            </Tooltip>
            <Tooltip :content="$t('source.common.previewPendingChanges')">
              <Button size="icon" class="w-6 h-6" :disabled="!dataCellChanged.changed && dataCellChanged.columns.length === 0" @click="handlerCellChangedPreview(true)">
                <RectangleEllipsis :size="15"/>
              </Button>
            </Tooltip>
            <Tooltip :content="$t('common.preview')">
              <Button size="icon" class="w-6 h-6" @click="handlerVisibleContent(true)">
                <Eye :size="15"/>
              </Button>
            </Tooltip>
            <Tooltip :content="$t('common.refresh')">
              <Button size="icon" class="w-6 h-6" @click="handlerRefresh">
                <RefreshCw :size="15"/>
              </Button>
            </Tooltip>
          </div>
        </div>
      </template>
      <CircularLoading v-if="refererLoading" :show="refererLoading"/>
      <AgGridVue class="ag-theme-datacap" style="width: 100%; min-height: 460px; height: 460px;" :gridOptions="gridOptions" :columnDefs="configure.headers"
                 :rowData="configure.datasets" :tooltipShowDelay="100" :sortingOrder="['desc', 'asc', null]" :rowSelection="'multiple'" @grid-ready="handlerGridReady"
                 @sortChanged="handlerSortChanged" @cellValueChanged="handlerCellValueChanged" @selectionChanged="handlerSelectionChanged" @columnVisible="handlerColumnVisible"
                 @columnMoved="handlerColumnMoved"/>
    </Card>
    <TableCellInfo v-if="dataCellChanged.pending && info" :isVisible="dataCellChanged.pending" :columns="dataCellChanged.columns" :tableId="info.applyId as number"
                   :type="dataCellChanged.type" @close="handlerCellChangedPreview(false)"/>
    <TableRowDelete v-if="dataSelectedChanged.pending && info" :isVisible="dataSelectedChanged.pending" :tableId="info.applyId as number" :columns="dataSelectedChanged.columns"
                    @close="handlerSelectedChangedPreview(false)"/>
    <SqlInfo v-if="visibleContent.show" :isVisible="visibleContent.show" :content="visibleContent.content" @close="handlerVisibleContent(false)"/>
  </div>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
import { StructureModel } from '@/model/structure'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { useI18n } from 'vue-i18n'
import { AgGridVue } from 'ag-grid-vue3'
import 'ag-grid-community/styles/ag-grid.css'
import '@/views/components/grid/ag-theme-datacap.css'
import { ColumnApi, ColumnState, GridApi } from 'ag-grid-community'
import Card from '@/views/ui/card'
import { PaginationEnum, PaginationModel } from '@/model/pagination'
import { createColumnDefs, createDataEditorOptions } from '@/views/pages/admin/source/components/TableUtils.ts'
import { OrderFilter, SqlColumn, SqlType, TableFilter } from '@/model/table'
import TableService from '@/services/table'
import { cloneDeep, toNumber } from 'lodash'
import { ToastUtils } from '@/utils/toast'
import Button from '@/views/ui/button'
import Tooltip from '@/views/ui/tooltip'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import { ArrowLeft, ArrowLeftToLine, ArrowRight, ArrowRightToLine, Cog, Copy, Eye, Minus, Plus, RectangleEllipsis, RefreshCw } from 'lucide-vue-next'
import { Input } from '@/components/ui/input'
import TableCellInfo from '@/views/pages/admin/source/components/TableCellInfo.vue'
import TableRowDelete from '@/views/pages/admin/source/components/TableRowDelete.vue'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'

export default defineComponent({
  name: 'TableData',
  components: {
    SqlInfo,
    TableRowDelete,
    TableCellInfo,
    Input,
    CircularLoading,
    Card,
    AgGridVue,
    Button,
    Tooltip,
    Popover, PopoverContent, PopoverTrigger,
    ArrowLeftToLine, ArrowLeft, ArrowRight, ArrowRightToLine, Cog, Plus, RectangleEllipsis, Copy, Minus, Eye, RefreshCw
  },
  props: {
    info: {
      type: Object as () => StructureModel | null
    }
  },
  created()
  {
    this.i18n = useI18n()
    this.handlerInitialize()
    this.watchId()
  },
  data()
  {
    return {
      i18n: null as any,
      loading: false,
      refererLoading: false,
      gridOptions: null as any,
      gridApi: null as unknown as GridApi,
      gridColumnApi: null as unknown as ColumnApi,
      originalColumns: [] as any[],
      originalDatasets: [] as any[],
      newRows: [] as any[],
      configure: {
        headers: [] as any[],
        columns: [] as any[],
        datasets: [] as any[],
        pagination: null as unknown as PaginationModel,
        operator: PaginationEnum
      },
      visibleContent: {
        show: false,
        content: null as unknown as string
      },
      dataCellChanged: {
        changed: false,
        pending: false,
        type: null as unknown as SqlType,
        columns: [] as any[]
      },
      dataSelectedChanged: {
        changed: false,
        pending: false,
        columns: [] as any[]
      },
      visibleColumn: {
        show: false,
        columns: [] as any[]
      },
      filterConfigure: {
        show: false,
        columns: [] as any[],
        types: [] as any[],
        configure: {
          condition: 'AND',
          filters: [] as any[]
        }
      }
    }
  },
  methods: {
    handlerInitialize()
    {
      this.clearData()
      this.gridOptions = createDataEditorOptions(this.i18n)
      if (!this.configure.pagination) {
        const pagination: PaginationModel = {
          currentPage: 1,
          pageSize: 500
        }
        this.configure.pagination = pagination
      }
      if (this.info) {
        this.loading = true
        TableService.getData(toNumber(this.info.applyId), this.configure)
                    .then(response => {
                      if (response.status && response.data) {
                        this.configure.headers = createColumnDefs(response.data.headers, response.data.types)
                        this.originalColumns = this.configure.headers
                        this.configure.datasets = response.data.columns
                        this.originalDatasets = cloneDeep(response.data.columns)
                        this.configure.pagination = response.data.pagination
                        this.visibleContent.content = response.data.content
                        this.filterConfigure.columns = cloneDeep(response.data.headers)
                        this.filterConfigure.types = cloneDeep(response.data.types)
                      }
                      else {
                        ToastUtils.error(response.message)
                      }
                    })
                    .finally(() => this.loading = false)
      }
    },
    handlerGridReady(params: { api: GridApi; columnApi: ColumnApi; })
    {
      this.gridApi = params.api
      this.gridColumnApi = params.columnApi
    },
    handlerRefererData(configure: TableFilter)
    {
      this.configure.datasets = []
      this.gridOptions.overlayNoRowsTemplate = '<span></span>'
      if (this.info) {
        this.refererLoading = true
        TableService.getData(toNumber(this.info.applyId), configure)
                    .then(response => {
                      if (response.status && response.data) {
                        this.configure.headers = createColumnDefs(response.data.headers, response.data.types)
                        this.configure.datasets = response.data.columns
                        this.originalDatasets = cloneDeep(response.data.columns)
                        if (this.configure?.datasets?.length === 0) {
                          this.gridOptions.overlayNoRowsTemplate = '<span>No Rows To Show</span>'
                        }
                        this.configure.pagination = response.data.pagination
                        this.visibleContent.content = response.data.content
                        this.filterConfigure.columns = cloneDeep(response.data.headers)
                        this.filterConfigure.types = cloneDeep(response.data.types)
                      }
                      else {
                        this.$Message.error(response.message)
                      }
                    })
                    .finally(() => this.refererLoading = false)
      }
    },
    handlerSortChanged()
    {
      this.handlerRefererData(this.getConfigure())
    },
    handlerCellValueChanged(event: { data: any; colDef: { field: string; }; oldValue: any; newValue: any; rowIndex: number })
    {
      // If the index is less than or equal to the length of the current data collection -1, no request type is specified for the new data
      if (event.rowIndex <= this.originalDatasets.length - 1) {
        const oldColumn = event.data
        const originalColumn = cloneDeep(oldColumn)
        originalColumn[event.colDef.field] = event.oldValue
        this.dataCellChanged.changed = true
        const column: SqlColumn = {
          column: event.colDef.field,
          value: event.newValue,
          original: originalColumn
        }
        this.dataCellChanged.type = SqlType.UPDATE
        this.dataCellChanged.columns.push(column)
      }
    },
    handlerCellChangedPreview(isOpen: boolean)
    {
      this.dataCellChanged.pending = isOpen
      if (!isOpen) {
        this.dataCellChanged.changed = false
        this.dataCellChanged.columns = []
      }
    },
    handlerSelectionChanged()
    {
      const selectedRows = this.gridApi.getSelectedRows()
      this.dataSelectedChanged.changed = true
      this.dataSelectedChanged.columns = selectedRows
    },
    handlerSelectedChangedPreview(isOpen: boolean)
    {
      this.dataSelectedChanged.pending = isOpen
      this.dataSelectedChanged.changed = false
      if (!isOpen) {
        this.handlerInitialize()
      }
    },
    handlerApplyPagination(operator: PaginationEnum)
    {
      if (this.configure.pagination.currentPage) {
        if (operator === PaginationEnum.PREVIOUS) {
          this.configure.pagination.currentPage--
        }
        else if (operator === PaginationEnum.NEXT) {
          this.configure.pagination.currentPage++
        }
        else if (operator === PaginationEnum.FIRST) {
          this.configure.pagination.currentPage = 1
        }
        else if (operator === PaginationEnum.LAST) {
          if (this.configure.pagination.totalPages) {
            this.configure.pagination.currentPage = this.configure.pagination.totalPages
          }
        }
        this.handlerSortChanged()
      }
    },
    handlerVisibleContent(show: boolean)
    {
      this.visibleContent.show = show
    },
    handlerColumnVisible(event: { visible: any; column: { visible: any; colId: any; }; })
    {
      if (!event.visible) {
        this.configure.headers.map((column: { field: any; checked: boolean; }) => {
          if (column.field === event.column.colId) {
            column.checked = false
          }
        })
      }
    },
    handlerVisibleColumn(event: any, show: boolean)
    {
      this.visibleColumn.show = show
      if (event) {
        const configure: TableFilter = this.getConfigure()
        const columns = event.map((item: string) => ({ column: item }))
        configure.columns = columns
        // Remove the reduced column is not selected
        this.originalColumns.filter((item: { field: string; }) => !event.includes(item.field))
            .map((item: { checked: boolean; }) => {
              item.checked = false
            })
        // Add new Column is selected
        this.originalColumns.filter((item: { field: string; }) => event.includes(item.field))
            .map((item: { checked: boolean; }) => {
              item.checked = true
            })
        this.handlerRefererData(configure)
      }
      this.visibleColumn.columns = this.originalColumns
    },
    handlerColumnMoved(event: { finished: any; })
    {
      if (event.finished) {
        this.handlerRefererData(this.getConfigure())
      }
    },
    handlerFilterConfigure(show: boolean)
    {
      this.filterConfigure.show = show
      if (!show) {
        this.handlerRefererData(this.getConfigure())
      }
    },
    handlerApplyFilter(value: any)
    {
      this.filterConfigure.configure = value
    },
    handlerAddOrCloneRow(clone: boolean)
    {
      const newData = {} as any
      if (!clone) {
        this.originalColumns.forEach((column: { field: string; }) => {
          newData[column.field] = null
        })
        this.configure.datasets.push(newData)
        this.newRows.push(newData)
      }
      else {
        this.dataSelectedChanged.columns.forEach(column => {
          this.configure.datasets.push(column)
          this.newRows.push(column)
        })
      }
      this.dataCellChanged.type = SqlType.INSERT
      this.dataCellChanged.changed = true
      this.dataCellChanged.columns = this.newRows
      this.gridApi.setRowData(this.configure.datasets)
    },
    handlerRefresh()
    {
      this.handlerRefererData(this.getConfigure())
    },
    getSortConfigure(configure: TableFilter)
    {
      const columnState = this.gridColumnApi.getColumnState()
      const orders = columnState.map((column: ColumnState) => ({
        column: column.colId,
        order: column.sort
      }))
      configure.pagination = this.configure.pagination
      configure.orders = orders as OrderFilter[]
    },
    getVisibleColumn(configure: TableFilter)
    {
      const columns = this.gridColumnApi.getColumnState()
                          .filter(item => !item.hide)
                          .map((item: { colId: any; }) => ({ column: item.colId }))
      configure.columns = columns
    },
    getConfigure(): TableFilter
    {
      this.clearData()
      const configure: TableFilter = {
        filter: this.filterConfigure.configure
      }
      this.getSortConfigure(configure)
      this.getVisibleColumn(configure)
      return configure
    },
    clearData()
    {
      this.newRows = []
    },
    watchId()
    {
      watch(
          () => this.info,
          () => {
            this.configure.pagination = null as unknown as PaginationModel
            this.handlerInitialize()
          }
      )
    }
  }
})
</script>
