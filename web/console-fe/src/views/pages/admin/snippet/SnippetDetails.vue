<template>
  <a-drawer :title="$t('common.create') + $t('common.snippet')" :width="720" :closable="false"
            :visible="visible" :body-style="{ paddingBottom: '80px' }" :footer-style="{ textAlign: 'right' }">
    <a-form :model="snippetForm" :rules="rules" layout="vertical">
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
    <template #extra>
      <a-space>
        <a-button @click="handlerCancel()">{{ $t('common.cancel') }}</a-button>
        <a-button type="primary" @click="handlerSave()" :disabled="disabled">{{ $t('common.submit') }}</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>
<script lang="ts">
import {computed, defineComponent, reactive} from 'vue';
import type {Rule} from 'ant-design-vue/es/form';
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
      default: () => ''
    },
    id: {
      type: Number,
      default: () => 0
    }
  },
  setup()
  {
    const snippetForm = reactive<Snippet>({
      name: '',
      description: '',
      code: ''
    });

    const rules: Record<string, Rule[]> = {
      name: [{required: true, message: 'Please enter name'}],
      description: [{required: true, message: 'Please enter description'}],
      code: [{required: true, message: 'Please enter the code'}]
    };

    const disabled = computed(() => {
      return !(snippetForm.name && snippetForm.description && snippetForm.code);
    });
    return {
      snippetForm,
      rules,
      disabled
    };
  },
  created()
  {
    this.snippetForm.code = this.codeSnippet;
  },
  methods: {
    handlerSave()
    {
      const isUpdate = this.id === 0 ? false : true;
      new SnippetService()
        .saveAndUpdate(this.snippetForm, isUpdate)
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
