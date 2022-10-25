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
              <a-button type="primary" shape="circle" size="small"
                        @click="handlerShowContent(record.code)">
                <a-tooltip>
                  <template #title>SQL</template>
                  <eye-outlined/>
                </a-tooltip>
              </a-button>
              <a-tooltip>
                <template #title>{{ $t('common.modify') }}</template>
                <a-button type="primary" shape="circle" size="small" :disabled="currentUserId !== record.user.id" @click="handlerCreateOrUpdate(record.id)">
                  <template #icon>
                    <edit-outlined/>
                  </template>
                </a-button>
              </a-tooltip>
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

    <ConsoleSQLComponent v-if="visibleContent" :isVisible="visibleContent" :content="content"
                         @close="handlerCloseContent($event)"/>

    <SnippetDetails v-if="visibleSnippetInfo" :isVisible="visibleSnippetInfo" :id="applyId"
                    @close="handlerCloseCreateNew()"/>
  </div>
</template>

<script lang="ts">
import ConsoleSQLComponent from "@/components/ConsoleSQL.vue";
import SnippetDetails from "@/views/pages/admin/snippet/SnippetDetails.vue";
import {message} from "ant-design-vue";
import {defineComponent} from "vue";
import {useI18n} from 'vue-i18n';
import Common from "@/common/Common";
import {createHeaders} from "@/views/pages/admin/snippet/SnippetGenerate";
import {SnippetService} from "@/services/SnippetService";

export default defineComponent({
  name: "SnippetConsoleView",
  components: {ConsoleSQLComponent, SnippetDetails},
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
      visibleSnippetInfo: false,
      content: '',
      visibleContent: false,
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
    },
    handlerShowContent(content: string)
    {
      this.visibleContent = true;
      this.content = content;
    },
    handlerCloseContent(value: boolean)
    {
      this.visibleContent = value;
      this.content = '';
    },
    handlerCreateOrUpdate(value?: number)
    {
      if (value) {
        this.applyId = value;
      }
      this.visibleSnippetInfo = true;
    },
    handlerCloseCreateNew()
    {
      this.visibleSnippetInfo = false;
      this.applyId = 0;
      this.handlerInitialize(this.pagination.current, this.pagination.pageSize);
    }
  }
});
</script>
