<template>
  <div>
    <a-card title="Source List" size="small">
      <template #extra>
        <a-tooltip>
          <template #title>Create new source</template>
          <a-button type="primary" shape="circle" size="small" @click="handlerCreateOrUpdate()">
            <template #icon>
              <plus-circle-outlined />
            </template>
          </a-button>
        </a-tooltip>
      </template>
      <a-table size="middle" :loading="loading" :columns="headers" :data-source="columns" :pagination="pagination"
        @change="handlerTableChange($event)">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'action'">
            <a-space style="width: 100%">
              <a-popconfirm title="Are you sure delete?" ok-text="Yes" cancel-text="No"
                @confirm="handlerDeleteRecord(record.id)">
                <a-tooltip>
                  <template #title>Delete</template>
                  <a-button type="primary" danger shape="circle" size="small">
                    <template #icon>
                      <minus-outlined />
                    </template>
                  </a-button>
                </a-tooltip>
              </a-popconfirm>
              <a-tooltip>
                <template #title>Mofidy</template>
                <a-button type="primary" shape="circle" size="small" @click="handlerCreateOrUpdate(record.id)">
                  <template #icon>
                    <edit-outlined />
                  </template>
                </a-button>
              </a-tooltip>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <SourceInfoView v-if="visibleSourceInfo" :isVisible="visibleSourceInfo" :id="applyId"
      @close="handlerCloseCreateNew($event)" />
  </div>
</template>

<script lang="ts">
import { SourceService } from "@/services/SourceService";
import { headers } from "@/views/pages/admin/source/SourceGenerate";
import SourceInfoView from "@/views/pages/admin/source/SourceInfo.vue";
import { MinusOutlined, PlusCircleOutlined } from '@ant-design/icons-vue';
import { message } from "ant-design-vue";
import { defineComponent } from "vue";

export default defineComponent({
  name: "SourceConsoleView",
  components: { SourceInfoView, PlusCircleOutlined, MinusOutlined },
  data() {
    return {
      headers,
      columns: [],
      loading: false,
      visibleSourceInfo: false,
      applyId: 0,
      pagination: {
        total: 0,
        current: 1,
        pageSize: 10,
        showSizeChanger: true,
        pageSizeOptions: ["10", "20", "50", "100"],
        showTotal: (total: number) => `Total <${total}> Elements`
      }
    }
  },
  created() {
    this.handlerInitialize(this.pagination.current, this.pagination.pageSize)
  },
  methods: {
    handlerInitialize(page: number, size: number) {
      this.loading = true;
      new SourceService()
        .getSources(page, size)
        .then((response) => {
          if (response.status) {
            this.columns = response.data.content;
            this.pagination.total = response.data.total;
          }
          this.loading = false;
        })
    },
    handlerCreateOrUpdate(value?: number) {
      if (value) {
        this.applyId = value;
      }
      this.visibleSourceInfo = true;
    },
    handlerCloseCreateNew(value: boolean) {
      this.visibleSourceInfo = value;
      this.applyId = 0;
      this.handlerInitialize(this.pagination.current, this.pagination.pageSize);
    },
    handlerDeleteRecord(id: number) {
      new SourceService()
        .delete(id)
        .then((response) => {
          if (response.status) {
            message.success("Delete successful");
            this.handlerInitialize(this.pagination.current, this.pagination.pageSize);
          }
        });
    },
    handlerTableChange(pagination: any) {
      this.pagination.current = pagination.current;
      this.pagination.pageSize = pagination.pageSize;
      this.handlerInitialize(pagination.current, pagination.pageSize)
    }
  }
});
</script>
