<template>
  <div>
    <Modal v-model="visible"
           :title="`[ ${name} ] ${$t('common.history')}`"
           width="80%"
           :closable="false"
           :maskClosable="false">
      <Table :loading="loading"
             :columns="headers"
             :data="finalData?.content">
        <template #elapsed="{row}">
          {{ (new Date(row.updateTime) - new Date(row.createTime)) / 1000 }}
        </template>
        <template #state="{row}">
          <Tag :color="getColor(row.state)">
            {{ getStateText(row.state) }}
          </Tag>
        </template>
        <template #info="{row}">
          <Poptip trigger="hover"
                  padding="2px 10px"
                  transfer>
            <Button type="primary"
                    size="small"
                    icon="md-eye">
            </Button>
            <template #content>
              <MarkdownView :content="toMarkdown(row.info)"/>
            </template>
          </Poptip>
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
      <template #footer>
        <Button @click="handlerCancel()">
          {{ $t('common.cancel') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {ResponsePage} from "@/model/ResponsePage";
import {useI18n} from "vue-i18n";
import {createHistoryHeaders} from "@/views/system/schedule/GenertateSchedule";
import {Filter} from "@/model/Filter";
import {Pagination, PaginationBuilder} from "@/model/Pagination";
import ScheduleService from '@/services/admin/ScheduleService';
import {getColor, getText} from "@/views/admin/pipeline/PipelineGenerate";
import MarkdownView from "@/components/markdown/MarkdownView.vue";

const filter: Filter = new Filter();
const pagination: Pagination = PaginationBuilder.newInstance();
export default defineComponent({
  name: 'ScheduleHistory',
  components: {MarkdownView},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    name: {
      type: String
    },
    id: {
      type: Number
    }
  },
  setup()
  {
    const i18n = useI18n();
    const headers = createHistoryHeaders(i18n);
    return {
      headers,
      filter,
      pagination,
      i18n
    }
  },
  created()
  {
    this.handlerInitialize(this.filter);
  },
  data()
  {
    return {
      loading: false,
      finalData: null as ResponsePage
    }
  },
  methods: {
    getColor,
    handlerInitialize(filter: Filter)
    {
      this.loading = true
      ScheduleService.getScheduleHistory(filter, this.id)
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
    handlerCancel()
    {
      this.visible = false;
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
    getStateText(origin: string): string
    {
      return getText(this.i18n, origin);
    },
    toMarkdown(content: string)
    {
      return '```json\n' + JSON.stringify(content, null, 4) + '\n```';
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
});
</script>

