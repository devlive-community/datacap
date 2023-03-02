<template>
  <div style="max-height: 500px; max-width: 200px; overflow: auto;">
    <SkeletonItem v-if="loading" :animated="true" type="rect" size="large" style="width: 165px;"/>
    <Tree v-else :data="dataTreeArray" :load-data="handlerLoadChildData" @on-select-change="handlerSelectNode"/>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import MangerService from "@/services/source/MangerService";
import useClipboard from "vue-clipboard3";

const {toClipboard} = useClipboard();

export enum DataTreeLevel
{
  database,
  table,
  column
}

export class DataTree
{
  title: null;
  database: null;
  table: null;
  level: DataTreeLevel = DataTreeLevel.database;
  loading = false;
  children: DataTree[] = [];
}

export default defineComponent({
  name: "DataLazyTree",
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
      loading: false,
      dataTreeArray: []
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
      this.loading = true;
      MangerService.getDatabases(this.id)
        .then(response => {
          if (response.status) {
            const header = response.data.headers[0];
            response.data.columns.forEach(column => {
              const data = new DataTree();
              data.title = column[header];
              this.dataTreeArray.push(data);
            });
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handlerLoadChildData(item: DataTree, callback)
    {
      const dataChildArray = [];
      if (item.level === DataTreeLevel.database) {
        MangerService.getTables(this.id, item.title)
          .then(response => {
            if (response.status) {
              const header = response.data.headers[0];
              response.data.columns.forEach(column => {
                const data = new DataTree();
                data.title = column[header];
                data.database = item.title;
                data.level = DataTreeLevel.table;
                dataChildArray.push(data);
              });
            }
          })
          .finally(() => {
            callback(dataChildArray);
          });
      }
      else if (item.level === DataTreeLevel.table) {
        MangerService.getColumns(this.id, item.database, item.title)
          .then(response => {
            if (response.status) {
              const header = response.data.headers[0];
              response.data.columns.forEach(column => {
                const data = {
                  title: column[header],
                  database: item.database,
                  table: item.title,
                  level: DataTreeLevel.column
                };
                dataChildArray.push(data);
              });
            }
          })
          .finally(() => {
            callback(dataChildArray);
          });
      }
      else {
        callback(dataChildArray);
      }
    },
    handlerSelectNode(item: DataTree)
    {
      const target = item[0];
      let text: string = target.title;
      switch (target.level) {
        case DataTreeLevel.table:
          text = target.database + '.' + text;
          break
        case DataTreeLevel.column:
          text = target.database + '.' + target.table + '.' + text;
          break
      }
      toClipboard(text).finally(() => this.$Message.success('Copy successful!'));
    }
  }
});
</script>
