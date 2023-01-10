<template>
  <div>
    <Result v-if="!isSupported" type="error" style="margin-top: 50px;">
      <template #desc>
        {{ $t('alert.currentSourceNotSupportOperator') }}
      </template>
    </Result>
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
              <Tree :data="dataTreeArray" :load-data="handlerLoadColumn" @on-select-change="handlerSelectNode"></Tree>
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
              <Table ref="selection" :loading="dataLoading" :columns="headers" :data="columns"></Table>
              <div v-if="!dataLoading" style="text-align: center; margin: 5px 0;">
                <Space>
                  <Button :disabled="page === 0" size="small" icon="md-arrow-back" @click="handlerChangePage(false)"/>
                  <InputNumber v-model="currentPage" size="small"/>
                  <Button size="small" icon="md-arrow-forward" @click="handlerChangePage(true)"/>
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

export default defineComponent({
  name: "SourceManager",
  data()
  {
    return {
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
      currentPage: 0,
      databaseArray: [],
      dataTreeArray: [],
      size: 10,
      page: 0,
      headers: [],
      columns: []
    }
  },
  created()
  {
    this.handlerInitialize()
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
        });
    },
    handlerChangeDatabase()
    {
      this.tableLoading = true;
      MangerService.getTables(this.sourceId, this.currentDatabase)
        .then(response => {
          if (response.status) {
            const header = response.data.headers[0];
            this.dataTreeArray = [];
            response.data.columns.forEach(column => {
              this.dataTreeArray.push({
                title: column[header],
                level: 'table',
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
    handlerLoadColumn(item, callback)
    {
      this.currentTable = item.title;
      MangerService.getColumns(this.sourceId, this.currentDatabase, item.title)
        .then(response => {
          if (response.status) {
            const header = response.data.headers[0];
            const dataTreeColumnArray = [];
            response.data.columns.forEach(column => {
              dataTreeColumnArray.push({
                title: column[header],
                level: 'column',
                database: this.currentDatabase
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
        if (data.level === 'table') {
          this.currentTable = data.title;
        }
        MangerService.getData(this.sourceId, this.currentDatabase, this.currentTable, this.page, this.size)
          .then(response => {
            if (response.status) {
              if (this.columns.length > 0) {
                this.headers.push({
                  type: 'selection',
                  width: 60,
                  align: 'center'
                });
              }
              response.data.headers.forEach(header => {
                this.headers.push(
                  {
                    title: header,
                    key: header,
                    width: 200,
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
      }
    },
    handlerChangePage(nexted: boolean)
    {
      if (nexted) {
        this.currentPage += 1;
        this.page = this.currentPage * this.size;
      }
      else {
        this.currentPage -= 1;
        this.page = this.currentPage * this.size;
      }
      this.handlerSelectNode(this.currentItem);
    }
  }
});
</script>
