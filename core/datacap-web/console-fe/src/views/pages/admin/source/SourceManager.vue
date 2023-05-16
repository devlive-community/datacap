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
              <Table ref="selection" :loading="dataLoading" size="small" :columns="headers" :data="columns"></Table>
              <div v-if="!dataLoading" style="text-align: center; margin: 5px 0;">
                <Space>
                  <Button :disabled="currentPage === 0" size="small" icon="md-arrow-back" @click="handlerChangePage(false)"/>
                  <InputNumber v-model="currentPage" min="0" size="small"/>
                  <Button :disabled="columns.length < configure.limit" size="small" icon="md-arrow-forward" @click="handlerChangePage(true)"/>
                </Space>
              </div>
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

export default defineComponent({
  name: "SourceManager",
  components: {SortBy, SourceNotSupported},
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
      currentPage: 1,
      databaseArray: [],
      dataTreeArray: [],
      configure: Sql,
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
      this.currentItem = item;
      this.headers = [];
      this.columns = [];
      if (item.length > 0) {
        this.isShowData = true;
        this.dataLoading = true;
        const data = item[0];
        // Reinitialize when switching to a new table
        if (this.currentTable !== data.title) {
          this.configure = new Sql();
          this.isSort = false;
          this.currentPage = 0;
        }
        if (data.level === 'table') {
          this.currentTable = data.title;
        }
        this.configure.database = this.currentDatabase;
        this.configure.table = this.currentTable;
        this.handlerExecute();
      }
    },
    handlerChangePage(nexted: boolean)
    {
      if (nexted) {
        this.currentPage += 1;
        this.configure.offset = this.currentPage * this.configure.limit;
      }
      else {
        this.currentPage -= 1;
        this.configure.offset = this.currentPage * this.configure.limit;
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
