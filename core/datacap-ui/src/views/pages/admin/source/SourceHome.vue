<template>
  <div class="w-full">
    <Card>
      <template #title>{{ $t('source.common.list') }}</template>
      <template #extra>
        <Button size="icon" class="ml-auto gap-1 h-6 w-6" @click="handlerInfo(true, null)">
          <Plus :size="15"/>
        </Button>
      </template>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #type="{row}">
          <Tooltip :content="row.type">
            <Avatar :size="'sm'" :src="'/static/images/plugin/' + row.type + '.png'" :alt="row.type" class="cursor-pointer"/>
          </Tooltip>
        </template>
        <template #public="{row}">
          <Switch disabled :default-checked="row.public"/>
        </template>
        <template #version="{row}">
          <Tag v-if="row.version">{{ row.version }}</Tag>
          <Tag v-else>-</Tag>
        </template>
        <template #available="{ row }">
          <Tooltip v-if="!row.available" :content="row.message">
            <CircleX class="cursor-pointer" style="color: #cc0000"/>
          </Tooltip>
          <CirclePlay v-else class="cursor-pointer" style="color: hsl(var(--primary))"/>
        </template>
        <template #action="{ row }">
          <div class="space-x-2">
            <Tooltip :content="$t('source.common.modify').replace('$NAME', row.name)">
              <Button :disabled="loginUserId !== row.user.id" size="icon" class="rounded-full w-8 h-8" @click="handlerInfo(true, row)">
                <Pencil :size="15"/>
              </Button>
            </Tooltip>
          </div>
        </template>
      </TableCommon>
    </Card>
    <SourceInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerInfo(false, null)"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import { Button } from '@/components/ui/button'
import { CirclePlay, CircleX, Pencil, Plus } from 'lucide-vue-next'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { SourceModel } from '@/model/source'
import SourceService from '@/services/source'
import { createHeaders } from '@/views/pages/admin/source/SourceUtils'
import Avatar from '@/views/ui/avatar'
import Tooltip from '@/views/ui/tooltip'
import Tag from '@/views/ui/tag'
import { Switch } from '@/components/ui/switch'
import Common from '@/utils/common'
import SourceInfo from '@/views/pages/admin/source/SourceInfo.vue'

export default defineComponent({
  name: 'SourceHome',
  components: {
    SourceInfo,
    Tag,
    Tooltip,
    Switch,
    Avatar,
    TableCommon,
    Pencil, CircleX, CirclePlay,
    Button,
    Card,
    Plus
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const headers = createHeaders(useI18n())
    const loginUserId = Common.getCurrentUserId()

    return {
      filter,
      headers,
      loginUserId
    }
  },
  data()
  {
    return {
      loading: false,
      data: [],
      pagination: {} as PaginationModel,
      dataInfoVisible: false,
      dataInfo: null as SourceModel | null
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
      SourceService.getSources(this.filter.page, this.filter.size)
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
    },
    handlerInfo(opened: boolean, value: null | SourceModel)
    {
      this.dataInfoVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    }
  }
})
</script>
