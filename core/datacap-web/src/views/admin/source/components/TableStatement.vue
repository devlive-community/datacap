<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading"
                     style="margin-top: 150px;">
    </CircularLoading>
    <div v-else>
      <VAceEditor lang="mysql"
                  :theme="configure.theme"
                  :style="{height: '420px', fontSize: configure.fontSize + 'px'}"
                  :value="statement"
                  :options="{readOnly: true}">
      </VAceEditor>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import TableService from "@/services/Table";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {VAceEditor} from 'vue3-ace-editor';
import '@/ace-editor-theme';
import Common from "@/common/Common";
import {EditorConfigure} from "@/model/User";
import {SqlType, TableFilter} from "@/model/TableFilter";

export default defineComponent({
  name: "TableStatement",
  components: {CircularLoading, VAceEditor},
  props: {
    id: {
      type: Number,
      default: 0
    }
  },
  created()
  {
    this.configure = Common.getEditorConfigure;
    this.handlerInitialize();
    this.watchId();
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
    handlerInitialize()
    {
      this.loading = true;
      const configure: TableFilter = new TableFilter();
      configure.type = SqlType.SHOW;
      TableService.getData(this.id, configure)
        .then(response => {
          if (response.status) {
            const content = response.data?.columns[0];
            if (content.length > 1) {
              this.statement = content[1];
            }
            else {
              this.statement = content[0];
            }
          }
        })
        .finally(() => this.loading = false)
    },
    watchId()
    {
      watch(
        () => this.id,
        () => {
          this.handlerInitialize();
        }
      );
    }
  }
});
</script>
