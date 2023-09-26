<template>
  <div style="padding: 0">
    <CircularLoading v-if="loading"
                     :show="loading">
    </CircularLoading>
    <div v-else
         ref="splitContainer"
         class="split-container">
      <Split v-model="splitValue"
             :min="0.15">
        <template #left>
          <div ref="splitContainerLeftPane"
               class="split-container-pane">
            <Card style="width:100%;"
                  :padding="0"
                  :bordered="false"
                  dis-hover>
              <template #title>
                <Select v-model="applyValue.database"
                        @on-change="handlerChangeDatabase">
                  <Option v-for="item in databaseArray"
                          :value="item.applyId"
                          :key="item.title">
                    <FontAwesomeIcon icon="database"
                                     style="margin-right: 6px;">
                    </FontAwesomeIcon>
                    {{ item.title }}
                  </Option>
                </Select>
              </template>
              <div style="height: 470px; overflow: auto;">
                <Tree :data="dataTreeArray"
                      :load-data="handlerLoadChildData">
                </Tree>
                <CircularLoading v-if="dataTreeLoading"
                                 :show="dataTreeLoading">
                </CircularLoading>
              </div>
            </Card>
          </div>
        </template>
      </Split>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent, resolveComponent} from "vue";
import {useRouter} from "vue-router";
import {toNumber} from "lodash";
import {useI18n} from "vue-i18n";
import DatabaseService from "@/services/Database";
import TableService from "@/services/Table";
import ColumnService from "@/services/Column";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {DataStructureModel} from "@/model/DataStructure";
import {DataStructureEnum} from "@/enum/DataStructure";

export default defineComponent({
  name: "SourceManagerBeta",
  components: {CircularLoading},
  setup()
  {
    const i18n = useI18n();
    return {
      i18n
    }
  },
  data()
  {
    return {
      loading: false,
      dataTreeLoading: false,
      splitValue: 0.20,
      databaseArray: Array<DataStructureModel>(),
      dataTreeArray: Array<DataStructureModel>(),
      applyValue: {
        database: null
      }
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      const router = useRouter();
      const id = router.currentRoute?.value?.params['id'];
      DatabaseService.getAllBySource(toNumber(id))
        .then(response => {
          if (response.status) {
            response.data.forEach((item: { name: null; catalog: null; id: null }) => {
              const structure = new DataStructureModel();
              structure.title = item.name;
              structure.catalog = item.catalog;
              structure.applyId = item.id;
              this.databaseArray.push(structure);
            });
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handlerChangeDatabase()
    {
      this.dataTreeLoading = true;
      this.dataTreeArray = [];
      TableService.getAllByDatabase(this.applyValue.database)
        .then(response => {
          if (response.status) {
            response.data.forEach((item: { name: null; title: null; catalog: null; id: null; type: null; engine: null; database: { name: null }; }) => {
              const structure = new DataStructureModel();
              structure.title = item.name;
              structure.database = item.database.name;
              structure.catalog = item.catalog;
              structure.applyId = item.id;
              structure.type = item.type;
              structure.level = DataStructureEnum.TABLE;
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
              this.dataTreeArray.push(structure);
            });
          }
        })
        .finally(() => {
          this.dataTreeLoading = false;
        });
    },
    handlerLoadChildData(item: DataStructureModel, callback)
    {
      const dataChildArray = [];
      ColumnService.getAllByTable(item.applyId)
        .then(response => {
          if (response.status) {
            response.data.forEach((item: {
              name: null;
              title: null;
              catalog: null;
              id: null;
              type: null;
              engine: null;
              isKey: null;
              table: { name: null, database: { name: null } };
            }) => {
              const structure = new DataStructureModel();
              structure.title = item.name;
              structure.database = item.table.database.name;
              structure.table = item.table.name;
              structure.catalog = item.catalog;
              structure.applyId = item.id;
              structure.level = DataStructureEnum.COLUMN;
              structure.type = item.type;
              structure.engine = item.engine;
              structure.isKey = item.isKey;
              structure.render = (h, {data}) => {
                return h('div', [
                  h('span', [
                    h(resolveComponent('FontAwesomeIcon'), {
                      icon: this.getColumnIcon(data.isKey),
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
    },
    getColumnIcon(type: string)
    {
      if (type === 'PRI') {
        return 'key';
      }
      else if (type === 'MUL') {
        return 'repeat';
      }
      else if (type === 'UNI') {
        return 'circle';
      }
      else {
        return 'columns';
      }
    }
  }
});
</script>
<style scoped>
.split-container {
  height: 510px;
}

.split-container-pane {
  padding: 0;
}
</style>
