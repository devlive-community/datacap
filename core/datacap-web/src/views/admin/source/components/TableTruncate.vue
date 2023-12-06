<template>
  <div>
    <Modal v-model="visible"
           :title="$t('source.manager.truncateTable') + ' [ ' + data.title + ' ]'"
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
          <FontAwesomeIcon icon="trash"/>
          {{ $t('source.manager.truncateTable') }}
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
import {Button} from "view-ui-plus";

export default defineComponent({
  name: "TableTruncate",
  components: {Button, CircularLoading, VAceEditor},
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
      configure.type = SqlType.TRUNCATE;
      configure.preview = preview;
      TableService.getData(this.data.applyId, configure)
        .then(response => {
          if (response.status) {
            if (preview) {
              this.statement = response.data.content;
            }
            else {
              this.$Message.success(this.$t('source.manager.truncateTable') + ' [ ' + this.data.title + ' ] ' + this.$t('common.success'));
              this.handlerCancel();
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
          this.handlerInitialize();
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
