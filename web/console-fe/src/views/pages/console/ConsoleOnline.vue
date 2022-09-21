<template>
  <div class="home">
    <div ref="editorContainer">
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
    </div>
    <div style="margin-top: 5px;">
      <a-card :loading="tableLoading" :body-style="{padding: '2px'}">
        <SheetComponent :dataCfg="tableConfigure" :options="tableOptions" :showPagination="true" sheetType="table"/>
      </a-card>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import * as monaco from "monaco-editor";
import {message, SelectProps} from "ant-design-vue";
import {ExecuteModel} from "@/model/ExecuteModel";
import {ExecuteService} from "@/services/ExecuteService";
import "@antv/s2-vue/dist/style.min.css";
import "ant-design-vue/dist/antd.css";
import {SheetComponent} from "@antv/s2-vue";

let codeEditor!: monaco.editor.IStandaloneCodeEditor;

export default defineComponent({
  name: "DashboardConsoleView",
  components: {SheetComponent},
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
      tableConfigure: {},
      tableOptions: {},
      tableLoading: false
    }
  },
  methods: {
    handlerRun()
    {
      this.tableLoading = true;
      const editorContainer: HTMLElement = this.$refs.editorContainer as HTMLElement;
      const configure: ExecuteModel = {
        name: this.applySource,
        content: codeEditor.getValue(),
        format: "JSON"
      };
      new ExecuteService()
        .execute(configure)
        .then((response) => {
          if (response.status) {
            this.tableConfigure = {
              fields: {
                columns: response.data.headers
              },
              data: response.data.columns
            };
            this.tableOptions = {
              width: editorContainer.offsetWidth - 8,
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
          }
        }).finally(() => {
        this.tableLoading = false;
      })
    }
  }
});
</script>
