<template>
  <div class="home">
    <Layout class="content">
      <Sider hide-trigger class="content">
        <Card style="width:100%; min-height: 500px"
              dis-hover>
          <template #title>
            <SourceSelect @changeValue="handlerChangeValue($event)"/>
          </template>
          <DataStructureLazyTree v-if="applySource"
                                 :id="applySource">
          </DataStructureLazyTree>
        </Card>
      </Sider>
      <Layout class="content"
              :style="{padding: '0 12px 12px'}">
        <Content>
          <Card style="width:100%"
                dis-hover>
            <template #title>
              <Space>
                <Button type="primary"
                        size="small"
                        icon="md-arrow-dropright-circle"
                        :loading="tableLoading"
                        :disabled="!applySource"
                        @click="handlerRun()">
                  {{ buttonRunText }}
                </Button>
                <Button type="dashed"
                        size="small"
                        icon="md-code"
                        :disabled="!applySource"
                        @click="handlerFormat()">
                  {{ $t('common.format') }}
                </Button>
                <Button type="error"
                        size="small"
                        icon="md-close-circle"
                        :disabled="!applySource || !tableLoading"
                        @click="handlerCancel()">
                  {{ $t('common.cancel') }}
                </Button>
                <Button v-if="tableConfigure"
                        type="primary"
                        size="small"
                        icon="md-add"
                        @click="handlerCreateSnippet()">
                  {{ $t('common.snippet') }}
                </Button>
                <Poptip v-if="response.data"
                        placement="bottom">
                  <Button icon="md-clock"
                          size="small">
                    {{ response.data.processor.elapsed }} ms
                  </Button>
                  <template #content>
                    <Space :size="20">
                      <template #split> |</template>
                      <NumberInfo class="center"
                                  :total="response.data.connection.elapsed">
                        <template #title>
                          <span>Connection</span>
                          <Tooltip content="Connection time!"
                                   placement="bottom">
                            <Icon type="md-help-circle"
                                  style="margin-left: 3px"/>
                          </Tooltip>
                        </template>
                      </NumberInfo>
                      <NumberInfo class="center"
                                  :total="response.data.processor.elapsed">
                        <template #title>
                          <span>Execute</span>
                          <Tooltip content="Execute time!"
                                   placement="bottom">
                            <Icon type="md-help-circle"
                                  style="margin-left: 3px"/>
                          </Tooltip>
                        </template>
                      </NumberInfo>
                    </Space>
                  </template>
                </Poptip>
                <Badge v-if="applySource && (response.data || !response.status)"
                       :count="aiSupportType.length">
                  <Button type="primary"
                          size="small"
                          icon="md-ionitron"
                          @click="handlerVisibleHelp(true)">
                  </Button>
                </Badge>
              </Space>
            </template>
            <div ref="editorContainer">
              <Tabs v-model="activeKey"
                    type="card"
                    :animated="false"
                    @on-tab-remove="handlerMinusEditor"
                    @on-click="handlerChangeEditor">
                <template #extra>
                  <Button size="small"
                          type="primary"
                          icon="md-add"
                          @click="handlerPlusEditor">
                  </Button>
                </template>
                <TabPane v-for="editor in editorMaps.values()"
                         :key="editor.key"
                         :name="editor.key"
                         :label="editor.title"
                         :closable="editor.closable">
                  <VAceEditor lang="mysql"
                              theme="chrome"
                              style="height: 300px"
                              :key="editor.key"
                              :options="{enableSnippets: true, enableLiveAutocompletion: true}"
                              @init="handlerEditorDidMount($event, 'mysql', editor.key)">
                  </VAceEditor>
                </TabPane>
                <!--                <MonacoEditor theme="vs" :options="{theme: 'vs-dark', fontSize: 15}" language="sql" :height="300"-->
                <!--                              :key="activeKey.value" @change="handlerChangeEditorValue" :width="'100%'"-->
                <!--                              v-model:value="activeEditorValue" @editorDidMount="handlerEditorDidMount($event, 'mysql')">-->
                <!--                </MonacoEditor>-->
              </Tabs>
            </div>
          </Card>
          <div style="margin-top: 5px;">
            <BasicTableComponent v-if="tableConfigure"
                                 :configure="tableConfigure">
            </BasicTableComponent>
          </div>
        </Content>
      </Layout>
    </Layout>
    <SnippetDetails v-if="snippetDetails"
                    :isVisible="snippetDetails"
                    :codeSnippet="content"
                    @close="handlerCloseSnippetDetails($event)">
    </SnippetDetails>
    <QueryAiHelp v-if="visibleAiHelp"
                 :isVisible="visibleAiHelp"
                 :content="content"
                 :aiSupports="aiSupportType"
                 :error="error"
                 :engine="engine"
                 @close="handlerVisibleHelp($event)">
    </QueryAiHelp>
  </div>
</template>

<script lang="ts">
import {ExecuteModel} from "@/model/ExecuteModel";
import {ExecuteService} from "@/services/ExecuteService";
import {FormatService} from "@/services/FormatService";
import axios, {CancelTokenSource} from "axios";
import {defineComponent} from "vue";
import {useRouter} from "vue-router";
import {TableConfigure} from "@/components/table/TableConfigure";
import SourceSelect from "@/components/source/SourceSelect.vue";
import SnippetDetails from "@/views/admin/snippet/SnippetDetails.vue";
import BasicTableComponent from "@/components/table/BasicTable.vue";
import {AuditService} from "@/services/AuditService";
import {useI18n} from "vue-i18n";
import QueryAiHelp from "@/views/user/console/QueryAiHelp.vue";
import SnippetService from "@/services/SnippetService";
import DataStructureLazyTree from "@/components/common/DataStructureLazyTree.vue";
import {VAceEditor} from 'vue3-ace-editor';
import {Ace} from "ace-builds";
import {ResponseModel} from "@/model/ResponseModel";
import 'ace-builds/src-noconflict/mode-mysql';
import 'ace-builds/src-noconflict/theme-chrome';
import langTools from 'ace-builds/src-noconflict/ext-language_tools';
import {HttpCommon} from "@/common/HttpCommon";
import FunctionsService from "@/services/settings/functions/FunctionsService";
import Editor = Ace.Editor;

interface EditorInstance
{
  title: string;
  key: string;
  closable?: boolean,
  instance?: Editor
}

export default defineComponent({
  name: "ConsoleIndex",
  components: {
    VAceEditor,
    DataStructureLazyTree,
    QueryAiHelp,
    BasicTableComponent,
    SnippetDetails,
    SourceSelect
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
      applySource: 0,
      applySourceType: '',
      tableConfigure: null as TableConfigure,
      tableOptions: {},
      tableColumns: [],
      tableLoading: false,
      cancelToken: {} as CancelTokenSource,
      response: {} as ResponseModel,
      snippetDetails: false,
      editorMaps: new Map<string, EditorInstance>(),
      activeKey: null,
      content: null,
      visibleAiHelp: false,
      engine: null,
      aiSupportType: ['ANALYSIS', 'OPTIMIZE'],
      error: null,
      buttonRunText: null,
      isSelection: false
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.buttonRunText = this.i18n.t('common.run');
      const defaultEditor: EditorInstance = {
        title: 'New Query',
        key: Date.now().toString()
      };
      this.activeKey = defaultEditor.key;
      this.editorMaps.set(defaultEditor.key, defaultEditor);
      const router = useRouter();
      if (router.currentRoute?.value?.query) {
        const id = router.currentRoute.value.query.id as unknown as number;
        const from = router.currentRoute.value.query.from;
        if (id && from) {
          if (from === 'snippet') {
            SnippetService.getById(id)
              .then((response) => {
                if (response.status && response.data?.code) {
                  const instance = this.editorMaps.get(this.activeKey);
                  instance.instance.setValue(response.data.code);
                }
              });
          }
          else if (from === 'history') {
            new AuditService().getById(id)
              .then((response) => {
                if (response.status && response.data?.content) {
                  const instance = this.editorMaps.get(this.activeKey);
                  instance.instance.setValue(response.data.content);
                  this.handlerChangeValue(response.data.plugin.id + ':' + response.data.plugin.type);
                }
              });
          }
        }
      }
    },
    handlerInitializeCompleter(editor: Editor, language: string)
    {
      try {
        // The configuration editor selects content events
        editor.selection.on('changeSelection', () => {
          const selection = editor.getSelection();
          if (selection.isEmpty()) {
            this.buttonRunText = this.i18n.t('common.run');
            this.isSelection = false;
          }
          else {
            this.buttonRunText = this.i18n.t('common.runSelection');
            this.isSelection = true;
          }
        })

        langTools.addCompleter({
          getCompletions: function (editor, session, pos, prefix, callback) {
            return callback(null, []);
          }
        });
        // Clear default keywords and code snippets
        editor.completers = [];
        const that = this;
        const client = new HttpCommon().getAxios();
        client.all([FunctionsService.getByPlugin(language.toLowerCase()), SnippetService.getSnippets(0, 100000)])
          .then(client.spread((keyword, snippet) => {
            if (keyword.status) {
              const keywordCompleter = {
                getCompletions: function (editor, session, pos, prefix, callback) {
                  return callback(null, keyword.data.content.map(function (item: { example: string; name: string; type: any; description: string; }) {
                    return {
                      value: item.example,
                      caption: item.name,
                      meta: that.i18n.t('common.' + item.type.toLowerCase()),
                      docHTML: '<div>' +
                        '<strong>' + item.name + '</strong><br/><hr/>'
                        + that.i18n.t('common.description') + ':\n' + item.description + '<br/><hr/>'
                        + that.i18n.t('common.example') + ':\n' + item.example + '<br/><hr/>'
                        + '</div>'
                    };
                  }));
                }
              };
              editor.completers.push(keywordCompleter);
            }
            if (snippet.status) {
              const snippetCompleter = {
                getCompletions: function (editor, session, pos, prefix, callback) {
                  return callback(null, snippet.data.content.map(function (item: { code: any; name: string; description: string; }) {
                    return {
                      value: item.code,
                      caption: item.name,
                      meta: that.i18n.t('common.snippet'),
                      docHTML: '<div>' +
                        '<strong>' + item.name + '</strong><br/><hr/>'
                        + that.i18n.t('common.description') + ':\n' + item.description + '<br/><hr/>'
                        + '</div>'
                    };
                  }));
                }
              };
              editor.completers.push(snippetCompleter);
            }
          }));
      }
      catch (e) {
        console.error(e)
      }
    },
    handlerEditorDidMount(editor: Editor, language: string, key?: string | number)
    {
      if (key) {
        this.editorMaps.get(key).instance = editor;
      }
      // Initializes the completer
      this.handlerInitializeCompleter(editor, language);
    },
    handlerRun()
    {
      this.tableConfigure = null;
      this.error = null;
      this.aiSupportType = ['ANALYSIS', 'OPTIMIZE'];
      this.response = {};
      this.cancelToken = axios.CancelToken.source();
      this.tableLoading = true;
      const editorContainer: HTMLElement = this.$refs.editorContainer as HTMLElement;
      const editorInstance = this.editorMaps.get(this.activeKey);
      const configure: ExecuteModel = {
        name: this.applySource,
        content: this.isSelection ? editorInstance.instance.getSelectedText() : editorInstance.instance.getValue(),
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
            this.$Message.error({
              content: response.message,
              duration: 5,
              closable: true
            });
            this.error = response.message;
            this.aiSupportType.push('FIXEDBUGS');
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
      this.engine = idAndType[1];
      this.handlerEditorDidMount(this.editorMaps.get(this.activeKey).instance, idAndType[1]);
    },
    handlerFormat()
    {
      const editorInstance = this.editorMaps.get(this.activeKey);
      const configure = {
        sql: editorInstance.instance.getValue()
      };
      new FormatService()
        .formatSql(configure)
        .then((response) => {
          if (response.status) {
            editorInstance.instance.setValue(response.data);
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
      const editorInstance = this.editorMaps.get(this.activeKey);
      this.content = editorInstance.instance.getValue();
      this.snippetDetails = true;
    },
    handlerCloseSnippetDetails(value: boolean)
    {
      this.snippetDetails = value;
    },
    handlerPlusEditor()
    {
      this.error = null;
      this.aiSupportType = ['ANALYSIS', 'OPTIMIZE'];
      const newEditor: EditorInstance = {
        title: 'New Query',
        key: Date.now().toString(),
        closable: true
      };
      this.activeKey = newEditor.key;
      this.editorMaps.set(newEditor.key, newEditor);
    },
    handlerMinusEditor(targetKey: string)
    {
      this.editorMaps.delete(targetKey);
    },
    handlerChangeEditor(targetKey: string)
    {
      this.error = null;
      this.aiSupportType = ['ANALYSIS', 'OPTIMIZE'];
    },
    handlerVisibleHelp(value: boolean)
    {
      this.visibleAiHelp = value;
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

@keyframes changDeg {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.go {
  animation: changDeg 2s linear 0.2s infinite;
}
</style>
