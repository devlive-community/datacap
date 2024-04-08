<template>
  <VAceEditor v-if="readOnly" lang="mysql" :value="internalValue as string" :theme="configure.theme"
              :style="{ height: '300px', fontSize: configure.fontSize + 'px' }"
              :key="key" :options="{ readOnly: true }"/>
  <VAceEditor v-else lang="mysql" :value="internalValue as string" :theme="configure.theme"
              :style="{ height: '300px', fontSize: configure.fontSize + 'px' }"
              :key="key" :options="{ enableSnippets: true, enableLiveAutocompletion: true }"
              @init="handlerEditorDidMount($event, 'mysql')" @update:value="handlerUpdateValue" @input="handleInput"/>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { VAceEditor } from 'vue3-ace-editor'
import { Ace } from 'ace-builds'
import '@/ace-editor-theme'
import langTools from 'ace-builds/src-noconflict/ext-language_tools'
import { HttpUtils } from '@/utils/http'
import FunctionService from '@/services/function'
import SnippetService from '@/services/snippet'
import Common from '@/utils/common'
import { UserEditor } from '@/model/user'
import Editor = Ace.Editor

export default defineComponent({
  name: 'AceEditor',
  components: {
    VAceEditor
  },
  props: {
    value: {
      type: String
    },
    readOnly: {
      type: Boolean
    }
  },
  emits: ['update:value'],
  setup(props, { emit })
  {
    const key = Date.now().toString()

    const localEditorConfigure = localStorage.getItem(Common.userEditorConfigure)
    const defaultEditorConfigure: UserEditor = { fontSize: 12, theme: 'chrome' }
    const configure = localEditorConfigure ? JSON.parse(localEditorConfigure) : defaultEditorConfigure

    const internalValue = ref(props.value)

    const handlerUpdateValue = (value: string) => {
      emit('update:value', value)
    }

    const handleInput = (value: string) => {
      internalValue.value = value
      handlerUpdateValue(value)
    }

    return {
      key,
      configure,
      internalValue,
      handlerUpdateValue,
      handleInput
    }
  },
  methods: {
    handlerEditorDidMount(editor: Editor, _language: string)
    {
      try {
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
        client.all([FunctionService.getByPlugin(_language.toLowerCase()), SnippetService.getSnippets(0, 100000)])
              .then(client.spread((keyword: any, snippet: any) => {
                if (keyword.status) {
                  const keywordCompleter = {
                    // @ts-ignore
                    getCompletions: function (editor, session, pos, prefix, callback) {
                      return callback(null, keyword.data.content.map(function (item: { example: string; name: string; type: any; description: string; }) {
                        return {
                          value: item.example,
                          caption: item.name,
                          meta: that.$t('common.' + item.type.toLowerCase()),
                          docHTML: '<div>' +
                              '<strong>' + item.name + '</strong><br/><hr/>'
                              + that.$t('common.description') + ':\n' + item.description + '<br/><hr/>'
                              + that.$t('common.example') + ':\n' + item.example + '<br/><hr/>'
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
                      return callback(null, snippet.data.content.map(function (item: { code: any; name: string; description: string; }) {
                        return {
                          value: item.code,
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
    }
  }
})
</script>
