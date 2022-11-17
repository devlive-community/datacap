<template>
  <div>
    <a-card :bodyStyle="{padding: 0, 'min-height': 0}" :headStyle="{padding: 0}" size="small">
      <template #title>
        <a-button size="small" type="primary" @click="columnDrawerVisible = true" style="margin-right: 10px;">
          <column-width-outlined/>
        </a-button>
        <a-tooltip :title="$t('tooltip.pageShow')">
          <a-switch v-model:checked="isPage" size="small" style="margin-right: 10px;" @change="handlerChange">
            <template #checkedChildren>
              <check-outlined/>
            </template>
            <template #unCheckedChildren>
              <close-outlined/>
            </template>
          </a-switch>
        </a-tooltip>
        <a-dropdown style="margin-right: 10px;">
          <template #overlay>
            <a-menu @click="handlerExport()">
              <a-menu-item key="export_csv">
                CSV
              </a-menu-item>
            </a-menu>
          </template>
          <a-button type="primary" size="small">
            Export
            <DownloadOutlined/>
          </a-button>
        </a-dropdown>
        <a-drawer v-if="columnDrawerVisible" :title="$t('common.column')" placement="right" :closable="false" :mask-closable="false"
                  :visible="columnDrawerVisible" :getContainer="false" :headerStyle="{padding: '10px'}"
                  :keyboard="false" :style="{ position: 'absolute' }">
          <template #extra>
            <a-button size="small" shape="circle" type="primary" style="margin-right: 10px;"
                      danger @click="columnDrawerVisible = false">
              <template #icon>
                <close-circle-outlined/>
              </template>
            </a-button>
            <a-button size="small" shape="circle" type="primary" @click="handlerApplyColumn()">
              <template #icon>
                <save-outlined/>
              </template>
            </a-button>
          </template>
          <a-checkbox-group v-model:value="visibleColumns" style="width: 100%">
            <a-row>
              <a-col :span="8" v-for="columnDef in columnDefs" v-bind:key="columnDef.headerName">
                <a-checkbox :value="columnDef">{{ columnDef.headerName }}</a-checkbox>
              </a-col>
            </a-row>
          </a-checkbox-group>
        </a-drawer>
        <ag-grid-vue :key="timestamp" :style="{width: configure.width + 'px', height: configure.height + 'px', 'margin-top': '2px'}" :pagination="isPage"
                     class="ag-theme-datacap" :columnDefs="columnDefs" :rowData="configure.columns" :gridOptions="gridOptions">
        </ag-grid-vue>
      </template>
    </a-card>
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
