<template>
  <div>
    <SourceNotSupported v-if="!isSupported" :templateName="templateArray" style="margin-top: 50px;"></SourceNotSupported>
    <div v-else>
      <Layout :style="{padding: '0', 'min-height': '500px'}">
        <Sider hide-trigger :style="{background: '#fff'}">
          <Card style="width:100%">
            <template #title>
              <Tooltip transfer :content="data.type">
                <Avatar :src="'/static/images/plugin/' + data.type + '.png'" size="small"/>
                {{ data.name }}
              </Tooltip>
              <Divider style="margin: 10px 0px;"/>
              <Select v-model="currentDatabase" @on-change="handlerChangeDatabase">
                <Option v-for="database in databaseArray" :value="database" :key="database">
                  {{ database }}
                </Option>
              </Select>
            </template>
            <div style="max-height: 500px; max-width: 200px; overflow: auto;">
              <Tree :data="dataTreeArray" :load-data="handlerLoadChild" @on-select-change="handlerSelectNode"></Tree>
              <Spin size="large" fix :show="tableLoading"></Spin>
            </div>
            <Spin size="large" fix :show="loading"></Spin>
          </Card>
        </Sider>
        <Layout :style="{padding: '0 2px', background: '#fff'}">
          <Content :style="{minHeight: '280px', background: '#fff'}">
            <Card v-if="!isShowData">
              <Result type="warning">
                <template #desc>
                  {{ $t('alert.managerRequiredTreeData') }}
                </template>
              </Result>
            </Card>
            <Card v-else style="width:100%" :padding="0">
              <template #title>
                <Icon type="md-apps"></Icon>
                {{ currentTable }}
              </template>
              <template #extra>
                <Button size="small" :type="isSort ? 'primary' : 'default'" icon="md-options" @click="handlerSort">{{ $t('common.sort') }}</Button>
              </template>
              <SortBy v-if="isSort" :columns="headers" @getValue="handlerGetValue($event, 'sort')" style="margin: 5px;"></SortBy>
              <div v-if="!dataLoading">
                <Space>
                  <Button :disabled="currentPageNumber === 1" shape="circle" type="text"
                          size="small" icon="md-arrow-back" @click="handlerChangePage(false)"/>
                  <Input v-model="currentPageNumber" size="small" style="max-width: 50px;"/>
                  <Button :disabled="columns.length < configure.limit" shape="circle" type="text"
                          size="small" icon="md-arrow-forward" @click="handlerChangePage(true)"/>
                </Space>
              </div>
              <Table ref="selection" :loading="dataLoading" size="small" :columns="headers" :data="columns"></Table>
            </Card>
          </Content>
        </Layout>
      </Layout>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useRouter} from "vue-router";
import {SourceService} from "@/services/SourceService";
import {toNumber} from "lodash";
import {SourceModel} from "@/model/SourceModel";
import MangerService from "@/services/source/MangerService";
import SourceNotSupported from "@/components/common/SourceNotSupported.vue";
import SortBy from "@/views/pages/admin/source/components/sort/SortBy.vue";
import {Sql} from "@/model/sql/Sql";
import {useI18n} from "vue-i18n";
import {Input} from "view-ui-plus";

export default defineComponent({
  name: "SourceManager",
  components: {Input, SortBy, SourceNotSupported},
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
      isSort: false,
      data: null as SourceModel,
      currentDatabase: null,
      currentTable: null,
      currentItem: null,
      currentPageNumber: 1,
      databaseArray: [],
      dataTreeArray: [],
      configure: null as Sql,
      headers: [],
      columns: []
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
                  title: column['COLUMN_NAME'],
                  level: 'FindColumnType',
                  database: this.currentDatabase,
                  type: 'data',
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
          this.configure.offset = this.currentPageNumber;
          this.isSort = false;
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
      this.headers = [];
      this.columns = [];
      this.dataLoading = true;
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
            this.columns = response.data.columns;
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
    handlerSort()
    {
      this.isSort = !this.isSort;
    },
    handlerGetValue(configure: any, type: string)
    {
      if (type === 'sort') {
        this.configure.sort = configure;
      }
      this.handlerExecute();
    }
  }
});
</script>
