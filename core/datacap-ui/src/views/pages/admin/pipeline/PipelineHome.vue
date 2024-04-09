<template>
  <div class="w-full">
    <Card>
      <template #title>{{ $t('pipeline.common.list') }}</template>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #executor="{row}">
          <Tooltip :content="row.executor">
            <Avatar :src="`/static/images/executor/${row.executor}.png`"/>
          </Tooltip>
        </template>
        <template #from="{row}">
          <Tooltip :content="row.from.name">
            <Avatar :src="'/static/images/plugin/' + row.from.type + '.png'"/>
          </Tooltip>
        </template>
        <template #to="{row}">
          <Tooltip :content="row.to.name">
            <Avatar :src="'/static/images/plugin/' + row.to.type + '.png'"/>
          </Tooltip>
        </template>
        <template #state="{row}">
          <Tag :color="Common.getColor(row.state)">
            {{ Common.getText(i18n, row.state) }}
          </Tag>
        </template>
      </TableCommon>
    </Card>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Tooltip from '@/views/ui/tooltip'
import Avatar from '@/views/ui/avatar'
import Tag from '@/views/ui/tag'
import Button from '@/views/ui/button'
import Card from '@/views/ui/card'
import { FilterModel } from '@/model/filter'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { createHeaders } from '@/views/pages/admin/pipeline/PipelineUtils'
import { useI18n } from 'vue-i18n'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import PipelineService from '@/services/pipeline'
import Common from '@/utils/common.ts'

export default defineComponent({
  name: 'PipelineHome',
  components: {
    Button,
    TableCommon,
    Card,
    Tooltip,
    Avatar,
    Tag
  },
  setup()
  {
    const i18n = useI18n()
    const filter: FilterModel = new FilterModel()
    const headers = createHeaders(i18n)
    return {
      i18n,
      filter,
      headers
    }
  },
  computed: {
    Common()
    {
      return Common
    }
  },
  data()
  {
    return {
      loading: false,
      data: [],
      pagination: {} as PaginationModel
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      PipelineService.getAll(this.filter)
                     .then((response) => {
                       if (response.status) {
                         this.data = response.data.content
                         this.pagination = PaginationRequest.of(response.data)
                       }
                     })
                     .finally(() => this.loading = false)
    },
    handlerChangePage(value: PaginationModel)
    {
      this.filter.page = value.currentPage
      this.filter.size = value.pageSize
      this.handlerInitialize()
    }
  }
})
</script>
