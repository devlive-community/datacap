<template>
  <div>
    <Drawer :title="$t('common.create') + $t('common.snippet')" :width="720" :closable="false"
            v-model="visible" :maskClosable="false" :z-index="9" :styles="{}">
      <Form :model="snippetForm">
        <FormItem :label="$t('common.name')" label-position="top">
          <Input v-model="snippetForm.name"/>
        </FormItem>
        <FormItem :label="$t('common.description')" label-position="top">
          <Input type="textarea" v-model="snippetForm.description" :rows="4"/>
        </FormItem>
        <FormItem :label="$t('common.snippet')" name="code" label-position="top">
          <Input v-model="snippetForm.code" show-word-limit type="textarea" :rows="8"/>
        </FormItem>
      </Form>
      <div class="datacap-drawer-footer">
        <Button style="margin-right: 8px" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
        <Button type="primary" @click="handlerSave()">{{ $t('common.submit') }}</Button>
      </div>
    </Drawer>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive} from 'vue';
import {Snippet} from "@/model/Snippet";
import {SnippetService} from "@/services/SnippetService";

export default defineComponent({
  name: 'SnippetDetails',
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
            this.$Message.success("Create successful");
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
<style scoped>
.datacap-drawer-footer {
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: #fff;
}
</style>
