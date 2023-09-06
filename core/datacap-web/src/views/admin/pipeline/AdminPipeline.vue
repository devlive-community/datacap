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
        <template #executor="{row}">
          <Tooltip transfer
                   :content="row.executor">
            <Avatar :src="`/static/images/executor/${row.executor}.png`"
                    size="small">
            </Avatar>
          </Tooltip>
        </template>
        <template #from="{row}">
          <Tooltip transfer
                   :content="row.from.name">
            <Avatar :src="'/static/images/plugin/' + row.from.type + '.png'"
                    size="small">
            </Avatar>
          </Tooltip>
        </template>
        <template #to="{row}">
          <Tooltip transfer
                   :content="row.to.name">
            <Avatar :src="'/static/images/plugin/' + row.to.type + '.png'"
                    size="small">
            </Avatar>
          </Tooltip>
        </template>
        <template #state="{row}">
          <Tag :color="getColor(row.state)">
            {{ getStateText(row.state) }}
          </Tag>
        </template>
        <template #action="{ row }">
          <Space>
            <Tooltip :content="$t('common.error')"
                     transfer>
              <Button shape="circle"
                      :disabled="row.state !== 'FAILURE' && !(row.state == 'STOPPED' && row.message)"
                      type="error"
                      size="small"
                      icon="md-bug"
                      @click="handlerVisibleMarkdownPreview(row.message, true)">
              </Button>
            </Tooltip>
            <Tooltip :content="$t('common.log')"
                     transfer>
              <Button shape="circle"
                      :disabled="row.state === 'QUEUE' || row.state === 'CREATED'"
                      type="primary"
                      size="small"
                      icon="md-bulb"
                      @click="handlerLogger(row, true)">
              </Button>
            </Tooltip>
            <Tooltip :content="$t('common.stop')"
                     transfer>
              <Button shape="circle"
                      :disabled="row.state === 'STOPPED' || row.state === 'SUCCESS' || row.state === 'FAILURE' || row.state === 'TIMEOUT'"
                      type="warning"
                      size="small"
                      icon="md-square"
                      @click="handlerStop(row, true)">
              </Button>
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
      <p v-if="!loading"
         style="margin-top: 10px;">
        <Page v-model="pagination.current"
              :total="pagination.total"
              :page-size="pagination.size"
              show-sizer
              show-elevator
              show-total
              @on-page-size-change="handlerSizeChange"
              @on-change="handlerIndexChange">
        </Page>
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
                     :is-visible="detail"
                     @close="handlerDetail(false)">
    </DetailsPipeline>

    <StopPipeline v-if="stopped"
                  :is-visible="stopped"
                  :info="info"
                  @close="handlerStop(null, false)">
    </StopPipeline>

    <LoggerPipeline v-if="logger"
                    :is-visible="logger"
                    :info="info"
                    @close="handlerLogger(null, false)">
    </LoggerPipeline>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {useI18n} from 'vue-i18n';
import {Filter} from "@/model/Filter";
import {ResponsePage} from "@/model/ResponsePage";
import {Pagination, PaginationBuilder} from "@/model/Pagination";
import {createHeaders, getColor, getText} from "@/views/admin/pipeline/PipelineGenerate";
import PipelineService from "@/services/user/PipelineService";
import MarkdownPreview from "@/components/common/MarkdownPreview.vue";
import DeletePipeline from "@/views/admin/pipeline/DeletePipeline.vue";
import DetailsPipeline from "@/views/admin/pipeline/DetailPipeline.vue";
import StopPipeline from "@/views/admin/pipeline/StopPipeline.vue";
import LoggerPipeline from "@/views/admin/pipeline/components/LoggerPipeline.vue";

const filter: Filter = new Filter();
const pagination: Pagination = PaginationBuilder.newInstance();

export default defineComponent({
  name: 'UserPipelineHome',
  components: {LoggerPipeline, StopPipeline, DetailsPipeline, DeletePipeline, MarkdownPreview},
  setup()
  {
    const i18n = useI18n();
    const headers = createHeaders(i18n);
    return {
      headers,
      filter,
      pagination,
      i18n
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
      detail: false,
      stopped: false,
      logger: false
    }
  },
  created()
  {
    this.handlerInitialize(this.filter);
  },
  methods: {
    getColor,
    getText,
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
      if (!isOpen) {
        this.handlerInitialize(this.filter);
      }
    },
    handlerStop(row: any, isOpen: boolean)
    {
      this.stopped = isOpen
      this.info = row
      if (!isOpen) {
        this.handlerInitialize(this.filter);
      }
    },
    handlerLogger(row: any, isOpen: boolean)
    {
      this.logger = isOpen
      this.info = row
    },
    getStateText(origin: string): string
    {
      return getText(this.i18n, origin);
    }
  }
})
</script>
