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
          <Tooltip :content="$t('source.manager.previewPendingChanges')"
                   transfer>
            <Button size="small"
                    :disabled="!dataCellChanged.changed"
                    @click="handlerCellChangedPreview(true)">
              <FontAwesomeIcon icon="upload"/>
            </Button>
          </Tooltip>
        </Space>
        <div style="float: right;">
          <Tooltip :content="$t('common.preview')"
                   transfer>
            <Button size="small"
                    @click="handlerVisibleContent(true)">
              <FontAwesomeIcon icon="eye"/>
            </Button>
          </Tooltip>
        </div>
      </div>
      <AgGridVue class="ag-theme-datacap"
                 style="width: 100%; min-height: 460px; height: 460px;"
                 :gridOptions="gridOptions"
                 :columnDefs="configure.headers"
                 :rowData="configure.columns"
                 :tooltipShowDelay="100"
                 :sortingOrder="['desc', 'asc', null]"
                 @grid-ready="handlerGridReady"
                 @sortChanged="handlerSortChanged"
                 @cellValueChanged="handlerCellValueChanged">
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
                            :event="dataCellChanged.event"
                            :tableId="id"
                            @close="handlerCellChangedPreview(false)">
      </TableCellEditPreview>
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
import {TableFilter} from "@/model/TableFilter";
import TableCellEditPreview from "@/views/admin/source/components/TableCellEditPreview.vue";

export default defineComponent({
  name: "TableData",
  components: {TableCellEditPreview, MarkdownPreview, InputNumber, CircularLoading, AgGridVue},
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
      configure: {
        headers: [],
        columns: [],
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
        event: null
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
            this.configure.columns = response.data.columns;
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
    handlerSortChanged()
    {
      this.configure.columns = [];
      this.gridOptions.overlayNoRowsTemplate = '<span></span>';
      this.refererLoading = true;
      const columnState = this.gridColumnApi.getColumnState();
      const orders = columnState.map((column: { colId: any; sort: any; }) => ({
        column: column.colId,
        order: column.sort
      }));
      const configure: TableFilter = new TableFilter();
      configure.pagination = this.configure.pagination;
      configure.orders = orders;

      TableService.getData(this.id, configure)
        .then(response => {
          if (response.status && response.data) {
            this.configure.columns = response.data.columns;
            if (this.configure.columns.length <= 0) {
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
    handlerCellValueChanged(event)
    {
      this.dataCellChanged.changed = true;
      this.dataCellChanged.event = event;
    },
    handlerCellChangedPreview(isOpen: boolean)
    {
      this.dataCellChanged.pending = isOpen;
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
