<template>
  <div>
    <Card style="width:100%"
          dis-hover
          :title="$t('common.menu')">
      <template #extra>
        <Tooltip>
          <template #content>{{ $t('common.create') }}</template>
          <Button type="primary" shape="circle" icon="md-add" size="small" @click="handlerVisibleDetail(null, true)"/>
        </Tooltip>
      </template>
      <Table :loading="loading" :columns="headers" :data="finalData?.content">
        <template #active="{row}">
          <Switch v-model="row.active" disabled></Switch>
        </template>
        <template #action="{ row }">
          <Space>
            <Tooltip :content="$t('common.modify')" transfer>
              <Button shape="circle" :disabled="row.default" type="primary" size="small" icon="md-create" @click="handlerVisibleDetail(row.id, true)"/>
            </Tooltip>
          </Space>
        </template>
      </Table>
      <p v-if="!loading" style="margin-top: 10px;">
        <Page v-model="pagination.current" :total="pagination.total" :page-size="pagination.size" show-sizer show-elevator show-total
              @on-page-size-change="handlerSizeChange" @on-change="handlerIndexChange"/>
      </p>
    </Card>
    <MenuDetails v-if="visibleDetail" :isVisible="visibleDetail" :id="applyId" @close="handlerVisibleDetail(null, $event)"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {useI18n} from 'vue-i18n';
import {Filter} from "@/model/Filter";
import {ResponsePage} from "@/model/ResponsePage";
import {Pagination, PaginationBuilder} from "@/model/Pagination";
import {createHeaders} from './MenuGenerate';
import MenuService from '@/services/admin/MenuService';
import MenuDetails from "@/views/system/menu/MenuDetails.vue";

const filter: Filter = new Filter();
const pagination: Pagination = PaginationBuilder.newInstance();
export default defineComponent({
  name: 'MenuHome',
  components: {MenuDetails},
  setup()
  {
    const i18n = useI18n();
    const headers = createHeaders(i18n);
    return {
      headers,
      filter,
      pagination
    }
  },
  data()
  {
    return {
      loading: false,
      visibleDetail: false,
      applyId: null,
      finalData: null as ResponsePage
    }
  },
  created()
  {
    this.handlerInitialize(this.filter);
  },
  methods: {
    handlerInitialize(filter: Filter)
    {
      this.loading = true;
      MenuService.getAll(filter)
        .then((response) => {
          if (response.status) {
            this.finalData = response.data;
            this.pagination.total = response.data.total;
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    handlerVisibleDetail(value: number, isOpened: boolean)
    {
      if (isOpened) {
        this.applyId = value;
        this.visibleDetail = true;
      }
      else {
        this.applyId = null;
        this.visibleDetail = false;
        this.handlerInitialize(this.filter)
      }
    },
    handlerSizeChange(size: number)
    {
      this.pagination.size = size;
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
      this.pagination.size = pagination.size;
      this.filter.page = pagination.current;
      this.filter.size = pagination.size;
      this.handlerInitialize(this.filter);
    }
  }
})
</script>
