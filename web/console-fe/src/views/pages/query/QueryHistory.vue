<template>
  <div>
    <Card style="width:100%" :title="$t('common.history')">
      <template #extra>
        <Tooltip>
          <template #content>{{ $t('common.create') }}</template>
          <Button type="primary" shape="circle" icon="md-add" size="small" @click="handlerCreateOrUpdate()"/>
        </Tooltip>
      </template>
      <Table :loading="loading" :columns="headers" :data="data.content">
        <template #plugin="{ row }">
          <Ellipsis :text="row.plugin.name" :height="25" tooltip/>
        </template>
        <template #elapsed="{ row }">
          <Tooltip :content="$t('tooltip.elapsedMillisecond')" transfer>
            {{ row.elapsed }}
          </Tooltip>
        </template>
        <template #state="{ row }">
          <Tag :color="row.state === 'SUCCESS' ? 'success' : 'error'">{{ row.state }}</Tag>
        </template>
        <template #action="{ row }">
          <Space>
            <Tooltip content="SQL" transfer>
              <Button shape="circle" type="info" size="small" icon="md-eye" @click="handlerShowContent(row.content)"/>
            </Tooltip>
            <Tooltip :content="$t('common.error')" transfer>
              <Button :disabled="row.state === 'SUCCESS'" shape="circle" type="error" size="small" icon="md-pin" @click="handlerShowError(row.message)"/>
            </Tooltip>
          </Space>
        </template>
      </Table>
      <p v-if="!loading" style="margin-top: 10px;">
        <Page v-model="pagination.current" :total="pagination.total" :page-size="pagination.pageSize" show-sizer show-elevator show-total
              @on-page-size-change="handlerSizeChange" @on-change="handlerIndexChange"/>
      </p>
    </Card>
    <SqlDetail v-if="visibleContent" :isVisible="visibleContent" :content="content"
               @close="handlerCloseContent($event)"/>
  </div>
</template>

<script lang="ts">
import {AuditService} from "@/services/AuditService";
import {createHeaders} from "@/views/pages/query/QueryGenerate";
import {useI18n} from 'vue-i18n';
import {defineComponent} from "vue";
import {ResponsePage} from "@/model/ResponsePage";
import SqlDetail from "@/components/sql/SqlDetail.vue";

export default defineComponent({
  name: "QueryHistory",
  components: {SqlDetail},
  setup()
  {
    const i18n = useI18n();
    const headers = createHeaders(i18n);
    return {
      headers
    }
  },
  data()
  {
    return {
      data: ResponsePage,
      loading: false,
      content: '',
      visibleContent: false,
      pagination: {
        total: 0,
        current: 1,
        pageSize: 10
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
      new AuditService()
        .getPluginAudits(page, size)
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
      this.handlerInitialize(pagination.current, pagination.pageSize)
    },
    handlerShowError(message: string)
    {
      this.handlerShowContent(message);
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
    }
  }
});
</script>
