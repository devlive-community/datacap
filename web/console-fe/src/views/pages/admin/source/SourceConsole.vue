<template>
  <div>
    <a-card title="Source List" size="small">
      <template #extra>
        <a-tooltip>
          <template #title>Create new source</template>
          <a-button type="primary" shape="circle" size="small" @click="handlerCreateNew()">
            <template #icon>
              <plus-circle-outlined/>
            </template>
          </a-button>
        </a-tooltip>
      </template>
      <a-table size="middle" :loading="loading" :columns="headers" :data-source="columns">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'action'">
            <a-space style="width: 100%">
              <a-popconfirm title="Are you sure delete?" ok-text="Yes" cancel-text="No" @confirm="handlerDeleteRecord(record.id)">
                <a-tooltip>
                  <template #title>Delete</template>
                  <a-button type="primary" danger shape="circle" size="small">
                    <template #icon>
                      <minus-outlined/>
                    </template>
                  </a-button>
                </a-tooltip>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <SourceInfoView v-if="visibleSourceInfo" :isVisible="visibleSourceInfo" :id="applyId" @close="handlerCloseCreateNew($event)"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {MinusOutlined, PlusCircleOutlined} from '@ant-design/icons-vue';
import {SourceService} from "@/services/SourceService";
import SourceInfoView from "@/views/pages/admin/source/SourceInfo.vue";
import {message} from "ant-design-vue";

const headers = [
  {
    title: 'No',
    name: 'id',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: 'Name',
    name: 'name',
    dataIndex: 'name',
    key: 'name',
    ellipsis: true
  },
  {
    title: 'Description',
    name: 'description',
    dataIndex: 'description',
    key: 'description',
    ellipsis: true
  },
  {
    title: 'Protocol',
    name: 'protocol',
    dataIndex: 'protocol',
    key: 'protocol',
    ellipsis: true
  },
  {
    title: 'Host',
    name: 'host',
    dataIndex: 'host',
    key: 'host',
    ellipsis: true
  },
  {
    title: 'Port',
    name: 'port',
    dataIndex: 'port',
    key: 'port',
    ellipsis: true
  },
  {
    title: 'User Name',
    name: 'username',
    dataIndex: 'username',
    key: 'username',
    ellipsis: true
  },
  {
    title: 'Catalog',
    name: 'catalog',
    dataIndex: 'catalog',
    key: 'catalog',
    ellipsis: true
  },
  {
    title: 'Database',
    name: 'database',
    dataIndex: 'database',
    key: 'database',
    ellipsis: true
  },
  {
    title: 'Create Time',
    name: 'createTime',
    dataIndex: 'createTime',
    key: 'createTime',
    ellipsis: true
  },
  {
    title: 'Action',
    name: 'action',
    dataIndex: 'action',
    key: 'action'
  }
];

export default defineComponent({
  name: "SourceConsoleView",
  components: {SourceInfoView, PlusCircleOutlined, MinusOutlined},
  data()
  {
    return {
      headers,
      columns: [],
      loading: false,
      visibleSourceInfo: false,
      applyId: 0
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
      new SourceService()
        .getSources()
        .then((response) => {
          if (response.status) {
            this.columns = response.data.content;
          }
          this.loading = false;
        })
    },
    handlerCreateNew()
    {
      this.visibleSourceInfo = true;
    },
    handlerCloseCreateNew(value: boolean)
    {
      this.visibleSourceInfo = value;
      this.applyId = 0;
      this.handlerInitialize();
    },
    handlerDeleteRecord(id: number)
    {
      new SourceService()
        .delete(id)
        .then((response) => {
          if (response.status) {
            message.success("Delete successful");
            this.handlerInitialize();
          }
        });
    }
  }
});
</script>
