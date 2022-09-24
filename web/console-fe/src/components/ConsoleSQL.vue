<template>
  <a-modal v-model:visible="visible" title="SQL" :footer="null" width="60%" @cancel="handlerCancel()">
    <MonacoEditor theme="vs" :options="{theme: 'vs-dark', fontSize: 15}" language="sql" :height="300"
      v-model:value="localContent">
    </MonacoEditor>
  </a-modal>
</template>
<script lang="ts">
import MonacoEditor from 'monaco-editor-vue3';
import { defineComponent } from "vue";

export default defineComponent({
  name: "ConsoleSQLComponent",
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    content: {
      type: String,
      default: () => ''
    }
  },
  components: { MonacoEditor },
  data() {
    return {
      localContent: this.content
    }
  }
  ,
  methods: {
    handlerCancel() {
      this.visible = false;
    }
  },
  computed: {
    visible: {
      get(): boolean {
        return this.isVisible;
      },
      set(value: boolean) {
        this.$emit('close', value);
      }
    }
  }
});
</script>

