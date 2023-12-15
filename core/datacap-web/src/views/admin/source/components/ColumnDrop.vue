<template>
  <div>
    <Modal v-model="visible"
           :title="$t('source.manager.dropColumn') + ' [ ' + data.title + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <CircularLoading v-if="loading"
                       :show="loading"
                       style="margin-top: 150px;">
      </CircularLoading>
      <div v-else>
        <VAceEditor lang="mysql"
                    :theme="configure.theme"
                    :style="{height: '100px', fontSize: configure.fontSize + 'px'}"
                    :value="statement"
                    :options="{readOnly: true}">
        </VAceEditor>
      </div>
      <template #footer>
        <Button type="error"
                @click="handlerInitialize(false)">
          <FontAwesomeIcon icon="delete-left"/>
          {{ $t('source.manager.dropColumn') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import TableService from "@/services/Table";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import '@/ace-editor-theme';
import {SqlType, TableFilter} from "@/model/TableFilter";
import {DataStructureModel} from "@/model/DataStructure";
import {EditorConfigure} from "@/model/User";
import {VAceEditor} from 'vue3-ace-editor';
import Common from "@/common/Common";

export default defineComponent({
  name: "ColumnDrop",
  components: {CircularLoading, VAceEditor},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: DataStructureModel
    }
  },
  created()
  {
    this.configure = Common.getEditorConfigure;
    this.handlerInitialize(true);
    this.watchData();
  },
  data()
  {
    return {
      loading: false,
      statement: null,
      configure: {} as EditorConfigure
    }
  },
  methods: {
    handlerInitialize(preview: boolean)
    {
      this.loading = true;
      const configure: TableFilter = new TableFilter();
      configure.type = SqlType.DROP;
      configure.preview = preview;
      configure.columnId = this.data.applyId;
      TableService.manageColumn(this.data.tableId, configure)
        .then(response => {
          if (response.data) {
            if (response.data?.isSuccessful) {
              if (preview) {
                this.statement = response.data.content;
              }
              else {
                this.$Message.success(this.$t('source.manager.dropColumn') + ' [ ' + this.data.title + ' ] ' + this.$t('common.success'));
                this.handlerCancel();
              }
            }
            else {
              this.$Message.error(response.data?.message);
            }
          }
        })
        .finally(() => this.loading = false)
    },
    handlerCancel()
    {
      this.visible = false;
    },
    watchData()
    {
      watch(
        () => this.data,
        () => {
          this.handlerInitialize(true);
        }
      );
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
