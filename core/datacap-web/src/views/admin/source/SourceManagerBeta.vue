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
                      style="margin-left: 6px;"
                      :load-data="handlerLoadChildData"
                      @on-select-change="handlerSelectNode">
                </Tree>
                <CircularLoading v-if="dataTreeLoading"
                                 :show="dataTreeLoading">
                </CircularLoading>
              </div>
            </Card>
          </div>
        </template>
        <template #right>
          <Card v-if="!applyValue.node"
                padding="0 10"
                :bordered="false"
                dis-hover>
            <Result type="warning"
                    :title="$t('tooltip.notSelectedNodeTitle')">
              <template #desc>
                <span>{{ $t('tooltip.notSelectedNodeDesc') }}</span>
              </template>
            </Result>
          </Card>
          <Card v-else
                style="width:100%;"
                padding="0 10"
                :bordered="false"
                :title="null"
                dis-hover>
            <Tabs v-model="applyValue.tabType"
                  :animated="false">
              <TabPane :label="tabPane.info"
                       name="info">
                <TableInfo v-if="applyValue.tabType === 'info'"
                           :info="applyValue.node">
                </TableInfo>
              </TabPane>
            </Tabs>
          </Card>
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
import TableInfo from "@/views/admin/source/components/TableInfo.vue";

export default defineComponent({
  name: "SourceManagerBeta",
  components: {TableInfo, CircularLoading},
  setup()
  {
    const i18n = useI18n();
    const resolveTabPaneComponent = (h, icon: string, key: string) => {
      return h('div', [
        h(resolveComponent('FontAwesomeIcon'), {
          icon: icon,
          style: {fontSize: '25px'}
        }),
        h('p', {
            style: {
              fontSize: '12px',
            }
          },
          i18n.t(key))
      ])
    }
    return {
      i18n,
      resolveTabPaneComponent
    }
  },
  data()
  {
    return {
      loading: false,
      dataTreeLoading: false,
      splitValue: 0.15,
      databaseArray: Array<DataStructureModel>(),
      dataTreeArray: Array<DataStructureModel>(),
      applyValue: {
        database: null,
        node: null,
        tabType: 'info'
      },
      tabPane: {
        info: (h) => this.resolveTabPaneComponent(h, 'circle-info', 'common.info'),
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
            response.data.forEach((item: {
              name: null;
              title: null;
              catalog: null;
              id: null;
              type: null;
              engine: null;
              comment: null;
              database: { name: null };
            }) => {
              const structure = new DataStructureModel();
              structure.title = item.name;
              structure.database = item.database.name;
              structure.catalog = item.catalog;
              structure.applyId = item.id;
              structure.type = item.type;
              structure.level = DataStructureEnum.TABLE;
              structure.engine = item.engine;
              structure.comment = item.comment;
              structure.origin = item;
              structure.render = (h, {data}) => {
                return h('div', [
                  h('span', [
                    h(resolveComponent('FontAwesomeIcon'), {
                      icon: "table",
                      style: {marginRight: '6px'}
                    }),
                    this.resolveTableComponent(h, data)
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
      if (item.level === DataStructureEnum.COLUMN) {
        callback(dataChildArray);
        return;
      }
      ColumnService.getAllByTable(item.applyId)
        .then(response => {
          if (response.status) {
            response.data.forEach((item: {
              name: null;
              title: null;
              catalog: null;
              id: null;
              type: null;
              dataType: null;
              extra: null;
              engine: null;
              isKey: null;
              defaultValue: null;
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
              structure.extra = item.extra;
              structure.dataType = item.dataType;
              structure.engine = item.engine;
              structure.isKey = item.isKey;
              structure.defaultValue = item.defaultValue;
              structure.render = (h, {data}) => {
                return h('div', [
                  h('span', [
                    h(resolveComponent('FontAwesomeIcon'), {
                      icon: this.getColumnIcon(data.isKey),
                      style: {marginRight: '6px'}
                    }),
                    h('span', data.title),
                    h('span', {
                        style: {
                          marginLeft: '6px',
                          color: '#c5c8ce'
                        },
                      },
                      this.getColumnTitle(data.type, data.extra, data.isKey, data.defaultValue)),
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
    handlerSelectNode(node: Array<DataStructureModel>)
    {
      if (node.length === 0) {
        // Prevent selection clearing after repeated clicks
        this.applyValue.node.selected = true;
        return;
      }
      const currentNode = node[0];
      if (currentNode.level === DataStructureEnum.COLUMN) {
        this.applyValue.node.selected = true;
        currentNode.selected = false;
        return;
      }
      this.applyValue.node = currentNode;
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
    },
    getColumnTitle(dataType: string, extra: string, isKey: string, defaultValue: string)
    {
      let title = dataType;
      if (isKey === 'PRI') {
        title = `${title} (${extra.replace('_', ' ')})`;
      }
      if (defaultValue && defaultValue !== 'null') {
        title = `${title} = ${defaultValue}`
      }
      return title;
    },
    resolveTableComponent(h, data: { comment: undefined; title: undefined })
    {
      if (data.comment) {
        return h(resolveComponent('Tooltip'), {
            content: data.comment,
            placement: 'bottom-start',
            transfer: true,
            delay: 1000
          },
          h('span', data.title));
      }
      else {
        return h('span', data.title);
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
