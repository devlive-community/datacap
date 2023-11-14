<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading"
                     style="margin-top: 150px;">
    </CircularLoading>
    <div v-else>
      <div style="margin-top: -12px; margin-bottom: 5px;">
        <ButtonGroup size="small">
          <Button :disabled="!configure.pagination.hasPreviousPage"
                  @click="handlerApplyPagination(configure.operator.FIRST)">
            <Tooltip :content="$t('common.firstPage')"
                     transfer>
              <FontAwesomeIcon icon="angles-left"/>
            </Tooltip>
          </Button>
          <Button :disabled="!configure.pagination.hasPreviousPage"
                  @click="handlerApplyPagination(configure.operator.PREVIOUS)">
            <Tooltip :content="$t('common.previousPage')"
                     transfer>
              <FontAwesomeIcon icon="arrow-left"/>
            </Tooltip>
          </Button>
          <Button :disabled="!configure.pagination.hasNextPage"
                  @click="handlerApplyPagination(configure.operator.NEXT)">
            <Tooltip :content="$t('common.nextPage')"
                     transfer>
              <FontAwesomeIcon icon="arrow-right"/>
            </Tooltip>
          </Button>
          <Button :disabled="!configure.pagination.hasNextPage"
                  @click="handlerApplyPagination(configure.operator.LAST)">
            <Tooltip :content="$t('common.lastPage')"
                     transfer>
              <FontAwesomeIcon icon="angles-right"/>
            </Tooltip>
          </Button>
          <Poptip placement="bottom"
                  style="margin-left: 10px;"
                  transfer>
            <Button size="small">
              <FontAwesomeIcon icon="gear"/>
            </Button>
            <template #title>
              <Space>
                {{ $t('common.jumpPage') }}
                <InputNumber v-model="configure.pagination.currentPage"
                             size="small"
                             min="1"
                             :max="configure.pagination.totalPages">
                </InputNumber>
                <Button size="small"
                        @click="handlerApplyPagination(configure.operator.JUMP)">
                  {{ $t('common.apply') }}
                </Button>
              </Space>
            </template>
            <template #content>
              <Space>
                {{ $t('common.showPageSize') }}
                <InputNumber v-model="configure.pagination.pageSize"
                             size="small"
                             min="1"
                             :max="10000">
                </InputNumber>
              </Space>
            </template>
          </Poptip>
        </ButtonGroup>
        <Space style="margin-left: 10px;">
          [
          <Text strong> {{ configure.pagination.startIndex + 1 }} / {{ configure.pagination.endIndex + 1 }}</Text>
          ]
          of
          <Text strong>{{ configure.pagination.totalRecords }}</Text>
          {{ $t('common.row') }}
        </Space>
        <Space style="margin-left: 30px;">
          <Tooltip :content="$t('source.manager.deleteRows')"
                   transfer>
            <Button size="small"
                    shape="circle"
                    :disabled="!dataSelectedChanged.changed"
                    @click="handlerSelectedChangedPreview(true)">
              <FontAwesomeIcon icon="minus"/>
            </Button>
          </Tooltip>
          <Tooltip :content="$t('source.manager.previewPendingChanges')"
                   transfer>
            <Button size="small"
                    shape="circle"
                    :disabled="!dataCellChanged.changed && dataCellChanged.columns.length === 0"
                    @click="handlerCellChangedPreview(true)">
              <FontAwesomeIcon icon="upload"/>
            </Button>
          </Tooltip>
          <Tooltip :content="$t('common.preview')"
                   transfer>
            <Button size="small"
                    @click="handlerVisibleContent(true)">
              <FontAwesomeIcon icon="eye"/>
            </Button>
          </Tooltip>
        </Space>
        <div style="float: right;">
          <Space>
            <Tooltip :content="$t('source.manager.filter')"
                     placement="bottom-end"
                     transfer>
              <Button size="small"
                      @click="handlerFilterConfigure(true)">
                <FontAwesomeIcon icon="filter"/>
              </Button>
            </Tooltip>
            <Tooltip :content="$t('source.manager.visibleColumn')"
                     placement="bottom-end"
                     transfer>
              <Button size="small"
                      @click="handlerVisibleColumn(null, true)">
                <FontAwesomeIcon icon="columns"/>
              </Button>
            </Tooltip>
          </Space>
        </div>
      </div>
      <AgGridVue class="ag-theme-datacap"
                 style="width: 100%; min-height: 460px; height: 460px;"
                 :gridOptions="gridOptions"
                 :columnDefs="configure.headers"
                 :rowData="configure.datasets"
                 :tooltipShowDelay="100"
                 :sortingOrder="['desc', 'asc', null]"
                 :rowSelection="'multiple'"
                 @grid-ready="handlerGridReady"
                 @sortChanged="handlerSortChanged"
                 @cellValueChanged="handlerCellValueChanged"
                 @selectionChanged="handlerSelectionChanged"
                 @columnVisible="handlerColumnVisible"
                 @columnMoved="handlerColumnMoved">
      </AgGridVue>
      <CircularLoading v-if="refererLoading"
                       :show="refererLoading">
      </CircularLoading>

      <MarkdownPreview v-if="visibleContent.show"
                       :isVisible="visibleContent.show"
                       :content="visibleContent.content"
                       @close="handlerVisibleContent(false)">
      </MarkdownPreview>
      <!-- Preview components after data editing -->
      <TableCellEditPreview v-if="dataCellChanged.pending"
                            :isVisible="dataCellChanged.pending"
                            :columns="dataCellChanged.columns"
                            :tableId="id"
                            @close="handlerCellChangedPreview(false)">
      </TableCellEditPreview>
      <!-- Preview components after data deleting -->
      <TableRowDeletePreview v-if="dataSelectedChanged.pending"
                             :isVisible="dataSelectedChanged.pending"
                             :tableId="id"
                             :columns="dataSelectedChanged.columns"
                             @close="handlerSelectedChangedPreview(false)">
      </TableRowDeletePreview>
      <!-- Displays the currently selected and unchecked columns -->
      <TableColumn v-if="visibleColumn.show"
                   :isVisible="visibleColumn.show"
                   :columns="visibleColumn.columns"
                   @close="handlerVisibleColumn($event, false)"
                   @onClose="handlerVisibleColumn($event, false)">
      </TableColumn>
      <TableColumnFilter v-if="filterConfigure.show"
                         :isVisible="filterConfigure.show"
                         @close="handlerFilterConfigure(false)">
      </TableColumnFilter>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import {AgGridVue} from "ag-grid-vue3";
import "ag-grid-community/styles/ag-grid.css";
import "@/components/table/ag-theme-datacap.css";
import TableService from "@/services/Table";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {createColumnDefs, createDataEditorOptions} from "@/views/admin/source/components/TableDataFunction";
import {useI18n} from "vue-i18n";
import {Pagination} from "@/entity/Pagination";
import {Pagination as PaginationEnum} from "@/enum/Pagination";
import {InputNumber} from "view-ui-plus";
import MarkdownPreview from "@/components/common/MarkdownPreview.vue";
import {ColumnApi, GridApi} from "ag-grid-community";
import {SqlColumn, TableFilter} from "@/model/TableFilter";
import TableCellEditPreview from "@/views/admin/source/components/TableCellEditPreview.vue";
import TableRowDeletePreview from "@/views/admin/source/components/TableRowDeletePreview.vue";
import {cloneDeep} from "lodash";
import TableColumn from "@/views/admin/source/components/TableColumn.vue";
import TableColumnFilter from "@/views/admin/source/components/TableColumnFilter.vue";

export default defineComponent({
  name: "TableData",
  components: {TableColumnFilter, TableColumn, TableRowDeletePreview, TableCellEditPreview, MarkdownPreview, InputNumber, CircularLoading, AgGridVue},
  props: {
    id: {
      type: Number,
      default: 0
    }
  },
  created()
  {
    this.i18n = useI18n();
    this.handlerInitialize();
    this.watchId();
  },
  data()
  {
    return {
      i18n: null,
      loading: false,
      refererLoading: false,
      gridOptions: null,
      gridApi: null as GridApi,
      gridColumnApi: null as ColumnApi,
      originalColumns: [],
      configure: {
        headers: [],
        columns: [],
        datasets: [],
        pagination: null as Pagination,
        operator: PaginationEnum
      },
      visibleContent: {
        show: false,
        content: null
      },
      dataCellChanged: {
        changed: false,
        pending: false,
        columns: []
      },
      dataSelectedChanged: {
        changed: false,
        pending: false,
        columns: []
      },
      visibleColumn: {
        show: false,
        columns: []
      },
      filterConfigure: {
        show: false
      }
    }
  },
  methods: {
    handlerInitialize()
    {
      this.gridOptions = createDataEditorOptions(this.i18n);
      if (!this.configure.pagination) {
        const pagination = new Pagination();
        pagination.currentPage = 1;
        pagination.pageSize = 500;
        this.configure.pagination = pagination;
      }
      this.loading = true;
      TableService.getData(this.id, this.configure)
        .then(response => {
          if (response.status && response.data) {
            this.configure.headers = createColumnDefs(response.data.headers, response.data.types);
            this.originalColumns = this.configure.headers
            this.configure.datasets = response.data.columns;
            this.configure.pagination = response.data.pagination;
            this.visibleContent.content = '```sql\n' + response.data.content + '\n```';
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => this.loading = false)
    },
    handlerGridReady(params: { api: GridApi; columnApi: ColumnApi; })
    {
      this.gridApi = params.api;
      this.gridColumnApi = params.columnApi;
    },
    handlerRefererData(configure: TableFilter)
    {
      this.configure.datasets = [];
      this.gridOptions.overlayNoRowsTemplate = '<span></span>';
      this.refererLoading = true;
      TableService.getData(this.id, configure)
        .then(response => {
          if (response.status && response.data) {
            this.configure.headers = createColumnDefs(response.data.headers, response.data.types);
            this.configure.datasets = response.data.columns;
            if (this.configure.datasets.length <= 0) {
              this.gridOptions.overlayNoRowsTemplate = '<span>No Rows To Show</span>';
            }
            this.configure.pagination = response.data.pagination;
            this.visibleContent.content = '```sql\n' + response.data.content + '\n```';
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => this.refererLoading = false)
    },
    handlerSortChanged()
    {
      const configure: TableFilter = new TableFilter();
      this.getSortConfigure(configure)
      this.handlerRefererData(configure)
    },
    handlerCellValueChanged(event: { data: any; colDef: { field: string; }; oldValue: any; newValue: any; })
    {
      const oldColumn = event.data;
      const originalColumn = cloneDeep(oldColumn);
      originalColumn[event.colDef.field] = event.oldValue;
      this.dataCellChanged.changed = true;
      const column: SqlColumn = {
        column: event.colDef.field,
        value: event.newValue,
        original: originalColumn
      }
      this.dataCellChanged.columns.push(column);
    },
    handlerCellChangedPreview(isOpen: boolean)
    {
      this.dataCellChanged.pending = isOpen;
      if (!isOpen) {
        this.dataCellChanged.changed = false;
        this.dataCellChanged.columns = [];
      }
    },
    handlerSelectionChanged()
    {
      const selectedRows = this.gridApi.getSelectedRows();
      this.dataSelectedChanged.changed = true;
      this.dataSelectedChanged.columns = selectedRows;
    },
    handlerSelectedChangedPreview(isOpen: boolean)
    {
      this.dataSelectedChanged.pending = isOpen;
      this.dataSelectedChanged.changed = false;
      if (!isOpen) {
        this.handlerInitialize();
      }
    },
    handlerApplyPagination(operator: PaginationEnum)
    {
      if (operator === PaginationEnum.PREVIOUS) {
        this.configure.pagination.currentPage--;
      }
      else if (operator === PaginationEnum.NEXT) {
        this.configure.pagination.currentPage++;
      }
      else if (operator === PaginationEnum.FIRST) {
        this.configure.pagination.currentPage = 1;
      }
      else if (operator === PaginationEnum.LAST) {
        this.configure.pagination.currentPage = this.configure.pagination.totalPages;
      }
      this.handlerSortChanged();
    },
    handlerVisibleContent(show: boolean)
    {
      this.visibleContent.show = show;
    },
    handlerColumnVisible(event: { visible: any; column: { visible: any; colId: any; }; })
    {
      if (!event.visible) {
        this.configure.headers.map((column: { field: any; checked: boolean; }) => {
          if (column.field === event.column.colId) {
            column.checked = false;
          }
        })
      }
    },
    handlerVisibleColumn(event, show: boolean)
    {
      this.visibleColumn.show = show;
      if (event) {
        const configure: TableFilter = new TableFilter()
        this.getSortConfigure(configure)
        this.getVisibleColumn(configure)
        const columns = event.map((item: string) => ({column: item}))
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
        const configure: TableFilter = new TableFilter()
        this.getSortConfigure(configure)
        this.getVisibleColumn(configure)
        this.handlerRefererData(configure)
      }
    },
    handlerFilterConfigure(show: boolean)
    {
      this.filterConfigure.show = show;
    },
    getSortConfigure(configure: TableFilter)
    {
      const columnState = this.gridColumnApi.getColumnState();
      const orders = columnState.map((column: { colId: any; sort: any; }) => ({
        column: column.colId,
        order: column.sort
      }));
      configure.pagination = this.configure.pagination;
      configure.orders = orders;
    },
    getVisibleColumn(configure: TableFilter)
    {
      const columns = this.gridColumnApi.getColumnState()
        .filter(item => !item.hide)
        .map((item: { colId: any; }) => ({column: item.colId}))
      configure.columns = columns
    },
    watchId()
    {
      watch(
        () => this.id,
        () => {
          this.configure.pagination = null as Pagination;
          this.handlerInitialize();
        }
      );
    }
  }
});
</script>
