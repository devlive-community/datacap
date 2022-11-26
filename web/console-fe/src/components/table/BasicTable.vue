<template>
  <div>
    <Card :padding="5">
      <Button size="small" type="primary" @click="columnDrawerVisible = true" icon="md-list" style="margin-right: 10px;"/>
      <Tooltip :content="$t('tooltip.pageShow')">
        <Switch v-model="isPage" size="small" style="margin-right: 10px;" @change="handlerChange">
          <template #open>
            <Icon type="md-check"></Icon>
          </template>
          <template #close>
            <Icon type="md-close"></Icon>
          </template>
        </Switch>
      </Tooltip>
      <Dropdown style="margin-right: 10px;">
        <Button type="primary" size="small">
          Export
          <Icon type="md-download"/>
        </Button>
        <template #list>
          <DropdownMenu>
            <DropdownItem @click="handlerExport()">CSV</DropdownItem>
          </DropdownMenu>
        </template>
      </Dropdown>
      <ag-grid-vue :key="timestamp" :style="{width: configure.width + 'px', height: configure.height + 'px', 'margin-top': '2px'}" :pagination="isPage"
                   class="ag-theme-datacap" :columnDefs="columnDefs" :rowData="configure.columns" :gridOptions="gridOptions">
      </ag-grid-vue>
      <Drawer v-if="columnDrawerVisible" :title="$t('common.column')" placement="right" :mask-closable="false"
              v-model="columnDrawerVisible" :style="{ position: 'absolute' }">
        <template #close>
          <Button size="small" shape="circle" type="error" style="margin-right: 10px;" icon="md-close" @click="columnDrawerVisible = false"/>
          <Button size="small" shape="circle" type="primary" icon="md-document" @click="handlerApplyColumn()"/>
        </template>
        <CheckboxGroup v-model="visibleColumns" style="width: 100%">
          <Checkbox v-for="columnDef in columnDefs" v-bind:key="columnDef.headerName" :label="columnDef">
            {{ columnDef.headerName }}
          </Checkbox>
        </CheckboxGroup>
      </Drawer>
    </Card>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {TableConfigure} from "@/components/table/TableConfigure";
import {ExportToCsv} from "export-to-csv";
import {AgGridVue} from "ag-grid-vue3";
import "ag-grid-community/styles/ag-grid.css";
import "./ag-theme-datacap.css";
import {createDefaultOptions} from "./TableGridOptions";
import {useI18n} from "vue-i18n";
import {TableColumnDef} from "@/components/table/TableColumnDef";
import {getTimestamp} from "@/common/DateCommon";

export default defineComponent({
  name: "BasicTableComponent",
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
    const gridOptions = createDefaultOptions(i18n);
    const timestamp = getTimestamp();
    return {
      gridOptions,
      timestamp
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  data()
  {
    return {
      columnDrawerVisible: false,
      visibleColumns: [],
      columnDefs: [],
      isPage: true
    }
  },
  methods: {
    handlerInitialize()
    {
      this.configure.headers.forEach(header => {
        const columnDef: TableColumnDef = {
          headerName: header,
          field: header
        };
        this.columnDefs.push(columnDef)
      });
      this.visibleColumns = this.columnDefs;
    },
    handlerApplyColumn()
    {
      this.columnDefs = this.visibleColumns;
      this.columnDrawerVisible = false;
    },
    handlerExport()
    {
      const options = {
        fieldSeparator: ',',
        quoteStrings: '',
        decimalSeparator: '.',
        showLabels: true,
        showTitle: false,
        useTextFile: false,
        useBom: true,
        filename: Date.parse(new Date().toString()).toString(),
        useKeysAsHeaders: true
      };
      const csvExporter = new ExportToCsv(options);
      csvExporter.generateCsv(this.configure.columns);
    },
    handlerChange()
    {
      this.timestamp = getTimestamp();
    }
  }
});
</script>
