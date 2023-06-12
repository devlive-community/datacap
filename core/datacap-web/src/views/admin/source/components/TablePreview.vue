<template>
  <div>
    <ag-grid-vue class="ag-theme-datacap"
                 :style="{width: configure.width + 'px', height: configure.height + 'px', 'margin-top': '2px'}"
                 :columnDefs="columnDefs"
                 :gridOptions="gridOptions"
                 :rowData="configure.columns"
                 :tooltipShowDelay="100"
                 :multiSortKey="'ctrl'"
                 :rowSelection="'multiple'"
                 @sortChanged="handlerSortChanged"
                 @selectionChanged="handlerSelectionChanged">
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
      columnDefs: []
    }
  },
  methods: {
    handlerInitialize()
    {
      this.columnDefs = [];
      // Build a select box for multiple selection operations
      if (this.configure.headers.length > 0) {
        this.columnDefs.push({
          width: 38,
          headerCheckboxSelection: true,
          checkboxSelection: true,
          showDisabledCheckboxes: true,
          lockPinned: true,
          pinned: 'left'
        })
      }
      // Render backend returns data
      this.configure.headers.forEach((header, index) => {
        const hasSort = this.sortColumns?.filter(sortColumn => sortColumn.column === header)[0];
        const columnDef = {
          headerName: header,
          field: header,
          sort: hasSort ? hasSort.sort : null,
          headerTooltip: header + ' [' + this.configure.types[index] + ']',
          comparator: () => {
            if (hasSort && hasSort.sort === 'asc') {
              return 0;
            }
            return hasSort && hasSort.sort === 'desc' ? -1 : 1;
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
    },
    handlerSelectionChanged(event)
    {
      const selectedRows = event.api.getSelectedRows();
      this.$emit('on-selected', selectedRows);
    }
  }
});
</script>
