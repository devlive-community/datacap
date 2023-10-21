<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.editor')"
           :closable="false"
           width="70%"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Divider orientation="left">{{ $t('common.preview') }}</Divider>
      <VAceEditor lang="mysql"
                  :theme="configure.theme"
                  :style="{height: '96px', fontSize: configure.fontSize + 'px'}"
                  :value="sqlContent"
                  :options="{readOnly: true}">
      </VAceEditor>
      <Divider orientation="left">{{ $t('common.configure') }}</Divider>
      <Form :model="configure"
            :label-width="180">
        <FormItem name="fontSize"
                  :label="$t('common.fontSize')">
          <InputNumber v-model="configure.fontSize"/>
        </FormItem>
        <FormItem name="theme"
                  :label="$t('common.theme')">
          <Select v-model="configure.theme"
                  style="width: 200px;">
            <Option v-for="theme in themes"
                    :value="theme"
                    :key="theme">
              {{ theme }}
            </Option>
          </Select>
        </FormItem>
        <CircularLoading v-if="loadingUserInfo"
                         :show="loadingUserInfo">
        </CircularLoading>
      </Form>
      <template #footer>
        <Button danger
                @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button type="primary"
                :loading="loadingChange"
                @click="handlerSave()">
          {{ $t('common.save') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import UserService from "@/services/UserService";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {EditorConfigure} from "@/model/User";
import {VAceEditor} from "vue3-ace-editor";
import themes from './AceEditor';
import Common from "@/common/Common";

export default defineComponent({
  name: 'ChangeEditor',
  components: {VAceEditor, CircularLoading},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    }
  },
  data()
  {
    return {
      sqlContent: 'SELECT *\nFROM `database`.`table`\nGROUP BY `name`\nORDER BY `id`\nLIMIT 100\nOFFSET 1000',
      themes: [],
      loadingUserInfo: false,
      loadingChange: false,
      configure: {} as EditorConfigure
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.themes = themes.themes;
      this.loadingUserInfo = true
      UserService.getInfo()
        .then(response => {
          if (response.data.editorConfigure) {
            this.configure = response.data.editorConfigure;
          }
          else {
            this.configure = new EditorConfigure()
          }
        })
        .finally(() => {
          this.loadingUserInfo = false
        })
    },
    handlerSave()
    {
      this.loadingChange = true;
      UserService.changeEditorConfigure(this.configure)
        .then((response) => {
          if (response.status) {
            this.$Message.success('Success');
            localStorage.setItem(Common.userEditorConfigure, JSON.stringify(this.configure))
            this.handlerCancel()
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.loadingChange = false;
        });
    },
    handlerCancel()
    {
      this.visible = false;
    },
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
