<template>
  <div class="home">
    <a-card size="small">
      <template #title>
        <a-space :size="8">
          <a-select v-model:value="applySource" size="small" style="width: 120px" :options="options2">
            <template #suffixIcon>
              <meh-outlined class="ant-select-suffix"/>
            </template>
          </a-select>
          <a-button type="primary" size="small" @click="handlerRun()">Run</a-button>
        </a-space>
      </template>
      <div id="editor-section" style="height: 300px"></div>
    </a-card>
    <a-card title="Result" :style="{ marginTop: '10px' }">
      <a-table :dataSource="columns" :columns="headers" />
    </a-card>
  </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import * as monaco from "monaco-editor";
import {SelectProps} from "ant-design-vue";
import {ExecuteModel} from "@/model/ExecuteModel";
import {ExecuteService} from "@/services/ExecuteService";

let codeEditor!: monaco.editor.IStandaloneCodeEditor;

export default defineComponent({
  name: "DashboardConsoleView",
  components: {},
  setup()
  {

    function initEditor()
    {
      const container: HTMLElement = document.getElementById("editor-section") as HTMLElement;
      codeEditor = monaco.editor.create(container, {
        value: '',
        language: 'sql',
        theme: 'vs-dark',
        fontSize: 15,
        selectOnLineNumbers: true,
        readOnly: false,
        cursorStyle: 'line',
        autoIndent: 'full',
        quickSuggestionsDelay: 100
      });
    }

    onMounted(() => {
      initEditor();
    })
    const options2 = ref<SelectProps['options']>([
      {
        value: '测试MySQL',
        label: '测试MySQL',
      },
    ]);
    return {options2}
  },
  data()
  {
    return {
      applySource: '',
      headers: [] as any[],
      columns: []
    }
  },
  methods: {
    handlerRun()
    {
      const configure: ExecuteModel = {
        name: this.applySource,
        content: codeEditor.getValue()
      };
      new ExecuteService()
        .execute(configure)
        .then((response) => {
          if (response.status) {
            const _headers = response.data.headers;
            const _types = response.data.types;
            for (let i = 0; i < _headers.length; i++) {
              this.headers.push({
                title: _headers[i] + '(' + _types[i] + ')',
                name: _headers[i],
                dataIndex: _headers[i],
                key: _headers[i],
              });
            }
          }
        })
    }
  }
});
</script>
