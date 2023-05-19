<template>
  <div style="padding: 0">
    <SourceNotSupported v-if="!isSupported" :templateName="templateArray" style="margin-top: 50px;"></SourceNotSupported>
    <div ref="splitContainer" class="split-container">
      <Split v-model="splitModel" :min="0.15">
        <template #left>
          <div ref="splitContainerLeftPane" class="split-container-pane">
            <Card style="width:100%;" :padding="0" :bordered="false" dis-hover>
              <template #title>
                <Row>
                  <Col span="4" style="margin-top: 3px;">
                    <Tooltip v-if="data" transfer :content="data.name" placement="bottom-start">
                      <Avatar :src="'/static/images/plugin/' + data.type + '.png'" size="small"/>
                    </Tooltip>
                  </Col>
                  <Col span="20">
                    <Select v-model="currentDatabase" @on-change="handlerChangeDatabase">
                      <Option v-for="database in databaseArray" :value="database" :key="database">
                        {{ database }}
                      </Option>
                    </Select>
                  </Col>
                </Row>
              </template>
              <div style="height: 470px; overflow: auto;">
                <Tree :data="dataTreeArray" :load-data="handlerLoadChild" @on-select-change="handlerSelectNode"></Tree>
                <Spin size="large" fix :show="tableLoading"></Spin>
              </div>
              <Spin size="large" fix :show="loading"></Spin>
            </Card>
          </div>
        </template>
        <template #right>
          <div ref="splitContainerRightPane" class="split-container-pane">
            <Card v-if="!isShowData" dis-hover :bordered="false">
              <Result type="warning" style="margin-top: 10px;">
                <template #desc>
                  {{ $t('alert.managerRequiredTreeData') }}
                </template>
              </Result>
            </Card>
            <div v-if="isShowData && !dataLoading">
              <!-- Paging related components -->
              <div style="margin: 3px 0px 3px 10px;">
                <Space>
                  <Button :disabled="currentPageNumber === 1" shape="circle" type="text"
                          size="small" icon="md-arrow-back" @click="handlerChangePage(false)"/>
                  <Input v-model="currentPageNumber" size="small" style="max-width: 50px;"/>
                  <Button :disabled="tableConfigure?.columns.length < configure.limit" shape="circle" type="text"
                          size="small" icon="md-arrow-forward" @click="handlerChangePage(true)"/>
                </Space>
              </div>
              <!-- Filter component -->
              <div style="margin: 3px 0px -4px 10px;">
                <Space>
                  <Input v-model="currentOrder.inputValue" size="small" clearable style="width: auto;" @on-enter="handlerGetValue">
                    <template #prepend>
                      <Icon type="md-list"/>
                      ORDER BY
                    </template>
                  </Input>
                </Space>
              </div>
            </div>
            <TablePreview v-if="tableConfigure"
                          :configure="tableConfigure"
                          :sortColumns="tableSortColumns"
                          @on-sorted="handlerOnSorted">
            </TablePreview>
            <Spin size="large" fix :show="dataLoading"></Spin>
          </div>
        </template>
      </Split>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useRouter} from "vue-router";
import {SourceService} from "@/services/SourceService";
import {join, toNumber} from "lodash";
import {SourceModel} from "@/model/SourceModel";
import MangerService from "@/services/source/MangerService";
import {Sql} from "@/model/sql/Sql";
import {useI18n} from "vue-i18n";
import SourceNotSupported from "@/components/common/SourceNotSupported.vue";
import {TableConfigure} from "@/components/table/TableConfigure";
import TablePreview from "@/views/pages/admin/source/components/TablePreview.vue";
import {Sort} from "@/model/sql/Sort";

export default defineComponent({
  name: "SourceManager",
  components: {TablePreview, SourceNotSupported},
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
      templateArray: ['getAllDatabase', 'getAllTablesFromDatabase', 'getAllColumnsFromDatabaseAndTable'],
      sourceId: 0,
      loading: false,
      tableLoading: false,
      dataLoading: false,
      isSupported: false,
      isShowData: false,
      data: null as SourceModel,
      currentDatabase: null,
      currentTable: null,
      currentItem: null,
      currentPageNumber: 1,
      currentOrder: {
        inputValue: null
      },
      databaseArray: [],
      dataTreeArray: [],
      configure: null as Sql,
      splitModel: 0.15,
      tableConfigure: null as TableConfigure,
      tableSortColumns: []
    }
  },
  created()
  {
    this.configure = new Sql();
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      const router = useRouter();
      const id = router.currentRoute?.value?.params['id'];
      this.sourceId = toNumber(id);
      new SourceService().getById(this.sourceId)
        .then(response => {
          if (response.status) {
            if (response.data) {
              this.data = response.data;
              this.isSupported = true;
            }
          }
        })
        .finally(() => {
          this.loading = false;
        });

      MangerService.getDatabases(this.sourceId)
        .then(response => {
          if (response.status) {
            const header = response.data.headers[0];
            response.data.columns.forEach(column => {
              this.databaseArray.push(column[header]);
            });
          }
          else {
            this.isSupported = false;
          }
        });
    },
    handlerChangeDatabase()
    {
      this.tableLoading = true;
      MangerService.findTableTypeByDatabase(this.sourceId, this.currentDatabase)
        .then(response => {
          if (response.status) {
            const header = response.data.headers[0];
            this.dataTreeArray = [];
            response.data.columns.forEach(column => {
              this.dataTreeArray.push({
                title: this.i18n.t('common.' + column[header]),
                level: 'GetDataForTableType',
                action: column[header],
                loading: false,
                type: 'action',
                children: []
              });
            });
          }
        })
        .finally(() => {
          this.tableLoading = false;
        });
    },
    handlerLoadChild(item, callback)
    {
      this.currentTable = item.title;
      // Load all tables under the database according to type
      if (item.level === 'GetDataForTableType') {
        MangerService.getTableDataByDatabaseAndType(this.sourceId, this.currentDatabase, item.action)
          .then(response => {
            if (response.status) {
              const dataTreeColumnArray = [];
              // Add the total amount of data to the parent class header
              item.title = item.title + ' [' + response.data.columns.length + ']';
              response.data.columns.forEach(column => {
                dataTreeColumnArray.push({
                  title: column['TABLE_NAME'],
                  level: 'FindColumnType',
                  database: this.currentDatabase,
                  catalog: column['TABLE_CATALOG'],
                  loading: false,
                  type: 'data',
                  children: []
                });
              });
              callback(dataTreeColumnArray);
            }
            else {
              this.$Message.error(response.message);
            }
          })
          .finally(() => {
            this.tableLoading = false;
          });
      }
      // Gets a collection of related data based on the specified database and data type
      else if (item.level === 'FindColumnType') {
        MangerService.findColumnTypeByDatabaseAndTable(this.sourceId, item.catalog, this.currentTable)
          .then(response => {
            if (response.status) {
              const dataTreeColumnArray = [];
              const types = [];
              response.data.columns.forEach(column => {
                column['COLUMN_TYPE'].split(',').forEach(type => {
                  if (types.indexOf(type) === -1) {
                    dataTreeColumnArray.push({
                      title: this.i18n.t('common.' + type),
                      level: 'GetColumnDataForTableType',
                      action: type,
                      catalog: column['TABLE_CATALOG'],
                      database: this.currentDatabase,
                      table: this.currentTable,
                      loading: false,
                      type: 'action',
                      children: []
                    });
                  }
                  types.push(type);
                })
              });
              callback(dataTreeColumnArray);
            }
            else {
              this.$Message.error(response.message);
            }
          })
          .finally(() => {
            this.tableLoading = false;
          });
      }
      // Gets a collection of related data based on the specified database, table, and data type
      else if (item.level === 'GetColumnDataForTableType') {
        MangerService.getColumnDataByDatabaseAndTableAndType(this.sourceId, item.catalog, item.table, item.action)
          .then(response => {
            if (response.status) {
              const dataTreeColumnArray = [];
              item.title = item.title + ' [' + response.data.columns.length + ']';
              response.data.columns.forEach(column => {
                dataTreeColumnArray.push({
                  title: column['COLUMN_NAME'] + ' [' + column['DATA_TYPE'] + ']',
                  level: 'FindColumnType',
                  database: this.currentDatabase,
                  type: 'data',
                  dataType: column['DATA_TYPE'],
                  children: []
                });
              })
              callback(dataTreeColumnArray);
            }
            else {
              this.$Message.error(response.message);
            }
          })
          .finally(() => {
            this.tableLoading = false;
          });
      }
    },
    handlerSelectNode(item)
    {
      if (item.length > 0) {
        const data = item[0];
        if (data.type === 'action') {
          return
        }
        this.currentItem = item;
        this.headers = [];
        this.columns = [];
        this.isShowData = true;
        this.dataLoading = true;
        // Reinitialize when switching to a new table
        if (this.currentTable !== data.title) {
          this.configure = new Sql();
          this.currentPageNumber = 1;
          this.currentOrder.inputValue = null;
          this.configure.offset = this.currentPageNumber;
          this.tableSortColumns = null;
        }
        this.currentTable = data.title;
        this.configure.database = data.catalog;
        this.configure.table = this.currentTable;
        this.handlerExecute();
      }
    },
    handlerChangePage(nexted: boolean)
    {
      if (nexted) {
        this.configure.offset = this.currentPageNumber * this.configure.limit + 1;
        this.currentPageNumber += 1;
      }
      else {
        this.currentPageNumber -= 1;
        this.configure.offset = (this.currentPageNumber - 1) * this.configure.limit + 1;
      }
      this.handlerSelectNode(this.currentItem);
    },
    handlerExecute()
    {
      this.tableConfigure = null;
      this.dataLoading = true;
      const splitContainerLeftPane: HTMLElement = this.$refs.splitContainerLeftPane as HTMLElement;
      const splitContainer: HTMLElement = this.$refs.splitContainer as HTMLElement;
      MangerService.getDataByConfigure(this.sourceId, this.configure)
        .then(response => {
          if (response.status) {
            response.data.headers.forEach(header => {
              this.headers.push(
                {
                  title: header,
                  key: header,
                  'minWidth': 200,
                  ellipsis: true,
                  tooltip: true
                }
              );
            });
            const tConfigure: TableConfigure = {
              headers: response.data.headers,
              columns: response.data.columns,
              height: splitContainerLeftPane.offsetHeight - 57,
              width: splitContainer.offsetWidth - splitContainerLeftPane.offsetWidth,
              showSeriesNumber: false
            };
            this.tableConfigure = tConfigure;
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.tableLoading = false;
          this.dataLoading = false;
        });
    },
    handlerGetValue()
    {
      const value = this.currentOrder.inputValue;
      if (value) {
        const sort: Array<Sort> = new Array<Sort>();
        value.split(',').forEach(item => {
          const array = item.trim().split(' ')
          sort.push({
            column: array[0],
            sort: array[1]
          })
        })
        this.configure.sort = sort;
      }
      else {
        const sort: Array<Sort> = new Array<Sort>();
        const array = value.trim().split(' ')
        sort.push({
          column: array[0],
          sort: array[1]
        })
        this.configure.sort = sort;
      }
      this.handlerExecute();
    },
    handlerOnSorted(sort: Array<Sort>)
    {
      this.configure.sort = sort;
      this.tableSortColumns = sort;
      this.currentOrder.inputValue = join(sort.map(item => item.column + ' ' + item.sort));
      this.handlerExecute();
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
