<template>
  <div>
    <ag-grid-vue class="ag-theme-datacap" :style="{width: configure.width + 'px', height: configure.height + 'px', 'margin-top': '2px'}"
                 :columnDefs="columnDefs" :rowData="configure.columns" :gridOptions="gridOptions">
    </ag-grid-vue>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {TableConfigure} from "@/components/table/TableConfigure";
import {AgGridVue} from "ag-grid-vue3";
import "ag-grid-community/styles/ag-grid.css";
import "@/components/table/ag-theme-datacap.css";
import {useI18n} from "vue-i18n";
import {TableColumnDef} from "@/components/table/TableColumnDef";
import TableGridOptions from "@/components/table/TableGridOptions";

export default defineComponent({
  name: "TablePreview",
  components: {AgGridVue},
  props: {
    configure: {
      type: TableConfigure,
      default: () => null
    }
  },
  setup()
  {
    const i18n = useI18n();
    const gridOptions = TableGridOptions.createDefaultOptions(i18n);
    return {
      gridOptions
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  data()
  {
    return {
      columnDefs: [],
      isPage: true
    }
  },
  methods: {
    handlerInitialize()
    {
      this.columnDefs = [];
      this.configure.headers.forEach(header => {
        const columnDef: TableColumnDef = {
          headerName: header,
          field: header
        };
        this.columnDefs.push(columnDef)
      });
    }
  }
});
</script>
