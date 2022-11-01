<template>
  <div style="max-height: 703px; max-width: 200px; overflow: auto;">
    <a-skeleton v-if="loading" active/>
    <a-tree v-else :tree-data="treeData" loading="true">
      <template #title="{ database, table, value, dataType }">
        <a-button size="small" type="text" @click="handlerCopy(database, table, value, dataType)">{{ value }}</a-button>
      </template>
    </a-tree>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import {ExecuteModel} from "@/model/ExecuteModel";
import {ExecuteService} from "@/services/ExecuteService";
import {message} from "ant-design-vue";
import {DatabaseService} from "@/services/DatabaseService";
import {TreeService} from "@/services/TreeService";
import {TreeData} from "@/model/TreeData";
import useClipboard from 'vue-clipboard3';

const {toClipboard} = useClipboard();

export default defineComponent({
  name: "DatabaseTree",
  props: {
    id: {
      type: String,
      default: () => ''
    },
    type: {
      type: String,
      default: () => ''
    }
  },
  data()
  {
    return {
      treeData: [] as TreeData[],
      loading: false
    }
  },
  created()
  {
    this.handlerInitialize();
    watch(() => this.id, () => {
        this.handlerInitialize();
      }
    );
  },
  methods: {
    handlerInitialize()
    {
      this.treeData = [];
      this.loading = true;
      const configure: ExecuteModel = {
        name: this.id,
        content: new DatabaseService().sql(this.type),
        format: "JSON"
      };
      new ExecuteService()
        .execute(configure, null)
        .then((response) => {
          if (response.status) {
            this.treeData = new TreeService().getTree(response);
          }
          else {
            message.error(response.message);
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handlerCopy(database: string, table: string, value: string, dataType: string)
    {
      let text: string = value;
      switch (dataType) {
        case 'database':
          text = database;
          break;
        case 'table':
          text = database + '.' + table;
          break
        case 'column':
          text = database + '.' + table + '.' + value;
          break
      }
      toClipboard(text).finally(() => message.success('Copy successful!'));
    }
  }
});
</script>
