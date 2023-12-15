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
                      @on-select-change="handlerSelectNode"
                      @on-contextmenu="handlerContextMenu">
                  <template #contextMenu>
                    <Dropdown v-if="contextData?.level === DataStructureEnum.TABLE"
                              placement="right-start"
                              transfer>
                      <DropdownItem>
                        <FontAwesomeIcon icon="circle-plus"/>
                        {{ $t('source.manager.new') }}
                        <Icon type="ios-arrow-forward"/>
                      </DropdownItem>
                      <template #list>
                        <DropdownMenu>
                          <DropdownItem @click="handlerCreateTable(true)">
                            <FontAwesomeIcon icon="table"/>
                            {{ $t('source.manager.newTable') }}
                          </DropdownItem>
                        </DropdownMenu>
                        <DropdownMenu>
                          <DropdownItem @click="handlerCreateColumn(true)">
                            <FontAwesomeIcon icon="columns"/>
                            {{ $t('source.manager.newColumn') }}
                          </DropdownItem>
                        </DropdownMenu>
                      </template>
                    </Dropdown>
                    <br/>
                    <Dropdown v-if="contextData?.level === DataStructureEnum.TABLE"
                              placement="right-start"
                              transfer>
                      <DropdownItem>
                        <FontAwesomeIcon icon="file-export"/>
                        {{ $t('source.manager.exportTable') }}
                        <Icon type="ios-arrow-forward"/>
                      </DropdownItem>
                      <template #list>
                        <DropdownMenu>
                          <DropdownItem @click="handlerExportData(true)">
                            <FontAwesomeIcon icon="table"/>
                            {{ $t('source.manager.exportTableData') }}
                          </DropdownItem>
                        </DropdownMenu>
                      </template>
                    </Dropdown>
                    <DropdownItem v-if="contextData?.level === DataStructureEnum.TABLE"
                                  @click="handlerTruncateTable(true)">
                      <FontAwesomeIcon icon="trash-can"/>&nbsp;
                      {{ $t('source.manager.truncateTable') }}
                    </DropdownItem>
                    <DropdownItem v-if="contextData?.level === DataStructureEnum.TABLE"
                                  @click="handlerDropTable(true)">
                      <FontAwesomeIcon icon="delete-left"/>
                      {{ $t('source.manager.dropTable') }}
                    </DropdownItem>
                  </template>
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
                           :id="applyValue.node.applyId">
                </TableInfo>
              </TabPane>
              <TabPane :label="tabPane.structure"
                       name="structure">
                <TableStructure v-if="applyValue.tabType === 'structure'"
                                :id="applyValue.node.applyId">
                </TableStructure>
              </TabPane>
              <TabPane :label="tabPane.data"
                       name="data">
                <TableData v-if="applyValue.tabType === 'data'"
                           :id="applyValue.node.applyId">
                </TableData>
              </TabPane>
              <TabPane :label="tabPane.statement"
                       name="statement">
                <TableStatement v-if="applyValue.tabType === 'statement'"
                                :id="applyValue.node.applyId">
                </TableStatement>
              </TabPane>
              <TabPane :label="tabPane.erDiagram"
                       name="erDiagram">
                <TableErDiagram v-if="applyValue.tabType === 'erDiagram'"
                                :id="applyValue.node.applyId"
                                :title="applyValue.node.title">
                </TableErDiagram>
              </TabPane>
            </Tabs>
          </Card>
        </template>
      </Split>
    </div>
    <TableTruncate v-if="tableTruncate.visible"
                   :isVisible="tableTruncate.visible"
                   :data="contextData"
                   @close="handlerTruncateTable(false)">
    </TableTruncate>
    <TableDrop v-if="tableDrop.visible"
               :isVisible="tableDrop.visible"
               :data="contextData"
               @close="handlerDropTable(false)">
    </TableDrop>
    <TableExportData v-if="tableExportData.visible"
                     :isVisible="tableExportData.visible"
                     :data="contextData"
                     @close="handlerExportData(false)">
    </TableExportData>
    <TableCreate v-if="tableCreateVisible"
                 :isVisible="tableCreateVisible"
                 :data="contextData"
                 @close="handlerCreateTable(false)">
    </TableCreate>
    <ColumnCreate v-if="columnCreateVisible"
                  :isVisible="columnCreateVisible"
                  :data="contextData"
                  @close="handlerCreateColumn(false)">
    </ColumnCreate>
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
import TableData from "@/views/admin/source/components/TableData.vue";
import {TabPane} from "view-ui-plus";
import TableStatement from "@/views/admin/source/components/TableStatement.vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import TableTruncate from "@/views/admin/source/components/TableTruncate.vue";
import TableDrop from "@/views/admin/source/components/TableDrop.vue";
import TableStructure from "@/views/admin/source/components/TableStructure.vue";
import TableErDiagram from "@/views/admin/source/components/TableErDiagram.vue";
import TableExportData from "@/views/admin/source/components/TableExportData.vue";
import TableCreate from "@/views/admin/source/components/TableCreate.vue";
import ColumnCreate from "@/views/admin/source/components/ColumnCreate.vue";

export default defineComponent({
  name: "SourceManagerBeta",
  computed: {
    DataStructureEnum()
    {
      return DataStructureEnum
    }
  },
  components: {
    ColumnCreate,
    TableExportData,
    TableErDiagram,
    TableStructure,
    TableDrop,
    TableTruncate,
    TableCreate,
    FontAwesomeIcon,
    TableStatement,
    TabPane,
    TableData,
    TableInfo,
    CircularLoading
  },
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
        node: null as DataStructureModel,
        tabType: 'info'
      },
      tabPane: {
        info: (h) => this.resolveTabPaneComponent(h, 'circle-info', 'common.info'),
        structure: (h) => this.resolveTabPaneComponent(h, 'columns', 'source.manager.structure'),
        data: (h) => this.resolveTabPaneComponent(h, 'table', 'common.data'),
        statement: (h) => this.resolveTabPaneComponent(h, 'tablet', 'common.statement'),
        erDiagram: (h) => this.resolveTabPaneComponent(h, 'diagram-predecessor', 'source.manager.erDiagram'),
      },
      contextData: null,
      tableTruncate: {
        visible: false
      },
      tableDrop: {
        visible: false
      },
      tableExportData: {
        visible: false
      },
      tableCreateVisible: false,
      columnCreateVisible: false
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
              database: { name: null, id: null };
            }) => {
              const structure = new DataStructureModel();
              structure.title = item.name;
              structure.database = item.database.name;
              structure.databaseId = item.database.id;
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
              table: { name: null, database: { name: null, id: null } };
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
        if (this.applyValue.node) {
          this.applyValue.node.selected = true;
        }
        currentNode.selected = false;
        return;
      }
      this.applyValue.node = currentNode;
    },
    handlerContextMenu(data: any)
    {
      this.contextData = data;
    },
    handlerTruncateTable(isOpen: boolean)
    {
      this.tableTruncate.visible = isOpen;
    },
    handlerDropTable(isOpen: boolean)
    {
      this.tableDrop.visible = isOpen;
      if (!isOpen) {
        this.handlerChangeDatabase();
      }
    },
    handlerExportData(isOpen: boolean)
    {
      this.tableExportData.visible = isOpen;
    },
    handlerCreateTable(isOpen: boolean)
    {
      this.tableCreateVisible = isOpen;
    },
    handlerCreateColumn(isOpen: boolean)
    {
      this.columnCreateVisible = isOpen;
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
        if (extra) {
          title = `${title} (${extra.replace('_', ' ')})`;
        }
        else {
          title = `${title}`;
        }
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

/deep/ .ivu-tabs-nav .ivu-tabs-tab {
  text-align: center;
}
</style>
