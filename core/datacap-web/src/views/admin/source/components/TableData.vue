<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading"
                     style="margin-top: 150px;">
    </CircularLoading>
    <div v-else>
      <ag-grid-vue class="ag-theme-datacap"
                   style="width: 100%; min-height: 455px; height: 455px;"
                   :gridOptions="gridOptions"
                   :columnDefs="configure.headers"
                   :rowData="configure.columns"
                   :tooltipShowDelay="100">
      </ag-grid-vue>
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

export default defineComponent({
  name: "TableData",
  components: {CircularLoading, AgGridVue},
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
        columns: []
      }
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      TableService.getData(this.id, null)
        .then(response => {
          if (response.status && response.data) {
            this.configure.headers = createColumnDefs(response.data.headers, response.data.types);
            this.configure.columns = response.data.columns;
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => this.loading = false)
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
