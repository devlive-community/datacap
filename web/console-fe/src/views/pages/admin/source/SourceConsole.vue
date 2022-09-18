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
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'name'">
            <a-tooltip placement="topLeft">
              <template #title>{{ record.description }}</template>
              {{ text }}
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'description'">
            <a-tooltip placement="topLeft">
              <template #title>{{ text }}</template>
              {{ text }}
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'type'">
            <a-tooltip placement="topRight">
              <template #title>
                <span v-if="text === 'SOURCE'">用于获取指定的数据源来获取该源数据</span>
                <span v-if="text === 'TRANSFORM'">数据转换(或者称为数据处理)将数据进行转换等操作</span>
                <span v-if="text === 'SINK'">用于将数据源获取到的数据写入到指定位置</span>
              </template>
              <a-tag v-if="text === 'SOURCE'" color="#2db7f5">{{ text }}</a-tag>
              <a-tag v-if="text === 'TRANSFORM'" color="purple">{{ text }}</a-tag>
              <a-tag v-if="text === 'SINK'" color="#108ee9">{{ text }}</a-tag>
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'configure'">
            <a-button type="link" @click="handlerShowConfigure(record.name, record.id)">{{ record.configures.length }}</a-button>
          </template>
        </template>
      </a-table>
    </a-card>

    <SourceInfoView v-if="visibleSourceInfo" :isVisible="visibleSourceInfo" :id="applyId" @close="handlerCloseCreateNew($event)"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {PlusCircleOutlined} from '@ant-design/icons-vue';
import {SourceService} from "@/services/SourceService";
import SourceInfoView from "@/views/pages/admin/source/SourceInfo.vue";

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
  }
];

export default defineComponent({
  name: "SourceConsoleView",
  components: {SourceInfoView, PlusCircleOutlined},
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
    }
  }
});
</script>
