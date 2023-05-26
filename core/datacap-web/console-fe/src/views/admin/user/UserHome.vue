<template>
  <div>
    <Card style="width:100%" :title="$t('common.authority')">
      <Table :loading="loading"
             :columns="headers"
             :data="finalData?.content">
        <template #action="{ row }">
          <Space>
            <Tooltip transfer
                     :content="$t('common.authority')">
              <Button shape="circle"
                      type="primary"
                      size="small"
                      icon="md-hammer"
                      :disabled="row.default"
                      @click="handlerAllocationRole(row.id)">
              </Button>
            </Tooltip>
          </Space>
        </template>
      </Table>
      <p v-if="!loading" style="margin-top: 10px;">
        <Page v-model="pagination.current"
              show-sizer
              show-elevator
              show-total
              :total="pagination.total"
              :page-size="pagination.size"
              @on-page-size-change="handlerSizeChange"
              @on-change="handlerIndexChange">
        </Page>
      </p>
      <RoleAllocationComponent v-if="allocationRole"
                               :is-visible="allocationRole"
                               :user-id="userId"
                               @close="handlerAllocationRole">
      </RoleAllocationComponent>
    </Card>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {useI18n} from 'vue-i18n';
import {Filter} from "@/model/Filter";
import {ResponsePage} from "@/model/ResponsePage";
import {Pagination, PaginationBuilder} from "@/model/Pagination";
import {createHeaders} from './UserGenerate';
import UserService from "@/services/admin/UserService";
import RoleAllocationComponent from "@/views/admin/role/components/RoleAllocation.vue";

const filter: Filter = new Filter();
const pagination: Pagination = PaginationBuilder.newInstance();
export default defineComponent({
  name: 'UserHome',
  components: {RoleAllocationComponent},
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
      allocationRole: false,
      userId: null,
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
      UserService.getAll(filter)
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
    },
    handlerAllocationRole(value: number)
    {
      this.userId = value
      this.allocationRole = !this.allocationRole
    }
  }
})
</script>
