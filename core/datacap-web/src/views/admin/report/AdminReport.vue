<template>
  <div>
    <Card style="width:100%"
          :title="$t('common.report')"
          dis-hover>
      <Table :loading="loading"
             :columns="headers"
             :data="data.content">
        <template #realtime="{ row }">
          <Switch v-model="row.realtime"
                  disabled>
          </Switch>
        </template>
        <template #source="{ row }">
          <Tooltip v-if="row.source"
                   transfer
                   :content="row.source.type">
            <Avatar :src="'/static/images/plugin/' + row.source.type + '.png'"
                    size="small">
            </Avatar>
          </Tooltip>
          <Tooltip v-else
                   transfer
                   :content="`${$t('common.dataset')} [ ${row.dataset.name} ]`">
            <Icon type="ios-analytics"
                  size="30">
            </Icon>
          </Tooltip>
        </template>
        <template #action="{ row }">
          <Space>
            <Button shape="circle"
                    type="primary"
                    size="small"
                    icon="md-eye"
                    @click="handlerViewChart(row, true)">
            </Button>
            <Button shape="circle"
                    size="small"
                    icon="md-create"
                    :disabled="!row.dataset"
                    @click="$router.push(`/admin/dataset/adhoc/${row.dataset.code}/${row.id}`)">
            </Button>
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
                  @close="handlerDelete(null, false)">
    </DeleteReport>
    <ViewReport v-if="viewChartVisible"
                :is-visible="viewChartVisible"
                :data="contextData"
                @close="handlerViewChart(null, false)">
    </ViewReport>
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
import ViewReport from "@/views/admin/report/ViewReport.vue";

const filter: Filter = new Filter();
const pagination: Pagination = PaginationBuilder.newInstance();
export default defineComponent({
  name: "ReportAdmin",
  components: {ViewReport, DeleteReport},
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
      contextData: null,
      viewChartVisible: false
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
    },
    handlerViewChart(data: any, opened: boolean)
    {
      this.viewChartVisible = opened;
      this.contextData = data;
    }
  }
});
</script>
