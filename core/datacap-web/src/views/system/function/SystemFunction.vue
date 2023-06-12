<template>
  <div>
    <Card style="width:100%" :title="$t('common.function')">
      <template #extra>
        <Space>
          <Tooltip>
            <template #content>{{ $t('common.create') }}</template>
            <Button type="primary" shape="circle" icon="md-add" size="small" @click="handlerCreateOrUpdate()"/>
          </Tooltip>
          <Dropdown>
            <a href="javascript:void(0)">
              <Button shape="circle" type="info" size="small" icon="md-more"/>
            </a>
            <template #list>
              <DropdownMenu>
                <DropdownItem @click="handlerImportController(true)">{{ $t('common.import') }}
                </DropdownItem>
              </DropdownMenu>
            </template>
          </Dropdown>
        </Space>
      </template>
      <Table :loading="loading" :columns="headers" :data="data.content" @on-sort-change="handlerSort">
        <template #plugin="{ row }">
          <AvatarList size="small" :list="handlerPluginConvert(row.plugin)" :max="5" :excess-style="{
                    color: '#f56a00',
                    backgroundColor: '#fde3cf'
                }"/>
        </template>
        <template #type="{ row }">
          {{ $t('common.' + row.type.toLowerCase()) }}
        </template>
        <template #action="{ row }">
          <Tooltip :content="$t('common.modify')" transfer>
            <Button shape="circle" type="primary" size="small" icon="md-create"
                    @click="handlerCreateOrUpdate(row.id)"/>
          </Tooltip>
        </template>
      </Table>
      <p v-if="!loading" style="margin-top: 10px;">
        <Page v-model="pagination.current" :total="pagination.total" :page-size="pagination.pageSize" show-sizer show-elevator show-total
              @on-page-size-change="handlerSizeChange" @on-change="handlerIndexChange"/>
      </p>
    </Card>
    <DetailsFunction v-if="visibleInfo" :isVisible="visibleInfo" :id="applyId" @close="handlerCloseCreateNew($event)"/>
    <ImportFunction v-if="visibleImport" :isVisible="visibleImport" @close="handlerImportController($event)"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useI18n} from 'vue-i18n';
import {ResponsePage} from "@/model/ResponsePage";
import {Filter} from "@/model/Filter";
import {Order} from "@/model/Order";
import {createHeaders} from "@/views/system/function/GenerateFunction";
import FunctionsService from "@/services/settings/functions/FunctionsService";
import DetailsFunction from "@/views/system/function/DetailsFunction.vue";
import ImportFunction from "@/views/system/function/ImportFunction.vue";

const filter: Filter = new Filter();
export default defineComponent({
  components: {ImportFunction, DetailsFunction},
  setup()
  {
    const i18n = useI18n();
    const headers = createHeaders(i18n);
    return {
      headers,
      filter
    }
  },
  data()
  {
    return {
      data: ResponsePage,
      loading: false,
      applyId: 0,
      visibleInfo: false,
      visibleImport: false,
      pagination: {
        total: 0,
        current: 1,
        pageSize: 10
      }
    }
  },
  created()
  {
    this.handlerInitialize(this.filter)
  },
  methods: {
    alert()
    {
      return alert
    },
    handlerInitialize(filter: Filter)
    {
      this.loading = true;
      FunctionsService.getAllByFilter(filter)
        .then((response) => {
          if (response.status) {
            this.data = response.data;
            this.pagination.total = response.data.total;
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handlerSizeChange(size: number)
    {
      this.pagination.pageSize = size;
      this.handlerTableChange(this.pagination);
    },
    handlerIndexChange(index: number)
    {
      this.pagination.current = index;
      this.handlerTableChange(this.pagination);
    },
    handlerTableChange(pagination: any)
    {
      this.pagination.current = pagination.current;
      this.pagination.pageSize = pagination.pageSize;
      this.filter.page = pagination.current;
      this.filter.size = pagination.pageSize;
      this.handlerInitialize(this.filter);
    },
    handlerSort(column: { key: string, order: string })
    {
      const order: Order = {
        column: column.key,
        order: column.order
      }
      if (!this.filter.orders) {
        this.filter.orders = new Array<Order>();
      }
      // distinct
      this.filter.orders = this.filter.orders.filter(value => value.column !== column.key);
      this.filter.orders.push(order);
      this.handlerInitialize(this.filter);
    },
    handlerCreateOrUpdate(value?: number)
    {
      if (value) {
        this.applyId = value;
      }
      this.visibleInfo = true;
    },
    handlerCloseCreateNew(value: boolean)
    {
      this.visibleInfo = value;
      this.applyId = 0;
      this.handlerInitialize(this.filter);
    },
    handlerPluginConvert(elements: [])
    {
      const plugins = [];
      elements.forEach(element => plugins.push({src: '/static/images/plugin/' + new String(element).split(' ')[0] + '.png', tip: element}));
      return plugins;
    },
    handlerImportController(value: boolean)
    {
      this.visibleImport = value;
      this.handlerInitialize(this.filter);
    },
  }
});
</script>
<style scoped>
.poptip-box .ivu-poptip-body-content {
  overflow: visible;
}
</style>
