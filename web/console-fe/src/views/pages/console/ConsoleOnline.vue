<template>
  <div class="home">
    <div ref="editorContainer">
      <a-card size="small">
        <template #title>
          <a-space :size="8">
            <SourceSelectComponent @changeValue="handlerChangeValue($event)" />
            <a-button type="primary" size="small" :loading="tableLoading" :disabled="!applySource"
              @click="handlerRun()">
              <play-circle-outlined v-if="!tableLoading" /> Run
            </a-button>
            <a-button type="dashed" size="small" :disabled="!applySource" @click="handlerFormat()">
              <format-painter-outlined /> Format
            </a-button>
            <a-button type="primary" danger size="small" :disabled="!applySource || !tableLoading"
              @click="handlerCancel()">
              <close-circle-outlined /> Cancel
            </a-button>
            <a-button v-if="response.data" type="link" size="small">
              <a-popconfirm placement="bottom" :showCancel="false">
                <clock-circle-outlined /> {{response.data.processor.elapsed}} ms
                <template #okButton></template>
                <template #icon></template>
                <template #title>
                  <div style="min-width: 300px;">
                    <a-row :gutter="16">
                      <a-col :span="12">
                        <a-statistic :value="response.data.connection.elapsed" suffix="ms">
                          <template #title>
                            <span>Connection</span>
                            <a-tooltip placement="right">
                              <template #title>
                                <span>Connection time!</span>
                              </template>
                              <question-circle-two-tone style="margin-left: 3px" />
                            </a-tooltip>
                          </template>
                        </a-statistic>
                      </a-col>
                      <a-col :span="12">
                        <a-statistic :value="response.data.processor.elapsed" suffix="ms">
                          <template #title>
                            <span>Execute</span>
                            <a-tooltip placement="right">
                              <template #title>
                                <span>Execute time!</span>
                              </template>
                              <question-circle-two-tone style="margin-left: 3px" />
                            </a-tooltip>
                          </template>
                        </a-statistic>
                      </a-col>
                    </a-row>
                  </div>
                </template>
              </a-popconfirm>
            </a-button>
          </a-space>
        </template>
        <MonacoEditor theme="vs" :options="{theme: 'vs-dark', fontSize: 15}" language="sql" :height="300"
          v-model:value="editorValue" @editorDidMount="handlerEditorDidMount($event)">
        </MonacoEditor>
      </a-card>
    </div>
    <div style="margin-top: 5px;">
      <a-card :loading="tableLoading" :body-style="{padding: '2px'}">
        <div v-if="tableConfigure.data" style="margin-bottom: 3px;">
          <a-dropdown>
            <template #overlay>
              <a-menu @click="handlerExport()">
                <a-menu-item key="export_csv">
                  CSV
                </a-menu-item>
              </a-menu>
            </template>
            <a-button type="primary" size="small">
              Export
              <DownloadOutlined />
            </a-button>
          </a-dropdown>
        </div>
        <SheetComponent :dataCfg="tableConfigure" :options="tableOptions" :showPagination="true" sheetType="table" />
      </a-card>
    </div>
  </div>
</template>

<script lang="ts">
import SourceSelectComponent from "@/components/source/SourceSelect.vue";
import { ExecuteModel } from "@/model/ExecuteModel";
import { ExecuteService } from "@/services/ExecuteService";
import { FormatService } from "@/services/FormatService";
import { LanguageService } from "@/services/LanguageService";
import { SheetComponent } from "@antv/s2-vue";
import "@antv/s2-vue/dist/style.min.css";
import { message } from "ant-design-vue";
import "ant-design-vue/dist/antd.css";
import axios, { CancelTokenSource } from "axios";
import { ExportToCsv } from 'export-to-csv';
import * as monaco from 'monaco-editor';
import MonacoEditor from 'monaco-editor-vue3';
import { defineComponent } from "vue";

export default defineComponent({
  name: "DashboardConsoleView",
  components: { SheetComponent, SourceSelectComponent, MonacoEditor },
  unmounted() {
    if (this.editorCompletionProvider) {
      this.editorCompletionProvider.dispose();
    }
  },
  data() {
    return {
      applySource: null || '',
      tableConfigure: {},
      tableOptions: {},
      tableColumns: [],
      tableLoading: false,
      editorValue: '',
      cancelToken: {} as CancelTokenSource,
      response: {},
      editorInstance: {} as monaco.editor.ICodeEditor,
      editorCompletionProvider: {} as monaco.IDisposable
    }
  },
  methods: {
    handlerEditorDidMount(editor: any) {
      const suggestions = new LanguageService().transSuggestions([]);
      this.editorInstance = editor;
      this.editorCompletionProvider = monaco.languages.registerCompletionItemProvider("sql", {
        provideCompletionItems(): any {
          return {
            suggestions: suggestions.map((item) => ({
              ...item,
              kind: item.icon ? monaco.languages.CompletionItemKind.Variable : null,
            }))
          };
        },
        triggerCharacters: ['.', ' '],
      });
    },
    handlerRun() {
      this.response = {};
      this.cancelToken = axios.CancelToken.source();
      this.tableLoading = true;
      const editorContainer: HTMLElement = this.$refs.editorContainer as HTMLElement;
      const configure: ExecuteModel = {
        name: this.applySource,
        content: this.editorValue,
        format: "JSON"
      };
      new ExecuteService()
        .execute(configure, this.cancelToken.token)
        .then((response) => {
          if (response.status) {
            this.response = response;
            this.tableColumns = response.data.columns;
            this.tableConfigure = {
              fields: {
                columns: response.data.headers
              },
              data: response.data.columns
            };
            this.tableOptions = {
              width: editorContainer.offsetWidth - 8,
              height: 640,
              pagination: {
                pageSize: 20,
                current: 1,
              },
              showSeriesNumber: true,
            }
          }
          else {
            message.error(response.message);
          }
        })
        .finally(() => {
          this.tableLoading = false;
        });
    },
    handlerChangeValue(value: string) {
      this.applySource = value;
    },
    handlerFormat() {
      const configure = {
        sql: this.editorValue
      };
      new FormatService()
        .formatSql(configure)
        .then((response) => {
          if (response.status) {
            this.editorValue = response.data;
          }
          else {
            message.error(response.message);
          }
        });
    },
    handlerCancel() {
      this.cancelToken.cancel("Cancel query");
    },
    handlerExport() {
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
      csvExporter.generateCsv(this.tableColumns);
    }
  }
});
</script>
