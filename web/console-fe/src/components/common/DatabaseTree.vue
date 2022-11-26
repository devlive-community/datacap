<template>
  <div style="max-height: 500px; max-width: 200px; overflow: auto;">
    <SkeletonItem v-if="loading" :animated="true" type="rect" size="large" style="width: 165px;"/>
    <Tree v-else :data="treeData" :render="renderContent"/>
  </div>
</template>
<script lang="ts">
import {defineComponent, resolveComponent, watch} from "vue";
import {ExecuteModel} from "@/model/ExecuteModel";
import {ExecuteService} from "@/services/ExecuteService";
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
    renderContent(h, {data})
    {
      return h('span',
        {
          style: {
            display: 'inline-block',
            width: '100%'
          }
        },
        [
          h(resolveComponent('Ellipsis'), {
            text: data.title,
            length: 15,
            tooltip: true,
            onClick: () => {
              this.handlerCopy(data.database, data.table, data.value, data.dataType);
            }
          })
        ]);
    },
    handlerInitialize()
    {
      this.treeData = [];
      const sql = new DatabaseService().sql(this.type);
      const configure: ExecuteModel = {
        name: this.id,
        content: sql,
        format: "JSON"
      };
      if (sql) {
        this.loading = true;
        new ExecuteService()
          .execute(configure, null)
          .then((response) => {
            if (response.status) {
              this.treeData = new TreeService().getTree(response);
            }
            else {
              // // message.error(response.message);
            }
          })
          .finally(() => {
            this.loading = false;
          });
      }
      else {
        // // message.error('Not Supported!')
      }
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
      toClipboard(text).finally(() => this.$Message.success('Copy successful!'));
    }
  }
});
</script>
