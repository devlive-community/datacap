<template>
  <div style="min-height: 300px;">
    <Modal v-model="visible"
           width="70%"
           :title="$t('source.manager.previewDML')"
           :closable="false"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <CircularLoading v-if="loading"
                       style="padding: 20px 0px;"
                       :show="loading">
      </CircularLoading>
      <VAceEditor v-else
                  lang="mysql"
                  :theme="configure.theme"
                  :style="{height: '200px', fontSize: configure.fontSize + 'px'}"
                  :value="contentDML"
                  :options="{readOnly: true}">
      </VAceEditor>
      <template #footer>
        <Button danger
                @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button type="danger"
                :loading="loadingChange"
                @click="handlerSave()">
          {{ $t('common.submit') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {VAceEditor} from "vue3-ace-editor";
import TableService from "@/services/Table";
import {EditorConfigure} from "@/model/User";
import '@/ace-editor-theme';
import Common from "@/common/Common";
import {SqlColumn, SqlType, TableFilter} from "@/model/TableFilter";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: "TableRowDeletePreview",
  components: {CircularLoading, VAceEditor},
  props: {
    isVisible: {
      type: Boolean
    },
    tableId: {
      type: Number
    },
    columns: {
      type: Array
    }
  },
  setup()
  {
    const i18n = useI18n();
    return {
      i18n
    }
  },
  created()
  {
    this.configure = Common.getEditorConfigure;
    this.handlerInitialize();
  },
  data()
  {
    return {
      loading: false,
      loadingChange: false,
      configure: {} as EditorConfigure,
      contentDML: null,
      contentConfigure: {} as TableFilter
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      const originalColumns = Array<SqlColumn>();
      this.columns.forEach(item => originalColumns.push({original: item}))
      this.contentConfigure.columns = originalColumns;
      this.contentConfigure.type = SqlType.DELETE;
      this.contentConfigure.preview = true;
      TableService.putData(this.tableId, this.contentConfigure)
        .then(response => {
          if (response.status && response.data) {
            this.contentDML = response.data.content;
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => this.loading = false);
    },
    handlerSave()
    {
      this.loadingChange = false;
      this.contentConfigure.preview = false;
      TableService.putData(this.tableId, this.contentConfigure)
        .then(response => {
          if (response.status && response.data) {
            this.$Message.success(this.i18n.t('source.manager.updateSuccess'));
            this.handlerCancel();
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => this.loadingChange = false);
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
