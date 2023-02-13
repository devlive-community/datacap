<template>
  <div>
    <Card style="width:100%" :title="$t('common.sql') + $t('common.template')">
      <template #extra>
        <Tooltip>
          <template #content>{{ $t('common.create') }}</template>
          <Button type="primary" shape="circle" icon="md-add" size="small" @click="handlerCreateOrUpdate()"/>
        </Tooltip>
      </template>
      <Table :loading="loading" :columns="headers" :data="data.content" @on-sort-change="handlerSort">
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
    <TemplateSqlDetails v-if="visibleInfo" :isVisible="visibleInfo" :id="applyId" @close="handlerCloseCreateNew($event)"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useI18n} from 'vue-i18n';
import {ResponsePage} from "@/model/ResponsePage";
import {createHeaders} from "@/views/pages/admin/template/sql/SqlGenerate";
import TemplateSqlService from "@/services/template/TemplateSqlService";
import {Filter} from "@/model/Filter";
import {Order} from "@/model/Order";
import TemplateSqlDetails from "@/views/pages/admin/template/sql/SqlDetails.vue";

const filter: Filter = new Filter();
export default defineComponent({
  name: "TemplateSqlAdmin",
  components: {TemplateSqlDetails},
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
    handlerInitialize(filter: Filter)
    {
      this.loading = true;
      TemplateSqlService.getAllTemplateSql(filter)
        .then((response) => {
          if (response.status) {
            this.data = response.data;
            this.pagination.total = response.data.total;
          }
          this.loading = false;
        })
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
    }
  }
});
</script>
<style scoped>
.poptip-box .ivu-poptip-body-content {
  overflow: visible;
}
</style>
