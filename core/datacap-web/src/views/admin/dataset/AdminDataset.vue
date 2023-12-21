<template>
  <div>
    <Card :title="$t('common.dataset')"
          dis-hover>
      <Table :loading="loading"
             :columns="headers"
             :data="data?.content">
      </Table>
      <p v-if="!loading"
         style="margin-top: 10px;">
        <Page v-model="pagination.current"
              show-sizer
              show-elevator
              show-total
              :total="pagination.total"
              :page-size="pagination.pageSize"
              @on-page-size-change="handlerSizeChange"
              @on-change="handlerIndexChange">
        </Page>
      </p>
    </Card>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import {useI18n} from 'vue-i18n';
import {ResponsePage} from "@/model/ResponsePage";
import {createHeaders} from "@/views/admin/dataset/DatasetUtils";
import DatasetService from "@/services/admin/DatasetService";
import {Filter} from "@/model/Filter";

const filter: Filter = new Filter();
export default defineComponent({
  name: 'AdminDataset',
  setup()
  {
    const i18n = useI18n()
    const headers = createHeaders(i18n)
    return {
      i18n,
      headers,
      filter
    }
  },
  data()
  {
    return {
      loading: false,
      data: null as ResponsePage,
      pagination: {total: 0, current: 1, pageSize: 10}
    }
  },
  created()
  {
    this.handlerInitialize(this.filter)
  },
  methods: {
    handlerInitialize(filter: Filter)
    {
      this.loading = true
      DatasetService.getAll(filter)
        .then((response) => {
          if (response.status) {
            this.data = response.data
            this.pagination.total = response.data.total
          }
        })
        .finally(() => this.loading = false)
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
      this.handlerInitialize(this.filter)
    }
  }
});
</script>

<style scoped>
</style>
