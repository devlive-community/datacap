<template>
  <Modal v-model="visible"
         title="Sql"
         width="70%"
         :closable="false"
         :mask-closable="false"
         @cancel="handlerCancel()">
    <VAceEditor lang="mysql"
                :theme="configure.theme"
                :style="{height: '200px', fontSize: configure.fontSize + 'px'}"
                :value="localContent"
                :options="{readOnly: true}">
    </VAceEditor>
  </Modal>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import Common from "@/common/Common";
import {EditorConfigure} from "@/model/User";
import {VAceEditor} from 'vue3-ace-editor';
import '@/ace-editor-theme';

export default defineComponent({
  name: "SqlDetail",
  components: {VAceEditor},
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
  data()
  {
    return {
      localContent: this.content,
      configure: null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      const localEditorConfigure = localStorage.getItem(Common.userEditorConfigure);
      this.configure = localEditorConfigure ? JSON.parse(localEditorConfigure) : new EditorConfigure();
    },
    handlerCancel()
    {
      this.visible = false;
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible;
      },
      set(value: boolean)
      {
        this.$emit('close', value);
      }
    }
  }
});
</script>
