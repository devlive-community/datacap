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
                  @click="handlerApplyPagination(true)">
            <Tooltip :content="$t('common.previousPage')"
                     transfer>
              <FontAwesomeIcon icon="arrow-left"/>
            </Tooltip>
          </Button>
          <Button :disabled="!configure.pagination.hasNextPage"
                  @click="handlerApplyPagination(false)">
            <Tooltip :content="$t('common.nextPage')"
                     transfer>
              <FontAwesomeIcon icon="arrow-right"/>
            </Tooltip>
          </Button>
        </ButtonGroup>
      </div>
      <AgGridVue class="ag-theme-datacap"
                 style="width: 100%; min-height: 460px; height: 460px;"
                 :gridOptions="gridOptions"
                 :columnDefs="configure.headers"
                 :rowData="configure.columns"
                 :tooltipShowDelay="100">
      </AgGridVue>
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

export default defineComponent({
  name: "TableData",
  components: {FontAwesomeIcon, CircularLoading, AgGridVue},
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
        pagination: null as Pagination
      }
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
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => this.loading = false)
    },
    handlerApplyPagination(isPrevious: boolean)
    {
      if (isPrevious) {
        this.configure.pagination.currentPage--;
      }
      else {
        this.configure.pagination.currentPage++;
      }
      this.handlerInitialize();
    },
    watchId()
    {
      watch(
        () => this.id,
        () => {
          this.handlerInitialize();
        }
      );
    }
  }
});
</script>
