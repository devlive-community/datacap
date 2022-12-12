<template>
  <div class="home">
    <Layout class="content">
      <Sider hide-trigger class="content">
        <Card style="width:100%; min-height: 500px">
          <template #title>
            <SourceSelect @changeValue="handlerChangeValue($event)"/>
          </template>
          <DatabaseTree v-if="applySource" :id="applySource" :type="applySourceType"/>
        </Card>
      </Sider>
      <Layout class="content" :style="{padding: '0 12px 12px'}">
        <Content>
          <Card style="width:100%">
            <template #title>
              <Space>
                <Button type="primary" size="small" :loading="tableLoading" :disabled="!applySource"
                        icon="md-arrow-dropright-circle" @click="handlerRun()">
                  {{ $t('common.run') }}
                </Button>
                <Button type="dashed" size="small" :disabled="!applySource" icon="md-code" @click="handlerFormat()">
                  {{ $t('common.format') }}
                </Button>
                <Button type="error" size="small" :disabled="!applySource || !tableLoading"
                        icon="md-close-circle" @click="handlerCancel()">
                  {{ $t('common.cancel') }}
                </Button>
                <Button v-if="tableConfigure" type="primary" size="small" icon="md-add" @click="handlerCreateSnippet()">
                  {{ $t('common.snippet') }}
                </Button>
                <Poptip v-if="response.data" placement="bottom">
                  <Button icon="md-clock" size="small">
                    {{ response.data.processor.elapsed }} ms
                  </Button>
                  <template #content>
                    <Space :size="20">
                      <template #split> |</template>
                      <NumberInfo class="center" :total="response.data.connection.elapsed">
                        <template #title>
                          <span>Connection</span>
                          <Tooltip content="Connection time!" placement="bottom">
                            <Icon type="md-help-circle" style="margin-left: 3px"/>
                          </Tooltip>
                        </template>
                      </NumberInfo>
                      <NumberInfo class="center" :total="response.data.processor.elapsed">
                        <template #title>
                          <span>Execute</span>
                          <Tooltip content="Execute time!" placement="bottom">
                            <Icon type="md-help-circle" style="margin-left: 3px"/>
                          </Tooltip>
                        </template>
                      </NumberInfo>
                    </Space>
                  </template>
                </Poptip>
              </Space>
            </template>
            <div ref="editorContainer">
              <Tabs v-model="activeKey" type="card" :animated="false" @on-tab-remove="handlerMinusEditor" @on-click="handlerChangeEditor">
                <template #extra>
                  <Button @click="handlerPlusEditor" size="small" type="primary" icon="md-add"/>
                </template>
                <TabPane v-for="editor in editors" :key="editor.key" :name="editor.key" :label="editor.title" :closable="editor.closable">
                  <MonacoEditor theme="vs" :options="{theme: 'vs-dark', fontSize: 15}" language="sql" :height="300"
                                :key="activeKey.value" @change="handlerChangeEditorValue"
                                v-model:value="activeEditorValue" @editorDidMount="handlerEditorDidMount($event, 'mysql')">
                  </MonacoEditor>
                </TabPane>
              </Tabs>
            </div>
          </Card>
          <div style="margin-top: 5px;">
            <BasicTableComponent v-if="tableConfigure" :configure="tableConfigure"></BasicTableComponent>
          </div>
        </Content>
      </Layout>
    </Layout>
    <SnippetDetails v-if="snippetDetails" :isVisible="snippetDetails"
                    :codeSnippet="activeEditorValue" @close="handlerCloseSnippetDetails($event)">
    </SnippetDetails>
  </div>
</template>

<script lang="ts">
import {ExecuteModel} from "@/model/ExecuteModel";
import {ExecuteService} from "@/services/ExecuteService";
import {FormatService} from "@/services/FormatService";
import axios, {CancelTokenSource} from "axios";
import * as monaco from 'monaco-editor';
import MonacoEditor from 'monaco-editor-vue3';
import {defineComponent, ref} from "vue";
import {useRouter} from "vue-router";
import {SnippetService} from "@/services/SnippetService";
import DatabaseTree from "@/components/common/DatabaseTree.vue";
import {TableConfigure} from "@/components/table/TableConfigure";
import SourceSelect from "@/components/source/SourceSelect.vue";
import SnippetDetails from "@/views/pages/admin/snippet/SnippetDetails.vue";
import BasicTableComponent from "@/components/table/BasicTable.vue";
import {AuditService} from "@/services/AuditService";
import FunctionService from "@/services/settings/function/FunctionService";
import {useI18n} from "vue-i18n";

const editors = ref<{ title: string; key: string; closable?: boolean }[]>([
  {title: 'Editor', key: '1', closable: false}
]);
const activeKey = ref(editors.value[0].key);
const editorMap = new Map<string, monaco.editor.ICodeEditor>();
const editorValueMap = new Map<string, string>();

export default defineComponent({
  name: "QueryHome",
  components: {BasicTableComponent, SnippetDetails, DatabaseTree, SourceSelect, MonacoEditor},
  unmounted()
  {
    if (this.editorCompletionProvider) {
      this.editorCompletionProvider.dispose();
    }
  },
  setup()
  {
    const i18n = useI18n();
    return {
      i18n
    }
  },
  data()
  {
    return {
      applySource: null || '',
      applySourceType: '',
      tableConfigure: null as TableConfigure,
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
          else if (from === 'history') {
            new AuditService().getById(id)
              .then((response) => {
                if (response.status && response.data?.content) {
                  this.activeEditorValue = response.data.content;
                  this.handlerChangeValue(response.data.plugin.id + ':' + response.data.plugin.type);
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

      if (newEditor) {
        editorMap.set(newEditor, editor);
        editorValueMap.set(activeKey.value, '');
      }
      else {
        editorMap.set(activeKey.value, editor);
      }
      FunctionService.getByPlugin(language)
        .then((response) => {
          if (response.status) {
            const languageSugs = [];
            response.data.content.forEach(value => {
              languageSugs.push({
                label: value.name,
                detail: this.i18n.t('common.' + value.type.toLowerCase()) + '\n' + value.description,
                insertText: value.content
              });
            });
            this.editorCompletionProvider = monaco.languages.registerCompletionItemProvider("sql", {
              provideCompletionItems(): any
              {
                return {
                  suggestions: languageSugs.map((item) => ({
                    ...item,
                    kind: item.icon ? monaco.languages.CompletionItemKind.Variable : null,
                  }))
                };
              },
              triggerCharacters: ['.'],
            });
          }
        });
    },
    handlerRun()
    {
      this.tableConfigure = null;
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
            const tConfigure: TableConfigure = {
              headers: response.data.headers,
              columns: response.data.columns,
              height: 340,
              width: editorContainer.offsetWidth + 20,
              showSeriesNumber: false
            };
            this.tableConfigure = tConfigure;
          }
          else {
            this.$Message.error(response.message);
            this.tableConfigure = null;
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
            this.$Message.error(response.message);
          }
        });
    },
    handlerCancel()
    {
      this.cancelToken.cancel("Cancel query");
    },
    handlerCreateSnippet()
    {
      this.snippetDetails = true;
    },
    handlerCloseSnippetDetails(value: boolean)
    {
      this.snippetDetails = value;
    },
    handlerPlusEditor()
    {
      activeKey.value = 'newTab' + activeKey.value + Date.parse(new Date().toString());
      editors.value.push({title: 'New Tab', key: activeKey.value, closable: true});
      editorValueMap.set(activeKey.value, '');
      this.handlerEditorDidMount(null, this.applySourceType, activeKey.value);
      this.activeEditorValue = editorValueMap.get(activeKey.value) as string;
    },
    handlerMinusEditor(targetKey: string)
    {
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
    },
    handlerChangeEditor(targetKey: string)
    {
      this.activeEditorValue = editorValueMap.get(targetKey) as string;
    },
    handlerChangeEditorValue(value: string)
    {
      editorValueMap.set(activeKey.value, value);
    }
  },
  // Prevents errors from affecting other components
  errorCaptured(err: Error, vm)
  {
    return false;
  }
});
</script>
<style scoped>
.content {
  background-color: #FFFFFF;
}

.center {
  text-align: center;
}
</style>
