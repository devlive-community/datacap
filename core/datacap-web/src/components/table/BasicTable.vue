<template>
  <div>
    <Card :padding="5" dis-hover>
      <Space>
        <Button size="small" type="primary" @click="columnDrawerVisible = true" icon="md-list"/>
        <Tooltip :content="$t('tooltip.pageShow')">
          <Switch v-model="isPage" size="small" @on-change="handlerChange">
            <template #open>
              <Icon type="md-check"></Icon>
            </template>
            <template #close>
              <Icon type="md-close"></Icon>
            </template>
          </Switch>
        </Tooltip>
        <Dropdown>
          <Button type="primary" size="small">
            {{ $t('common.export') }}
            <Icon type="md-download"/>
          </Button>
          <template #list>
            <DropdownMenu>
              <DropdownItem @click="handlerExport()">CSV</DropdownItem>
            </DropdownMenu>
          </template>
        </Dropdown>
        <Tooltip :content="$t('common.visualization')">
          <Button size="small" icon="md-pie" @click="handlerVisualization(true)">
          </Button>
        </Tooltip>
        <Button type="primary"
                size="small"
                icon="md-add"
                to="/admin/dataset/info">
          {{ $t('common.dataset') }}
        </Button>
        <Poptip trigger="hover" placement="bottom" word-wrap width="150">
          <Button size="small" shape="circle" type="warning" icon="md-help"/>
          <template #content>
            {{ $t('tooltip.smallTips') }}
          </template>
        </Poptip>
        <RadioGroup v-model="type"
                    type="button"
                    size="small">
          <Radio label="table">
            {{ $t('common.table') }}
          </Radio>
          <Radio label="text">
            {{ $t('common.text') }}
          </Radio>
        </RadioGroup>
      </Space>
      <ag-grid-vue v-if="type === 'table'"
                   :key="timestamp"
                   :style="{width: configure.width + 'px', height: configure.height + 'px', 'margin-top': '2px'}"
                   :pagination="isPage"
                   class="ag-theme-datacap"
                   :columnDefs="columnDefs"
                   :rowData="configure.columns"
                   :gridOptions="gridOptions">
      </ag-grid-vue>
      <TextTable v-if="type === 'text'"
                 :configure="configure">
      </TextTable>
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
      <EchartsEditor v-if="visualizationDrawerVisible"
                     :isVisible="visualizationDrawerVisible"
                     :configure="visualizationConfigure"
                     :source-id="configure.sourceId"
                     :query="configure.query"
                     @close="handlerVisualization(false)">
      </EchartsEditor>
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
import {useI18n} from "vue-i18n";
import {TableColumnDef} from "@/components/table/TableColumnDef";
import {getTimestamp} from "@/common/DateCommon";
import EchartsEditor from "@/components/editor/echarts/EchartsEditor.vue";
import {EchartsConfigure} from "@/components/editor/echarts/EchartsConfigure";
import TableGridOptions from "@/components/table/TableGridOptions";
import TextTable from "@/components/text/TextTable.vue";
import {mapActions} from 'vuex';

export default defineComponent({
  name: "BasicTableComponent",
  components: {TextTable, EchartsEditor, AgGridVue},
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
      visualizationDrawerVisible: false,
      visualizationConfigure: EchartsConfigure,
      visibleColumns: [],
      columnDefs: [],
      isPage: true,
      type: 'table'
    }
  },
  methods: {
    ...mapActions(['updateData']),
    handlerInitialize()
    {
      this.updateData(this.configure);
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
    },
    handlerVisualization(isOpen: boolean)
    {
      if (isOpen) {
        this.visualizationConfigure = new EchartsConfigure();
        this.visualizationConfigure.headers = this.configure.headers;
        this.visualizationConfigure.types = this.configure.types;
        this.visualizationConfigure.columns = this.configure.columns;
        this.visualizationDrawerVisible = true;
      }
      else {
        this.visualizationDrawerVisible = false;
      }
    }
  }
});
</script>
