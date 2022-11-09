<template>
  <div class="home">
    <a-row>
      <a-col :span="4">
        <a-card size="small" bodyStyle="padding: 2px;">
          <template #title>
            <a-space :size="8">
              <SourceSelectComponent @changeValue="handlerChangeValue($event)"/>
            </a-space>
          </template>
          <DatabaseTree v-if="applySource" :id="applySource" :type="applySourceType"></DatabaseTree>
        </a-card>
      </a-col>
      <a-col :span="20">
        <div ref="editorContainer">
          <a-card size="small" bodyStyle="padding: 2px;" style="margin-left: 2px">
            <template #title>
              <a-space :size="8">
                <a-button type="primary" size="small" :loading="tableLoading" :disabled="!applySource"
                          @click="handlerRun()">
                  <play-circle-outlined v-if="!tableLoading"/>
                  {{ $t('common.run') }}
                </a-button>
                <a-button type="dashed" size="small" :disabled="!applySource" @click="handlerFormat()">
                  <format-painter-outlined/>
                  {{ $t('common.format') }}
                </a-button>
                <a-button type="primary" danger size="small" :disabled="!applySource || !tableLoading"
                          @click="handlerCancel()">
                  <close-circle-outlined/>
                  {{ $t('common.cancel') }}
                </a-button>
                <a-button v-if="tableConfigure.data" type="primary" size="small" @click="handlerCreateSnippet()">
                  <plus-outlined/>
                  {{ $t('common.snippet') }}
                </a-button>
                <a-button v-if="response.data" type="link" size="small">
                  <a-popconfirm placement="bottom" :showCancel="false">
                    <clock-circle-outlined/>
                    {{ response.data.processor.elapsed }} ms
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
                                  <question-circle-two-tone style="margin-left: 3px"/>
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
                                  <question-circle-two-tone style="margin-left: 3px"/>
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
            <a-tabs v-model:activeKey="activeKey" type="editable-card" @edit="handlerPlusEditor" @change="handlerChangeEditor" size="small">
              <a-tab-pane v-for="editor in editors" :key="editor.key" :tab="editor.title" :closable="editor.closable">
                <MonacoEditor theme="vs" :options="{theme: 'vs-dark', fontSize: 15}" language="sql" :height="300"
                              :key="activeKey.value" @change="handlerChangeEditorValue"
                              v-model:value="activeEditorValue" @editorDidMount="handlerEditorDidMount($event, 'mysql')">
                </MonacoEditor>
              </a-tab-pane>
            </a-tabs>
            <div style="margin-top: 5px;">
              <a-empty v-if="!tableConfigure.data && !tableLoading"/>
              <a-card v-else :loading="tableLoading" :body-style="{padding: '2px'}">
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
                      <DownloadOutlined/>
                    </a-button>
                  </a-dropdown>
                </div>
                <SheetComponent :dataCfg="tableConfigure" :options="tableOptions" :showPagination="true" sheetType="table"/>
              </a-card>
            </div>
          </a-card>
        </div>
      </a-col>
    </a-row>
    <SnippetDetails v-if="snippetDetails" :isVisible="snippetDetails"
                    :codeSnippet="activeEditorValue" @close="handlerCloseSnippetDetails($event)">
    </SnippetDetails>
  </div>
</template>

<script lang="ts">
import SourceSelectComponent from "@/components/source/SourceSelect.vue";
import {ExecuteModel} from "@/model/ExecuteModel";
import {ExecuteService} from "@/services/ExecuteService";
import {FormatService} from "@/services/FormatService";
import {LanguageService} from "@/services/LanguageService";
import {SheetComponent} from "@antv/s2-vue";
import "@antv/s2-vue/dist/style.min.css";
import {message} from "ant-design-vue";
import "ant-design-vue/dist/antd.css";
import axios, {CancelTokenSource} from "axios";
import {ExportToCsv} from 'export-to-csv';
import * as monaco from 'monaco-editor';
import MonacoEditor from 'monaco-editor-vue3';
import {defineComponent, ref} from "vue";
import SnippetDetails from "@/views/pages/admin/snippet/SnippetDetails.vue";
import {useRouter} from "vue-router";
import {SnippetService} from "@/services/SnippetService";
import DatabaseTree from "@/components/common/DatabaseTree.vue";

const editors = ref<{ title: string; key: string; closable?: boolean }[]>([
  {title: 'Editor', key: '1', closable: false}
]);
const activeKey = ref(editors.value[0].key);
const editorMap = new Map<string, monaco.editor.ICodeEditor>();
const editorValueMap = new Map<string, string>();

export default defineComponent({
  name: "DashboardConsoleView",
  components: {DatabaseTree, SnippetDetails, SheetComponent, SourceSelectComponent, MonacoEditor},
  unmounted()
  {
    if (this.editorCompletionProvider) {
      this.editorCompletionProvider.dispose();
    }
  },
  data()
  {
    return {
      applySource: null || '',
      applySourceType: '',
      tableConfigure: {},
      tableOptions: {},
      tableColumns: [],
      tableLoading: false,
      cancelToken: {} as CancelTokenSource,
      response: {},
      editorCompletionProvider: {} as monaco.IDisposable,
      snippetDetails: false,
      activeEditorValue: '',
      editors,
      activeKey,
      editorValueMap
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      const router = useRouter();
      if (router.currentRoute?.value?.query) {
        const id = router.currentRoute.value.query.id as unknown as number;
        const from = router.currentRoute.value.query.from;
        if (id && from) {
          if (from === 'snippet') {
            new SnippetService().getById(id)
              .then((response) => {
                if (response.status && response.data?.code) {
                  this.activeEditorValue = response.data.code;
                }
              });
          }
        }
      }
    },
    handlerEditorDidMount(editor: any, language: string, newEditor?: string)
    {
      try {
        this.editorCompletionProvider.dispose();
      }
      catch (e) {
        console.log(e);
      }
      const suggestions = new LanguageService().transSuggestions([], language);
      if (newEditor) {
        editorMap.set(newEditor, editor);
      }
      else {
        editorMap.set(activeKey.value, editor);
      }
      editorValueMap.set(activeKey.value, '');
      this.editorCompletionProvider = monaco.languages.registerCompletionItemProvider("sql", {
        provideCompletionItems(): any
        {
          return {
            suggestions: suggestions.map((item) => ({
              ...item,
              kind: item.icon ? monaco.languages.CompletionItemKind.Variable : null,
            }))
          };
        },
        triggerCharacters: ['.'],
      });
    },
    handlerRun()
    {
      this.response = {};
      this.cancelToken = axios.CancelToken.source();
      this.tableLoading = true;
      const editorContainer: HTMLElement = this.$refs.editorContainer as HTMLElement;
      const configure: ExecuteModel = {
        name: this.applySource,
        content: this.activeEditorValue,
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
              width: editorContainer.offsetWidth - 15,
              height: 340,
              pagination: {
                pageSize: 10,
                current: 1,
              },
              showSeriesNumber: true,
            }
          }
          else {
            message.error(response.message);
            this.tableConfigure = {};
          }
        })
        .finally(() => {
          this.tableLoading = false;
        });
    },
    handlerChangeValue(value: string)
    {
      const idAndType = value.split(':');
      this.applySource = idAndType[0];
      this.applySourceType = idAndType[1];
      this.editorCompletionProvider.dispose();
      this.handlerEditorDidMount(editorMap.get(activeKey.value), idAndType[1]);
    },
    handlerFormat()
    {
      const configure = {
        sql: this.activeEditorValue
      };
      new FormatService()
        .formatSql(configure)
        .then((response) => {
          if (response.status) {
            this.activeEditorValue = response.data;
            editorValueMap.set(activeKey.value, this.activeEditorValue);
          }
          else {
            message.error(response.message);
          }
        });
    },
    handlerCancel()
    {
      this.cancelToken.cancel("Cancel query");
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
      csvExporter.generateCsv(this.tableColumns);
    },
    handlerCreateSnippet()
    {
      this.snippetDetails = true;
    },
    handlerCloseSnippetDetails(value: boolean)
    {
      this.snippetDetails = value;
    },
    handlerPlusEditor(targetKey: string | MouseEvent, action: string)
    {
      if (action === 'add') {
        activeKey.value = 'newTab' + activeKey.value + Date.parse(new Date().toString());
        editors.value.push({title: 'New Tab', key: activeKey.value});
        editorValueMap.set(activeKey.value, '');
        this.handlerEditorDidMount(null, this.applySourceType, activeKey.value);
        this.activeEditorValue = editorValueMap.get(activeKey.value) as string;
      }
      else {
        let lastIndex = 0;
        editors.value.forEach((editor, i) => {
          if (editor.key === targetKey) {
            lastIndex = i - 1;
          }
        });
        editors.value = editors.value.filter(editor => editor.key !== targetKey);
        if (editors.value.length && activeKey.value === targetKey) {
          if (lastIndex >= 0) {
            activeKey.value = editors.value[lastIndex].key;
          }
          else {
            activeKey.value = editors.value[0].key;
          }
        }
        editorMap.delete(targetKey as string);
        editorValueMap.delete(targetKey as string);
        this.handlerChangeEditor(activeKey.value);

      }
    },
    handlerChangeEditor(targetKey: string)
    {
      this.activeEditorValue = editorValueMap.get(targetKey) as string;
    },
    handlerChangeEditorValue(value: string)
    {
      editorValueMap.set(activeKey.value, value);
    }
  }
});
</script>
