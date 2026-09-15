<template>
  <a-layout>
    <a-layout has-sider>
      <a-layout-sider theme="light" style="top: -8px;" :width="280">
        <a-card :bordered="false">
          <SourceSelect v-model="selectSource.full as string" @on-change="onChange($event)"/>

          <MetadataTree v-if="selectSource.code" :code="selectSource.code as string"/>
        </a-card>
      </a-layout-sider>

      <a-layout-content class="min-h-screen flex-1 overflow-hidden">
        <div ref="editorContainer">
          <a-card :bordered="false">
            <template #title>
              <a-space v-if="selectSource.code">
                <a-button type="primary" :loading="loading.running" :disabled="(!selectSource.id && !loading.running) || loading.running" @click="onRun()">
                  <template #icon>
                    <ShadcnIcon icon="Play" :size="15"/>
                  </template>
                  {{ $t('query.common.execute') }}
                </a-button>

                <a-button :loading="loading.formatting"
                          :disabled="(!selectSource.id && !loading.formatting) || loading.formatting"
                          @click="onFormat()">
                  <template #icon>
                    <ShadcnIcon icon="RemoveFormatting" :size="15"/>
                  </template>
                  {{ $t('query.common.format') }}
                </a-button>

                <a-button danger
                          :loading="loading.formatting"
                          :disabled="!selectSource.id || !loading.running"
                          @click="onCancel()">
                  <template #icon>
                    <ShadcnIcon icon="Ban" :size="15"/>
                  </template>
                  {{ $t('common.cancel') }}
                </a-button>

                <a-button v-if="responseConfigure.response" type="primary" @click="visibleSnippet(true)">
                  <template #icon>
                    <ShadcnIcon icon="Plus" :size="15"/>
                  </template>
                  {{ $t('common.snippet') }}
                </a-button>

                <div v-if="responseConfigure.response">
                  <a-tooltip>
                    <template #title>
                      <div class="flex flex-col">
                        <span class="text-xs">
                          {{ $t('query.common.connectionTime') }}
                          {{ responseConfigure.response.data.connection.elapsed }} ms
                        </span>

                        <span class="text-xs">
                          {{ $t('query.common.executionTime') }}
                          {{ responseConfigure.response.data.processor.elapsed }} ms
                        </span>
                      </div>
                    </template>

                    <a-button>
                      <ShadcnIcon icon="Clock" :size="15"/>
                      {{ responseConfigure.response.data.processor.elapsed }} ms
                    </a-button>
                  </a-tooltip>
                </div>

                <a-button v-if="selectSource.id && (responseConfigure.response?.data || !responseConfigure.response?.status)"
                          type="primary" @click="visibleQueryHelp(true)">
                  <template #icon>
                    <ShadcnIcon icon="Bot" :size="15"/>
                  </template>

                  {{ $t('query.common.help') }}
                </a-button>

                <a-button :disabled="!selectSource.code" @click="onPlusEditor">
                  <template #icon>
                    <ShadcnIcon icon="Pencil" :size="15"/>
                  </template>

                  {{ $t('common.createEditor') }}
                </a-button>
              </a-space>
            </template>

            <div v-if="!selectSource.code" class="flex justify-center" style="height: 300px">
              <div class="flex items-center justify-center">
                <a-alert :message="$t('source.tip.selected')"/>
              </div>
            </div>

            <a-tabs v-else
                    v-model:activeKey="activeEditor"
                    type="editable-card"
                    hide-add
                    @edit="onEditTabs">
              <a-tab-pane v-for="item in Object.values(editors)"
                          :key="item.key"
                          :tab="item.title"
                          :closable="Object.keys(editors).length > 1">
                <ShadcnCodeEditor v-model="item.content"
                                  :config="{language: 'sql', ...editorConfig}"
                                  :auto-complete-config="{
                                      endpoint: `${baseUrl ? baseUrl : ''}/api/v1/metadata/${selectSource.code}/suggests`,
                                      method: 'GET',
                                      trigger: ['.', '@'],
                                      headers: { 'Authorization': auth?.type + ' ' + auth?.token },
                                      requestParams: (context) => ({
                                          keyword: context.word
                                      }),
                                      transform: (response: any) => {
                                        return response[0].data.columns.map((item: any) => ({
                                          label: item.object_name,
                                          insertText: item.object_name,
                                          detail: $t(item.object_key),
                                          icon: item.object_type
                                        }))
                                      }
                                  }">
                </ShadcnCodeEditor>
              </a-tab-pane>
            </a-tabs>
          </a-card>
        </div>

        <a-layout-content class="mt-1">
          <GridTable v-if="responseConfigure.gridConfigure" :configure="responseConfigure.gridConfigure"/>
        </a-layout-content>
      </a-layout-content>
    </a-layout>
  </a-layout>

  <QueryHelp v-if="visibility.queryHelp"
             :is-visible="visibility.queryHelp"
             :content="editors[activeEditor]?.content"
             :help-type="queryConfigure.queryType"
             :engine="selectSource.engine as string"
             :message="responseConfigure.message as string"
             @close="visibleQueryHelp(false)">
  </QueryHelp>

  <SnippetInfo v-if="dataInfoVisible"
               :is-visible="dataInfoVisible"
               :info="dataInfo"
               @close="visibleSnippet(false)">
  </SnippetInfo>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import SourceSelect from '@/views/components/source/SourceSelect.vue'
import SnippetService from '@/services/snippet'
import AuditService from '@/services/audit'
import { ExecuteModel } from '@/model/execute'
import ExecuteService from '@/services/execute'
import GridTable from '@/views/components/grid/GridTable.vue'
import { GridConfigure } from '@/views/components/grid/GridConfigure'
import { ResponseModel } from '@/model/response'
import FormatService from '@/services/format'
import { HelpType } from '@/views/pages/admin/query/HelpType'
import QueryHelp from '@/views/pages/admin/query/QueryHelp.vue'
import MetadataTree from '@/views/components/tree/MetadataTree.vue'
import { SnippetModel, SnippetRequest } from '@/model/snippet'
import SnippetInfo from '@/views/pages/admin/snippet/SnippetInfo.vue'
import { TokenUtils } from '@/utils/token.ts'
import axios from 'axios'

interface EditorInstance
{
  title: string
  key: string
  content: string
}

defineOptions({ name: 'QueryHome' })

const route = useRoute()
const auth = TokenUtils.getAuthUser()

const editorContainer = ref<HTMLElement | null>(null)

const loading = reactive({
  running: false,
  formatting: false,
  froming: false
})
const visibility = reactive({
  queryHelp: false
})
const selectSource = reactive({
  id: null as string | null | undefined,
  type: null as string | null | undefined,
  engine: null as string | null | undefined,
  code: null as string | null | undefined,
  full: null as string | null
})
const editors = ref<Record<string, EditorInstance>>({})
const activeEditor = ref<string | null>(null)
const editorConfig = {
  fontSize: 12,
  theme: 'chrome'
}
const queryConfigure = reactive({
  configure: null as ExecuteModel | null,
  cancelToken: null as any | null,
  queryType: [HelpType.ANALYSIS, HelpType.OPTIMIZE] as HelpType[]
})
const responseConfigure = reactive({
  response: null as ResponseModel | null,
  gridConfigure: null as GridConfigure | null,
  message: null as string | null
})
const dataInfo = ref<SnippetModel>(null as unknown as SnippetModel)
const dataInfoVisible = ref(false)
const baseUrl = import.meta.env.VITE_API_BASE_URL

const createEditor = () => {
  const newEditor: EditorInstance = {
    title: `Query`,
    key: `editor-${ Date.now() }`,
    content: ''
  }

  activeEditor.value = newEditor.key
  editors.value = {
    ...editors.value,
    [newEditor.key]: newEditor
  }
}

const onChange = (value: string) => {
  const idAndType = value.split(':')
  selectSource.id = idAndType[0]
  selectSource.type = idAndType[1]
  selectSource.engine = idAndType[1]
  selectSource.code = idAndType[2]
}

const handlerInitialize = () => {
  createEditor()

  queryConfigure.configure = { name: selectSource.id as string, content: '', mode: 'ADHOC' }
  const params = route.params
  if (params) {
    const code = params.code
    const type = params.type
    if (code && type) {
      if (type === 'snippet') {
        loading.froming = true
        queryConfigure.configure.mode = 'SNIPPET'
        SnippetService.getByCode(code as string)
                      .then((response) => {
                        if (response.status && response.data?.code) {
                          const active = editors.value[activeEditor.value as string]
                          active.content = response.data.context
                        }
                      })
                      .finally(() => (loading.froming = false))
      }
      else if (type === 'history') {
        loading.froming = true
        queryConfigure.configure.mode = 'HISTORY'
        AuditService.getByCode(code as string)
                    .then((response) => {
                      if (response.status && response.data) {
                        const active = editors.value[activeEditor.value as string]
                        active.content = response.data.content
                        const full = `${ response.data.source.id }:${ response.data.source.type }:${ response.data.source.code }`
                        selectSource.full = full
                        onChange(full)
                      }
                    })
                    .finally(() => (loading.froming = false))
      }
    }
  }
}

const onPlusEditor = () => {
  responseConfigure.message = null
  createEditor()
}

const onEditTabs = (targetKey: any, action: string) => {
  if (action === 'remove') {
    const keys = Object.keys(editors.value)
    if (keys.length <= 1) {
      return
    }
    const next = { ...editors.value }
    delete next[targetKey]
    editors.value = next
    if (activeEditor.value === targetKey) {
      activeEditor.value = Object.keys(editors.value)[0]
    }
  }
}

const onRun = () => {
  responseConfigure.gridConfigure = null
  responseConfigure.response = null
  responseConfigure.message = null
  queryConfigure.queryType = [HelpType.ANALYSIS, HelpType.OPTIMIZE]
  queryConfigure.cancelToken = axios.CancelToken.source()
  queryConfigure.configure!.name = selectSource.code as string
  const editor = editors.value[activeEditor.value as string]
  queryConfigure.configure!.content = editor.content
  const container = editorContainer.value as HTMLElement

  loading.running = true
  ExecuteService.execute(queryConfigure.configure!, queryConfigure.cancelToken.token)
                .then((response) => {
                  if (response.status) {
                    responseConfigure.response = response
                    responseConfigure.gridConfigure = {
                      headers: response.data.headers,
                      columns: response.data.columns,
                      height: 340,
                      width: container.offsetWidth + 20,
                      showSeriesNumber: false,
                      sourceId: selectSource.id as unknown as number,
                      query: editor.content,
                      code: selectSource.code as string
                    }
                    editor.content = response.data.content
                  }
                  else {
                    message.error(response.message)
                    responseConfigure.message = response.message
                    queryConfigure.queryType.push(HelpType.FIXEDBUGS)
                    responseConfigure.gridConfigure = null
                  }
                })
                .finally(() => (loading.running = false))
}

const onCancel = () => {
  queryConfigure.cancelToken.cancel('Cancel query')
}

const onFormat = () => {
  loading.formatting = true

  const active = editors.value[activeEditor.value as string]
  const configure = { sql: active.content }
  FormatService.formatSql(configure)
               .then((response) => {
                 if (response.status) {
                   active.content = response.data
                 }
                 else {
                   message.error(response.message)
                 }
               })
               .finally(() => (loading.formatting = false))
}

const visibleQueryHelp = (value: boolean) => {
  visibility.queryHelp = value
}

const visibleSnippet = (opened: boolean) => {
  const active = editors.value[activeEditor.value as string]
  dataInfoVisible.value = opened
  if (active) {
    const content = active.content
    dataInfo.value = SnippetRequest.of()
    dataInfo.value.context = content as string
  }
}

handlerInitialize()
</script>
