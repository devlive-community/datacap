<template>
  <div>
    <Modal v-model="visible"
           width="60%"
           :title="`[ ${name} ] ${$t('common.syncHistory')}`"
           :closable="false"
           :maskClosable="false">
      <Table :loading="loading"
             :columns="headers"
             :data="finalData?.content">
        <template #elapsed="{row}">
          {{ row.elapsed }}
        </template>
        <template #state="{row}">
          <Tag :color="getColor(row.state)">
            <Poptip v-if="row.state === 'FAILURE'"
                    :content="row.message"
                    transfer
                    placement="bottom">
              {{ getStateText(row.state) }}
            </Poptip>
            <span v-else>
              {{ getStateText(row.state) }}
            </span>
          </Tag>
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
import {Filter} from "@/model/Filter";
import {Pagination, PaginationBuilder} from "@/model/Pagination";
import {useI18n} from "vue-i18n";
import {ResponsePage} from "@/model/ResponsePage";
import {getColor, getText} from "@/views/admin/pipeline/PipelineGenerate";
import DatasetService from "@/services/admin/DatasetService";
import {createHistoryHeaders} from "@/views/admin/dataset/DatasetUtils";

export default defineComponent({
  name: 'DatasetHistory',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    code: {
      type: Number
    },
    name: {
      type: String
    }
  },
  setup()
  {
    const filter: Filter = new Filter();
    const pagination: Pagination = PaginationBuilder.newInstance();
    const i18n = useI18n();
    const headers = createHistoryHeaders(i18n);
    return {
      headers,
      filter,
      pagination,
      i18n
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
  data()
  {
    return {
      loading: false,
      finalData: null as ResponsePage
    }
  },
  created()
  {
    this.handlerInitialize(this.filter)
  },
  methods: {
    getColor,
    handlerInitialize(filter: Filter)
    {
      this.loading = true
      DatasetService.getHistory(this.code, filter)
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
    }
  }
});
</script>
