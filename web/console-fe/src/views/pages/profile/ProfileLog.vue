<template>
  <div>
    <Card style="width:100%; minHeight: 150px;">
      <template #title>
        {{ $t('setting.log') }}
      </template>
      <div>
        <Table :columns="headers" :data="data.content">
          <template #state="{ row }">
            <Tag :color="row.state === 'SUCCESS' ? 'success' : 'error'">{{ row.state }}</Tag>
          </template>
        </Table>
        <Page v-model="pagination.current" :total="pagination.total" :page-size="pagination.pageSize" show-sizer show-elevator show-total
              @on-page-size-change="handlerSizeChange" @on-change="handlerIndexChange"/>
        <Spin fix :show="loading"/>
      </div>
    </Card>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {useI18n} from "vue-i18n";
import {Filter} from "@/model/Filter";
import {ResponsePage} from "@/model/ResponsePage";
import {createHeaders} from './ProfileGenerate';
import UserService from "@/services/UserService";

const filter: Filter = new Filter();
export default defineComponent({
  setup()
  {
    const i18n = useI18n();
    const headers = createHeaders(i18n);
    return {
      headers,
      filter
    }
  },
  created()
  {
    this.handlerInitialize(this.filter)
  },
  data()
  {
    return {
      loading: false,
      data: ResponsePage,
      pagination: {
        total: 0,
        current: 1,
        pageSize: 10
      }
    }
  },
  methods: {
    handlerInitialize(filter: Filter)
    {
      this.loading = true;
      UserService.getLogs(this.filter)
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
    }
  }
});
</script>
<style scoped>
.content {
  background-color: #FFFFFF;
}
</style>
