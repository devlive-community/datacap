<template>
  <div>
    <ag-grid-vue class="ag-theme-datacap"
                 :style="{width: configure.width + 'px', height: configure.height + 'px', 'margin-top': '2px'}"
                 :columnDefs="columnDefs"
                 :gridOptions="gridOptions"
                 :multiSortKey="multiSortKey"
                 :rowData="configure.columns"
                 :tooltipShowDelay="tooltipShowDelay"
                 @sortChanged="handlerSortChanged">
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
import TableGridOptions from "@/components/table/TableGridOptions";
import {Column} from "ag-grid-community";
import {isEmpty} from "lodash";
import {Sort} from "@/model/sql/Sort";

export default defineComponent({
  name: "TablePreview",
  components: {AgGridVue},
  props: {
    configure: {
      type: TableConfigure,
      default: () => null
    },
    sortColumns: {
      type: Array,
      default: () => []
    }
  },
  created()
  {
    const i18n = useI18n();
    this.gridOptions = TableGridOptions.createDataEditorOptions(i18n);
    this.handlerInitialize();
  },
  data()
  {
    return {
      gridOptions: null,
      multiSortKey: 'ctrl',
      tooltipShowDelay: 100,
      columnDefs: []
    }
  },
  methods: {
    handlerInitialize()
    {
      this.columnDefs = [];
      this.configure.headers.forEach((header, index) => {
        const hasSort = this.sortColumns?.filter(sortColumn => sortColumn.column === header)[0];
        const columnDef = {
          headerName: header,
          field: header,
          sort: hasSort ? hasSort.sort : null,
          headerTooltip: header + ' [' + this.configure.types[index] + ']',
          noSort: true,
          comparator: () => {
            if (hasSort) {
              if (hasSort.sort === 'asc') {
                return 0;
              }
              else if (hasSort.sort === 'desc') {
                return -1;
              }
            }
            return 1;
          }
        };
        this.columnDefs.push(columnDef)
      });
    },
    handlerSortChanged(event)
    {
      this.ready = true;
      if (this.ready) {
        const columns: Column[] = event.columnApi.getAllGridColumns();
        // Get all order columns
        const orderColumns: Column[] = columns.filter(column => !isEmpty(column.getSort()));
        const sort: Array<Sort> = new Array<Sort>();
        orderColumns.forEach(column => {
          sort.push({
            column: column.getColId(),
            sort: column.getSort()
          });
        });
        this.$emit('on-sorted', sort);
      }
    }
  }
});
</script>
