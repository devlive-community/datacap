<template>
  <div style="max-height: 500px; max-width: 200px; overflow: auto;">
    <CircularLoading v-if="loading"
                     :show="loading">
    </CircularLoading>
    <Tree v-else :data="dataTreeArray"
          :load-data="handlerLoadChildData"
          @on-select-change="handlerSelectNode">
    </Tree>
  </div>
</template>
<script lang="ts">
import {DataStructureModel} from "@/model/DataStructure";
import {defineComponent, resolveComponent, watch} from "vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import DatabaseService from "@/services/Database";
import TableService from "@/services/Table";
import ColumnService from "@/services/Column";
import {DataStructureEnum} from "@/enum/DataStructure";

export default defineComponent({
  name: "DataStructureLazyTree",
  components: {CircularLoading},
  props: {
    id: {
      type: Number,
      default: () => 0
    }
  },
  data()
  {
    return {
      loading: false,
      dataTreeArray: Array<DataStructureModel>()
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
      this.dataTreeArray = [];
      this.loading = true;
      DatabaseService.getAllBySource(this.id)
        .then(response => {
          if (response.status) {
            response.data.forEach((item: { name: null; catalog: null; id: null }) => {
              const structure = new DataStructureModel();
              structure.title = item.name;
              structure.catalog = item.catalog;
              structure.applyId = item.id;
              structure.render = (h, {data}) => {
                return h('div', [
                  h('span', [
                    h(resolveComponent('FontAwesomeIcon'), {
                      icon: "database",
                      style: {marginRight: '6px'}
                    }),
                    h('span', data.title)
                  ])
                ]);
              }
              this.dataTreeArray.push(structure);
            });
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handlerLoadChildData(item: DataStructureModel, callback)
    {
      const dataChildArray = [];
      if (item.level === DataStructureEnum.DATABASE) {
        TableService.getAllByDatabase(item.applyId)
          .then(response => {
            if (response.status) {
              response.data.forEach((item: { name: null; title: null; catalog: null; id: null; type: null; engine: null; database: { name: null }; }) => {
                const structure = new DataStructureModel();
                structure.title = item.name;
                structure.database = item.database.name;
                structure.catalog = item.catalog;
                structure.applyId = item.id;
                structure.level = DataStructureEnum.TABLE;
                structure.type = item.type;
                structure.engine = item.engine;
                structure.render = (h, {data}) => {
                  return h('div', [
                    h('span', [
                      h(resolveComponent('FontAwesomeIcon'), {
                        icon: "table",
                        style: {marginRight: '6px'}
                      }),
                      h('span', data.title)
                    ])
                  ]);
                }
                dataChildArray.push(structure);
              });
            }
          })
          .finally(() => {
            callback(dataChildArray);
          });
      }
      else if (item.level === DataStructureEnum.TABLE) {
        ColumnService.getAllByTable(item.applyId)
          .then(response => {
            if (response.status) {
              response.data.forEach((item: { name: null; title: null; catalog: null; id: null; type: null; engine: null; table: { name: null, database: { name: null } }; }) => {
                const structure = new DataStructureModel();
                structure.title = item.name;
                structure.database = item.table.database.name;
                structure.table = item.table.name;
                structure.catalog = item.catalog;
                structure.applyId = item.id;
                structure.level = DataStructureEnum.COLUMN;
                structure.type = item.type;
                structure.engine = item.engine;
                structure.render = (h, {data}) => {
                  return h('div', [
                    h('span', [
                      h(resolveComponent('FontAwesomeIcon'), {
                        icon: "columns",
                        style: {marginRight: '6px'}
                      }),
                      h('span', data.title)
                    ])
                  ]);
                }
                dataChildArray.push(structure);
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
    handlerSelectNode(item: DataStructureModel)
    {
      const target = item[0];
      let text: string = target.title;
      switch (target.level) {
        case DataStructureEnum.TABLE:
          text = target.database + '.' + text;
          break
        case DataStructureEnum.COLUMN:
          text = target.database + '.' + target.table + '.' + text;
          break
      }
      this.$Copy({
        text: text
      });
    }
  }
});
</script>
