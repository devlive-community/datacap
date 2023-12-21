<template>
  <div>
    <Card :title="$t('common.dataset')"
          dis-hover>
      <Table :loading="loading"
             :columns="headers"
             :data="data?.content">
        <template #source="{ row }">
          <Tooltip transfer
                   :content="row.source.type">
            <Avatar :src="'/static/images/plugin/' + row.source.type + '.png'"
                    size="small">
            </Avatar>
          </Tooltip>
        </template>
        <template #state="{ row }">
          <Poptip trigger="hover"
                  placement="bottom"
                  transfer>
            <Text>{{ getState(row.state) }}</Text>
            <template #content>
              <DatasetState style="margin-top: 25px;"
                            :states="row.state">
              </DatasetState>
            </template>
          </Poptip>
        </template>
        <template #action="{ row }">
          <Space>
            <Button shape="circle"
                    size="small"
                    :disabled="isSuccess(row.state)"
                    @click="handlerRebuild(row, true)">
              <FontAwesomeIcon :icon="row.state === 'SUCCESS' ? 'circle-stop' : 'circle-play'"/>
            </Button>
            <Button type="primary"
                    shape="circle"
                    size="small"
                    @click="$router.push('/admin/dataset/create?id=' + row.id)">
              <FontAwesomeIcon icon="pen-square"/>
            </Button>
            <Button type="error"
                    shape="circle"
                    size="small"
                    :disabled="isSuccess(row.state)"
                    @click="handlerError(row, true)">
              <FontAwesomeIcon icon="triangle-exclamation"/>
            </Button>
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
    <DatasetRebuild v-if="rebuildVisible"
                    :is-visible="rebuildVisible"
                    :data="contextData"
                    @close="handlerRebuild(null, false)">
    </DatasetRebuild>
    <MarkdownPreview v-if="errorVisible"
                     :isVisible="errorVisible"
                     :content="'```java\n' + contextData.message + '\n```'"
                     @close="handlerError(null, false)">
    </MarkdownPreview>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import {useI18n} from 'vue-i18n';
import {ResponsePage} from "@/model/ResponsePage";
import {createHeaders} from "@/views/admin/dataset/DatasetUtils";
import DatasetService from "@/services/admin/DatasetService";
import {Filter} from "@/model/Filter";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import DatasetRebuild from "@/views/admin/dataset/DatasetRebuild.vue";
import DatasetState from "@/views/admin/dataset/components/DatasetState.vue";
import MarkdownPreview from "@/components/common/MarkdownPreview.vue";

const filter: Filter = new Filter();
export default defineComponent({
  name: 'AdminDataset',
  components: {MarkdownPreview, DatasetState, DatasetRebuild, FontAwesomeIcon},
  setup()
  {
    const i18n = useI18n()
    const headers = createHeaders(i18n)
    return {
      i18n,
      headers,
      filter
    }
  },
  data()
  {
    return {
      loading: false,
      data: null as ResponsePage,
      pagination: {total: 0, current: 1, pageSize: 10},
      rebuildVisible: false,
      contextData: null,
      errorVisible: false
    }
  },
  created()
  {
    this.handlerInitialize(this.filter)
  },
  methods: {
    handlerInitialize(filter: Filter)
    {
      this.loading = true
      DatasetService.getAll(filter)
        .then((response) => {
          if (response.status) {
            this.data = response.data
            this.pagination.total = response.data.total
          }
        })
        .finally(() => this.loading = false)
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
    handlerRebuild(record: any, opened: boolean)
    {
      this.rebuildVisible = opened
      this.contextData = record
    },
    handlerError(record: any, opened: boolean)
    {
      this.errorVisible = opened
      this.contextData = record
    },
    getState(state: Array<any> | null)
    {
      if (state && state.length > 0) {
        const s = state[state.length - 1]
        return s
      }
      return null
    },
    isSuccess(state: Array<any> | null)
    {
      if (state && state.length > 0) {
        const s = state[state.length - 1]
        return s.endsWith('SUCCESS')
      }
      return false
    }
  }
});
</script>

<style scoped>
</style>
