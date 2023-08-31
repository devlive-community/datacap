<template>
  <div>
    <Card style="width:100%"
          :title="$t('common.pipeline')"
          dis-hover>
      <template #extra>
        <Button type="primary"
                size="small"
                icon="md-add"
                @click="handlerDetail(true)">
          {{ $t('common.create') }}
        </Button>
      </template>
      <Table :loading="loading"
             :columns="headers"
             :data="finalData?.content">
        <template #from="{row}">
          <Tooltip transfer :content="row.from.name">
            <Avatar :src="'/static/images/plugin/' + row.from.type + '.png'" size="small"/>
          </Tooltip>
        </template>
        <template #to="{row}">
          <Tooltip transfer :content="row.to.name">
            <Avatar :src="'/static/images/plugin/' + row.to.type + '.png'" size="small"/>
          </Tooltip>
        </template>
        <template #action="{ row }">
          <Space>
            <Tooltip :content="$t('common.error')"
                     transfer>
              <Button shape="circle" :disabled="row.state !== 'FAILURE'" type="error" size="small" icon="md-bug"
                      @click="handlerVisibleMarkdownPreview(row.message, true)"/>
            </Tooltip>
            <Tooltip :content="$t('common.delete')"
                     transfer>
              <Button :disabled="row.state == 'RUNNING'"
                      icon="md-trash"
                      shape="circle"
                      size="small"
                      type="error"
                      @click="handlerDelete(row, true)"/>
            </Tooltip>
          </Space>
        </template>
      </Table>
      <p v-if="!loading" style="margin-top: 10px;">
        <Page v-model="pagination.current" :total="pagination.total" :page-size="pagination.size" show-sizer show-elevator show-total
              @on-page-size-change="handlerSizeChange" @on-change="handlerIndexChange"/>
      </p>
    </Card>
    <MarkdownPreview v-if="visibleWarning"
                     :isVisible="visibleWarning"
                     :content="finalContent"
                     @close="handlerVisibleMarkdownPreview(null, $event)">
    </MarkdownPreview>

    <DeletePipeline v-if="deleted"
                    :is-visible="deleted"
                    :info="info"
                    @close="handlerDelete(null, false)">
    </DeletePipeline>

    <DetailsPipeline v-if="detail"
                     is-visible="detail"
                     @close="handlerDetail(false)">
    </DetailsPipeline>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {useI18n} from 'vue-i18n';
import {Filter} from "@/model/Filter";
import {ResponsePage} from "@/model/ResponsePage";
import {Pagination, PaginationBuilder} from "@/model/Pagination";
import {createHeaders} from "@/views/admin/pipeline/PipelineGenerate";
import PipelineService from "@/services/user/PipelineService";
import MarkdownPreview from "@/components/common/MarkdownPreview.vue";
import DeletePipeline from "@/views/admin/pipeline/DeletePipeline.vue";
import DetailsPipeline from "@/views/admin/pipeline/DetailPipeline.vue";

const filter: Filter = new Filter();
const pagination: Pagination = PaginationBuilder.newInstance();
export default defineComponent({
  name: 'UserPipelineHome',
  components: {DetailsPipeline, DeletePipeline, MarkdownPreview},
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
      visibleWarning: false,
      finalContent: null,
      finalData: null as ResponsePage,
      deleted: false,
      info: null,
      // Pipeline detail
      detail: false
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
      PipelineService.getAll(filter)
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
    handlerVisibleMarkdownPreview(content: string, isOpen: boolean)
    {
      this.visibleWarning = isOpen;
      if (content) {
        this.finalContent = '```java\n' + content + '\n```';
      }
    },
    handlerDelete(row: any, isOpen: boolean)
    {
      this.deleted = isOpen;
      this.info = row
      if (!isOpen) {
        this.handlerInitialize(this.filter);
      }
    },
    handlerDetail(isOpen: boolean)
    {
      this.detail = isOpen
    }
  }
})
</script>
