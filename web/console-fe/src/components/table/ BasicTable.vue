<template>
  <div>
    <a-card :bodyStyle="{padding: 0, 'min-height': 0}" size="small">
      <template #title>
        <a-button size="small" type="primary" @click="columnDrawerVisible = true" style="margin-right: 10px;">
          <column-width-outlined/>
        </a-button>
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
              <a-col :span="8" v-for="tag in configure.headers" v-bind:key="tag">
                <a-checkbox :value="tag">{{ tag }}</a-checkbox>
              </a-col>
            </a-row>
          </a-checkbox-group>
        </a-drawer>
      </template>
      <SheetComponent
        :dataCfg="dataCfg"
        :options="options"
        :showPagination="true"
        :sheetType="'table'"/>
    </a-card>
  </div>
</template>

<script lang="ts">
import {SheetComponent} from "@antv/s2-vue";
import '@antv/s2-vue/dist/style.min.css';
import {defineComponent} from "vue";
import {TableConfigure} from "@/components/table/TableConfigure";
import {S2DataConfig, S2Options} from "@antv/s2";
import {ExportToCsv} from "export-to-csv";

export default defineComponent({
  name: "BasicTableComponent",
  components: {SheetComponent},
  props: {
    configure: {
      type: TableConfigure,
      default: () => null
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  data()
  {
    return {
      dataCfg: null as S2DataConfig,
      options: null as S2Options,
      columnDrawerVisible: false,
      visibleColumns: []
    }
  },
  methods: {
    handlerInitialize()
    {
      this.visibleColumns = this.configure.headers;
      this.dataCfg = {
        fields: {
          columns: this.configure.headers
        },
        data: this.configure.columns
      };
      this.options = {
        width: this.configure.width,
        height: this.configure.height,
        showSeriesNumber: this.configure.showSeriesNumber
      };
    },
    handlerApplyColumn()
    {
      this.dataCfg.fields.columns = this.visibleColumns;
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
    }
  }
});
</script>
