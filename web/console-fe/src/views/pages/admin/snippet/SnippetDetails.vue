<template>
  <a-drawer :title="$t('common.create') + $t('common.snippet')" :width="720" :closable="false" :maskClosable="false"
            :visible="visible" :body-style="{ paddingBottom: '80px' }" :footer-style="{ textAlign: 'right' }">
   <a-spin :spinning="loading">
     <a-form :model="snippetForm" layout="vertical">
       <a-row :gutter="16">
         <a-col :span="12">
           <a-form-item has-feedback :label="$t('common.name')" name="name">
             <a-input v-model:value="snippetForm.name"/>
           </a-form-item>
         </a-col>
         <a-col :span="12">
         </a-col>
       </a-row>
       <a-row :gutter="16">
         <a-col :span="24">
           <a-form-item has-feedback :label="$t('common.description')" name="description">
             <a-textarea v-model:value="snippetForm.description" :rows="4"/>
           </a-form-item>
         </a-col>
       </a-row>
       <a-row :gutter="16">
         <a-col :span="24">
           <a-form-item has-feedback :label="$t('common.snippet')" name="code">
             <MonacoEditor theme="vs" :options="{theme: 'vs-dark', fontSize: 15}" language="sql" :height="300"
                           v-model:value="snippetForm.code">
             </MonacoEditor>
           </a-form-item>
         </a-col>
       </a-row>
     </a-form>
   </a-spin>
    <template #extra>
      <a-space>
        <a-button @click="handlerCancel()">{{ $t('common.cancel') }}</a-button>
        <a-button type="primary" @click="handlerSave()">{{ $t('common.submit') }}</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>
<script lang="ts">
import {defineComponent, reactive} from 'vue';
import {Snippet} from "@/model/Snippet";
import MonacoEditor from "monaco-editor-vue3";
import {SnippetService} from "@/services/SnippetService";
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'SnippetDetails',
  components: {MonacoEditor},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    codeSnippet: {
      type: String,
      default: () => null
    },
    id: {
      type: Number,
      default: () => 0
    }
  },
  data()
  {
    return {
      isUpdate: false,
      snippetForm: null as unknown as Snippet,
      loading: false
    }
  },
  created()
  {
    this.snippetForm = reactive<Snippet>({
      name: '',
      description: '',
      code: ''
    });
    this.isUpdate = this.id === 0 ? false : true;
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      if (this.id > 0) {
        this.loading = true;
        new SnippetService().getById(this.id)
          .then(response => {
            if (response.status) {
              this.snippetForm = reactive<Snippet>(response.data);
            }
          })
          .finally(() => {
            this.loading = false;
          });
      }
      if (this.codeSnippet != null) {
        this.snippetForm.code = this.codeSnippet;
      }
    },
    handlerSave()
    {
      new SnippetService()
        .saveAndUpdate(this.snippetForm, this.isUpdate)
        .then((response) => {
          if (response.status) {
            message.success("Create successful");
            this.visible = false;
          }
        });
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
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
});
</script>
