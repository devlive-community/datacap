<template>
  <div>
    <Card style="width:100%"
          :title="$t('common.report')"
          dis-hover>
      <Table :loading="loading"
             :columns="headers"
             :data="data.content">
        <template #action="{ row }">
          <Space>
            <Tooltip :content="$t('common.delete')"
                     transfer>
              <Button shape="circle"
                      type="error"
                      size="small"
                      icon="md-trash"
                      @click="handlerDelete(row, true)">
              </Button>
            </Tooltip>
          </Space>
        </template>
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
    <DeleteReport v-if="deleteVisible"
                  :is-visible="deleteVisible"
                  :data="contextData"
                  @click="handlerDelete(null, false)">
    </DeleteReport>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useI18n} from 'vue-i18n';
import {ResponsePage} from "@/model/ResponsePage";
import {createHeaders} from "@/views/admin/report/ReportUtils";
import ReportService from "@/services/admin/ReportService";
import {Filter} from "@/model/Filter";
import {Pagination, PaginationBuilder} from "@/model/Pagination";
import DeleteReport from "@/views/admin/report/DeleteReport.vue";

const filter: Filter = new Filter();
const pagination: Pagination = PaginationBuilder.newInstance();
export default defineComponent({
  name: "ReportAdmin",
  components: {DeleteReport},
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
      pagination: {
        total: 0,
        current: 1,
        pageSize: 10
      },
      deleteVisible: false,
      contextData: null
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
      ReportService.getAll(filter)
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
      this.handlerInitialize(this.filter)
    },
    handlerDelete(data: any, opened: boolean)
    {
      this.deleteVisible = opened;
      this.contextData = data;
      if (!opened) {
        this.handlerInitialize(this.filter);
      }
    }
  }
});
</script>
