<template>
  <div class="hidden space-y-6 pb-16 w-full md:block">
    <div class="flex flex-col space-y-8 lg:flex-row lg:space-x-6 lg:space-y-0">
      <aside class="-mx-4 w-[200px]">
        <Card>
          <CardHeader class="p-0">
            <SourceSelect :value="selectSource.full as string" @changeValue="handlerChangeValue($event)"/>
            <DataStructureLazyTree v-if="selectSource.id" :id="selectSource.id as string"/>
          </CardHeader>
        </Card>
      </aside>
      <div class="flex-1">
        <div class="space-y-6">
          <div class="flex items-center space-x-4 text-sm">
            <Card class="w-full h-full" style="border-radius: 0">
              <CardHeader class="p-2 pl-5">
                <CardTitle>
                  <div class="flex justify-between items-center h-5 pt-3 pb-3">
                    <div class="flex items-center">
                      <Button :disabled="(!selectSource.id && !loading.running) || loading.running" size="sm" @click="handlerRun()">
                        <Loader2 v-if="loading.running" class="w-full justify-center animate-spin mr-1" :size="18"/>
                        <PlayCircle v-else class="mr-1" :size="18"/>
                        {{ selectEditor.isSelection ? $t('query.common.executeSelection') : $t('query.common.execute') }}
                      </Button>
                      <Button :disabled="(!selectSource.id && !loading.formatting) || loading.formatting" size="sm" class="ml-2" variant="secondary" @click="handlerFormat()">
                        <Loader2 v-if="loading.formatting" class="w-full justify-center animate-spin mr-1" :size="18"/>
                        <RemoveFormatting v-else class="mr-1" :size="18"/>
                        {{ $t('query.common.format') }}
                      </Button>
                      <Button size="sm" style="background-color: #FF4D4F;" class="ml-2" :disabled="!selectSource.id || !loading.running" @click="handlerCancel()">
                        <Ban class="mr-1" :size="18"/>
                        {{ $t('common.cancel') }}
                      </Button>
                      <Button v-if="responseConfigure.response" size="sm" class="ml-2" @click="handlerSnippet(true)">
                        <Plus class="mr-1" :size="18"/>
                        {{ $t('common.snippet') }}
                      </Button>
                      <HoverCard v-if="responseConfigure.response">
                        <HoverCardTrigger as-child>
                          <Button size="sm" class="ml-2" variant="outline">
                            <Clock class="mr-1" :size="18"/>
                            {{ responseConfigure.response.data.processor.elapsed }} ms
                          </Button>
                        </HoverCardTrigger>
                        <HoverCardContent class="w-80">
                          <div class="flex">
                            <Card class="left text-center w-1/2">
                              <CardHeader class="border-b p-4">
                                <CardTitle>{{ $t('query.common.connectionTime') }}</CardTitle>
                              </CardHeader>
                              <CardContent class="mt-3">
                                <p>{{ responseConfigure.response.data.connection.elapsed }} ms</p>
                              </CardContent>
                            </Card>
                            <Card class="ml-3 right text-center w-1/2">
                              <CardHeader class="border-b p-4">
                                <CardTitle>{{ $t('query.common.executionTime') }}</CardTitle>
                              </CardHeader>
                              <CardContent class="mt-3">
                                <p>{{ responseConfigure.response.data.processor.elapsed }} ms</p>
                              </CardContent>
                            </Card>
                          </div>
                        </HoverCardContent>
                      </HoverCard>
                      <Button v-if="selectSource.id && (responseConfigure.response?.data || !responseConfigure.response?.status)" size="sm" class="ml-2"
                              variant="ghost" @click="handlerQueryHelp(true)">
                        <Bot class="mr-1" :size="18"/>
                        {{ $t('query.common.help') }}
                      </Button>
                    </div>
                    <div class="flex items-center space-x-4 text-sm">
                      <Button size="sm" variant="outline" @click="handlerPlusEditor">
                        <Pencil class="mr-1" :size="15"/>
                        {{ $t('common.createEditor') }}
                      </Button>
                    </div>
                  </div>
                </CardTitle>
              </CardHeader>
              <CardContent ref="editorContainer" class="p-0">
                <Tabs v-model="selectEditor.activeKey as string" @update:modelValue="handlerChangeEditor">
                  <TabsList class="w-full border-r-0 border-0" style="border-radius: 0">
                    <TabsTrigger v-for="(item, index) in selectEditor.editorMaps.values()" :value="item.key">
                      {{ item.title }}
                      <CircleX v-if="index > 0" class="ml-1" :size="18" @click="handlerMinusEditor(item.key, index)"/>
                    </TabsTrigger>
                  </TabsList>
                  <VAceEditor lang="mysql" :value="selectEditor.editorInstance?.instance?.getValue() as string" :theme="selectEditor.editorInstance?.configure?.theme"
                              :style="{ height: '300px', fontSize: selectEditor.editorInstance?.configure?.fontSize + 'px' }"
                              :key="selectEditor.editorInstance?.key" :options="{ enableSnippets: true, enableLiveAutocompletion: true, readOnly: loading.froming }"
                              @init="handlerEditorDidMount($event, 'mysql', selectEditor.editorInstance?.key)"/>
                </Tabs>
              </CardContent>
              <CardContent class="p-0">
                <GridTable v-if="responseConfigure.gridConfigure" :configure="responseConfigure.gridConfigure"/>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
    <!-- Ai Help -->
    <QueryHelp v-if="visibility.queryHelp" :is-visible="visibility.queryHelp" :content="selectEditor.editorInstance?.instance?.getValue() as string"
               :help-type="queryConfigure.queryType" :engine="selectSource.engine as string" :message="responseConfigure.message as string" @close="handlerQueryHelp(false)"/>
    <SnippetInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerSnippet(false)"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import SourceSelect from '@/views/components/source/SourceSelect.vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { VAceEditor } from 'vue3-ace-editor'
import { Ace } from 'ace-builds'
import '@/ace-editor-theme'
import { UserEditor } from '@/model/user'
import Common from '@/utils/common'
import SnippetService from '@/services/snippet'
import AuditService from '@/services/audit'
import { ExecuteModel } from '@/model/execute'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Button } from '@/components/ui/button'
import { Ban, Bot, CircleX, Clock, Loader2, Pencil, PlayCircle, Plus, RemoveFormatting } from 'lucide-vue-next'
import langTools from 'ace-builds/src-noconflict/ext-language_tools'
import { HttpUtils } from '@/utils/http'
import FunctionService from '@/services/function'
import ExecuteService from '@/services/execute'
import axios from 'axios'
import { ToastUtils } from '@/utils/toast'
import GridTable from '@/views/components/grid/GridTable.vue'
import { GridConfigure } from '@/views/components/grid/GridConfigure'
import { ResponseModel } from '@/model/response'
import { HoverCard, HoverCardContent, HoverCardTrigger } from '@/components/ui/hover-card'
import FormatService from '@/services/format'
import { HelpType } from '@/views/pages/admin/query/HelpType'
import QueryHelp from '@/views/pages/admin/query/QueryHelp.vue'
import DataStructureLazyTree from '@/views/components/tree/DataStructureLazyTree.vue'
import { SnippetModel, SnippetRequest } from '@/model/snippet'
import SnippetInfo from '@/views/pages/admin/snippet/SnippetInfo.vue'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { FilterModel } from '@/model/filter.ts'
import Editor = Ace.Editor

interface EditorInstance
{
  title: string;
  key: string;
  instance?: Editor,
  configure?: UserEditor
}

export default defineComponent({
  name: 'QueryHome',
  components: {
    CircularLoading,
    SnippetInfo,
    DataStructureLazyTree,
    QueryHelp,
    HoverCardContent, HoverCardTrigger, HoverCard,
    GridTable,
    Button,
    Tabs, TabsContent, TabsList, TabsTrigger,
    CardTitle, CardContent, CardHeader, Card,
    SourceSelect,
    Loader2, Plus, CircleX, PlayCircle, Ban, Clock, RemoveFormatting, Pencil, Bot,
    VAceEditor
  },
  data()
  {
    return {
      loading: {
        running: false,
        formatting: false,
        froming: false
      },
      visibility: {
        queryHelp: false
      },
      selectSource: {
        id: null as string | null | undefined,
        type: null as string | null | undefined,
        engine: null as string | null | undefined,
        code: null as string | null | undefined,
        full: null as string | null
      },
      selectEditor: {
        editorMaps: new Map<string, EditorInstance>(),
        activeKey: null as string | null,
        editorInstance: null as EditorInstance | null,
        configure: null as UserEditor | null,
        isSelection: false
      },
      queryConfigure: {
        configure: null as ExecuteModel | null,
        cancelToken: null as any | null,
        queryType: [HelpType.ANALYSIS, HelpType.OPTIMIZE]
      },
      responseConfigure: {
        response: null as ResponseModel | null,
        gridConfigure: null as GridConfigure | null,
        message: null as string | null
      },
      dataInfo: null as unknown as SnippetModel,
      dataInfoVisible: false
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.createEditor()
      this.queryConfigure.configure = { name: this.selectSource.id as string, content: '', mode: 'ADHOC', format: 'JSON' }
      const params = this.$route.params
      if (params) {
        const code = params.code
        const type = params.type
        if (code && type) {
          if (type === 'snippet') {
            this.loading.froming = true
            this.queryConfigure.configure.mode = 'SNIPPET'
            SnippetService.getByCode(code as string)
                          .then((response) => {
                            if (response.status && response.data?.code) {
                              const instance = this.selectEditor.editorMaps.get(this.selectEditor.activeKey as string)
                              if (instance) {
                                instance.instance?.setValue(response.data.context)
                              }
                            }
                          })
                          .finally(() => this.loading.froming = false)
          }
          else if (type === 'history') {
            this.loading.froming = true
            this.queryConfigure.configure.mode = 'HISTORY'
            AuditService.getByCode(code as string)
                        .then((response) => {
                          if (response.status && response.data?.content) {
                            const instance = this.selectEditor.editorMaps.get(this.selectEditor.activeKey as string)
                            if (instance) {
                              instance.instance?.setValue(response.data.content)
                              const full = `${ response.data.source.id }:${ response.data.source.type }:${ response.data.source.code }`
                              this.selectSource.full = full
                              this.handlerChangeValue(full)
                            }
                          }
                        })
                        .finally(() => this.loading.froming = false)
          }
        }
      }
    },
    handlerEditorDidMount(editor: Editor, _language: string, key?: string | null)
    {
      if (key) {
        const instance = this.selectEditor.editorMaps.get(key as string)
        if (instance) {
          instance.instance = editor
        }
      }
      // Initializes the completer
      this.handlerInitializeCompleter(editor, _language)
    },
    handlerChangeValue(value: string)
    {
      const idAndType = value.split(':')
      this.selectSource.id = idAndType[0]
      this.selectSource.type = idAndType[1]
      this.selectSource.engine = idAndType[1]
      this.selectSource.code = idAndType[2]
      const instance = this.selectEditor.editorMaps.get(this.selectEditor.activeKey as string)
      if (instance) {
        this.handlerEditorDidMount(instance.instance as any, idAndType[1])
      }
    },
    handlerPlusEditor()
    {
      this.responseConfigure.message = null
      this.createEditor()
    },
    handlerMinusEditor(targetKey: string, index: number)
    {
      const keys = Array.from(this.selectEditor.editorMaps.keys())
      if (index > 0 && index < keys.length) {
        const previousKey = keys[index - 1]
        const instance = this.selectEditor.editorMaps.get(previousKey)
        if (instance) {
          this.selectEditor.editorInstance = instance
          this.selectEditor.activeKey = previousKey
          this.selectEditor.isSelection = false
        }
      }
      this.selectEditor.editorMaps.delete(targetKey)
    },
    handlerChangeEditor(value: any)
    {
      this.responseConfigure.message = null
      this.queryConfigure.queryType = [HelpType.ANALYSIS, HelpType.OPTIMIZE]
      const instance = this.selectEditor.editorMaps.get(value)
      if (instance) {
        this.selectEditor.editorInstance = instance
        this.selectEditor.activeKey = value
        this.selectEditor.isSelection = false
      }
    },
    handlerInitializeCompleter(editor: Editor, language: string)
    {
      try {
        // The configuration editor selects content events
        editor.selection.on('changeSelection', () => {
          const selection = editor.getSelection()
          if (selection.isEmpty()) {
            this.selectEditor.isSelection = false
          }
          else {
            this.selectEditor.isSelection = true
          }
        })

        langTools.addCompleter({
          // @ts-ignore
          getCompletions: function (editor: any, session: any, pos: any, prefix: any, callback: (arg0: null, arg1: never[]) => any) {
            return callback(null, [])
          }
        })

        // Clear default keywords and code snippets
        editor.completers = []
        const that = this
        const client = new HttpUtils().getAxios()
        const filter = new FilterModel()
        client.all([FunctionService.getByPlugin(language.toLowerCase()), SnippetService.getAll(filter)])
              .then(client.spread((keyword: any, snippet: any) => {
                if (keyword.status) {
                  const keywordCompleter = {
                    // @ts-ignore
                    getCompletions: function (editor, session, pos, prefix, callback) {
                      return callback(null, keyword.data.content.map(function (item: { example: string; name: string; type: any; description: string; }) {
                        return {
                          value: item.example,
                          caption: item.name,
                          meta: that.$t('function.common.' + item.type.toLowerCase()),
                          docHTML: '<div>' +
                              '<strong>' + item.name + '</strong><br/><hr/>'
                              + that.$t('common.description') + ':\n' + item.description + '<br/><hr/>'
                              + that.$t('function.common.example') + ':\n' + item.example + '<br/><hr/>'
                              + '</div>'
                        }
                      }))
                    }
                  }
                  editor.completers.push(keywordCompleter)
                }
                if (snippet.status) {
                  const snippetCompleter = {
                    // @ts-ignore
                    getCompletions: function (editor, session, pos, prefix, callback) {
                      return callback(null, snippet.data.content.map(function (item: { context: any; name: string; description: string; }) {
                        return {
                          value: item.context,
                          caption: item.name,
                          meta: that.$t('common.snippet'),
                          docHTML: '<div>' +
                              '<strong>' + item.name + '</strong><br/><hr/>'
                              + that.$t('common.description') + ':\n' + item.description + '<br/><hr/>'
                              + '</div>'
                        }
                      }))
                    }
                  }
                  editor.completers.push(snippetCompleter)
                }
              }))
      }
      catch (e) {
        console.error(e)
      }
    },
    handlerRun()
    {
      this.responseConfigure.gridConfigure = null
      this.responseConfigure.response = null
      this.responseConfigure.message = null
      this.queryConfigure.queryType = [HelpType.ANALYSIS, HelpType.OPTIMIZE]
      this.queryConfigure.cancelToken = axios.CancelToken.source()
      const editorContainer: HTMLElement = this.$refs.editorContainer as HTMLElement
      const editorInstance = this.selectEditor.editorInstance
      //   limit: this.queryLimit,
      if (editorInstance && this.queryConfigure.configure) {
        const content = this.selectEditor.isSelection ? editorInstance.instance?.getSelectedText() : editorInstance.instance?.getValue()
        this.queryConfigure.configure.content = content as string
        this.queryConfigure.configure.name = this.selectSource.id as string
      }

      this.loading.running = true
      ExecuteService.execute(this.queryConfigure.configure!, this.queryConfigure.cancelToken.token)
                    .then((response) => {
                      if (response.status) {
                        if (editorInstance) {
                          const content = this.selectEditor.isSelection ? editorInstance.instance?.getSelectedText() : editorInstance.instance?.getValue()
                          this.responseConfigure.response = response
                          const tConfigure: GridConfigure = {
                            headers: response.data.headers,
                            columns: response.data.columns,
                            height: 340,
                            width: editorContainer.offsetWidth + 20,
                            showSeriesNumber: false,
                            sourceId: this.selectSource.id as unknown as number,
                            query: content,
                            code: this.selectSource.code as string
                          }
                          this.responseConfigure.gridConfigure = tConfigure
                          editorInstance.instance?.setValue(response.data.content)
                        }
                      }
                      else {
                        ToastUtils.error(response.message)
                        this.responseConfigure.message = response.message
                        this.queryConfigure.queryType.push(HelpType.FIXEDBUGS)
                        this.responseConfigure.gridConfigure = null
                      }
                    })
                    .finally(() => this.loading.running = false)
    },
    handlerCancel()
    {
      this.queryConfigure.cancelToken.cancel('Cancel query')
    },
    handlerFormat()
    {
      const editorInstance = this.selectEditor.editorInstance
      if (editorInstance) {
        this.loading.formatting = true
        const configure = { sql: editorInstance.instance?.getValue() }
        FormatService.formatSql(configure)
                     .then((response) => {
                       if (response.status) {
                         editorInstance.instance?.setValue(response.data)
                         this.selectEditor.isSelection = false
                       }
                       else {
                         ToastUtils.error(response.message)
                       }
                     })
                     .finally(() => this.loading.formatting = false)
      }
    },
    handlerQueryHelp(value: boolean)
    {
      this.visibility.queryHelp = value
    },
    handlerSnippet(opened: boolean)
    {
      const editorInstance = this.selectEditor.editorInstance
      this.dataInfoVisible = opened
      if (editorInstance) {
        const content = this.selectEditor.isSelection ? editorInstance.instance?.getSelectedText() : editorInstance.instance?.getValue()
        this.dataInfo = SnippetRequest.of()
        this.dataInfo.code = content as string
      }
    },
    createEditor()
    {
      const localEditorConfigure = localStorage.getItem(Common.userEditorConfigure)
      const defaultEditorConfigure: UserEditor = { fontSize: 12, theme: 'chrome' }
      this.selectEditor.configure = localEditorConfigure ? JSON.parse(localEditorConfigure) : defaultEditorConfigure
      const defaultEditor: EditorInstance = { title: 'New Query', key: Date.now().toString(), configure: this.selectEditor.configure as any }
      this.selectEditor.activeKey = defaultEditor.key
      this.selectEditor.editorMaps.set(defaultEditor.key, defaultEditor)
      this.selectEditor.editorInstance = defaultEditor
      this.selectEditor.isSelection = false
    }
  }
})
</script>
