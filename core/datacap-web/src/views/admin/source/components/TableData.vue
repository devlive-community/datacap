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
        <div style="float: right;">
          <Button size="small"
                  @click="handlerVisibleContent(true)">
            <FontAwesomeIcon icon="eye"/>
          </Button>
        </div>
      </div>
      <AgGridVue class="ag-theme-datacap"
                 style="width: 100%; min-height: 460px; height: 460px;"
                 :gridOptions="gridOptions"
                 :columnDefs="configure.headers"
                 :rowData="configure.columns"
                 :tooltipShowDelay="100">
      </AgGridVue>

      <MarkdownPreview v-if="visibleContent.show"
                       :isVisible="visibleContent.show"
                       :content="visibleContent.content"
                       @close="handlerVisibleContent(false)">
      </MarkdownPreview>
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
import {createColumnDefs} from "@/views/admin/source/components/TableDataFunction";
import {useI18n} from "vue-i18n";
import TableGridOptions from "@/components/table/TableGridOptions";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {Pagination} from "@/entity/Pagination";
import {Pagination as PaginationEnum} from "@/enum/Pagination";
import {InputNumber} from "view-ui-plus";
import MarkdownPreview from "@/components/common/MarkdownPreview.vue";

export default defineComponent({
  name: "TableData",
  components: {MarkdownPreview, InputNumber, FontAwesomeIcon, CircularLoading, AgGridVue},
  props: {
    id: {
      type: Number,
      default: 0
    }
  },
  created()
  {
    const i18n = useI18n();
    this.gridOptions = TableGridOptions.createDataEditorOptions(i18n);
    this.handlerInitialize();
    this.watchId();
  },
  data()
  {
    return {
      loading: false,
      gridOptions: null,
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
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      TableService.getData(this.id, this.configure.pagination)
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
      this.handlerInitialize();
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
