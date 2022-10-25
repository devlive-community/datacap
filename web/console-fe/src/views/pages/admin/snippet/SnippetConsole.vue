<template>
  <div>
    <a-card :title="$t('common.snippet')" size="small">
      <a-table size="middle" :loading="loading" :columns="headers" :data-source="columns" :pagination="pagination"
               @change="handlerTableChange($event)">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'username'">
            <a-avatar style="background-color: #87d068">{{ record.user.username }}</a-avatar>
          </template>
          <template v-if="column.dataIndex === 'action'">
            <a-space style="width: 100%">
              <a-popconfirm title="Are you sure delete?" ok-text="Yes" cancel-text="No" :disabled="currentUserId !== record.user.id"
                            @confirm="handlerDeleteRecord(record.id)">
                <a-tooltip>
                  <template #title>{{ $t('common.delete') }}</template>
                  <a-button type="primary" danger shape="circle" size="small" :disabled="currentUserId !== record.user.id">
                    <template #icon>
                      <minus-outlined/>
                    </template>
                  </a-button>
                </a-tooltip>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script lang="ts">
import {message} from "ant-design-vue";
import {defineComponent} from "vue";
import {useI18n} from 'vue-i18n';
import Common from "@/common/Common";
import {createHeaders} from "@/views/pages/admin/snippet/SnippetGenerate";
import {SnippetService} from "@/services/SnippetService";

export default defineComponent({
  name: "SnippetConsoleView",
  setup()
  {
    const i18n = useI18n();
    const headers = createHeaders(i18n);
    const currentUserId = Common.getCurrentUserId();
    return {
      headers,
      currentUserId
    }
  },
  data()
  {
    return {
      columns: [],
      loading: false,
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
  created()
  {
    this.handlerInitialize(this.pagination.current, this.pagination.pageSize)
  },
  methods: {
    handlerInitialize(page: number, size: number)
    {
      this.loading = true;
      new SnippetService()
        .getSnippets(page, size)
        .then((response) => {
          if (response.status) {
            this.columns = response.data.content;
            this.pagination.total = response.data.total;
          }
          this.loading = false;
        })
    },
    handlerDeleteRecord(id: number)
    {
      new SnippetService()
        .delete(id)
        .then((response) => {
          if (response.status) {
            message.success("Delete successful");
            this.handlerInitialize(this.pagination.current, this.pagination.pageSize);
          }
        });
    },
    handlerTableChange(pagination: any)
    {
      this.pagination.current = pagination.current;
      this.pagination.pageSize = pagination.pageSize;
      this.handlerInitialize(pagination.current, pagination.pageSize)
    }
  }
});
</script>
