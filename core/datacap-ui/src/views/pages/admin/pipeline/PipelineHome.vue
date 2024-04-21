<template>
  <div class="w-full">
    <Card title-class="p-3">
      <template #title>{{ $t('pipeline.common.list') }}</template>
      <template #extra>
        <Tooltip :content="$t('pipeline.common.create')">
          <Button size="icon" class="w-6 h-6 rounded-full" to="/admin/pipeline/info">
            <Plus :size="14"/>
          </Button>
        </Tooltip>
      </template>
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
        <template #action="{row}">
          <div class="space-x-2">
            <Tooltip :content="$t('common.error')">
              <Button :disabled="row.state !== 'FAILURE' && !(row.state == 'STOPPED' && row.message)" :color="'#ed4014'" size="icon" class="w-6 h-6 rounded-full"
                      @click="handlerShowMessage(true, row)">
                <TriangleAlert :size="14"/>
              </Button>
            </Tooltip>
            <DropdownMenu>
              <DropdownMenuTrigger as-child>
                <Button size="icon" class="rounded-full w-6 h-6" variant="outline">
                  <Cog class="w-full justify-center" :size="14"/>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent>
                <DropdownMenuGroup>
                  <DropdownMenuItem class="cursor-pointer" :disabled="row.state !== 'RUNNING'" @click="handlerStop(true, row)">
                    <CircleStop class="mr-2 h-4 w-4"/>
                    <span>{{ $t('pipeline.common.stop') }}</span>
                  </DropdownMenuItem>
                  <DropdownMenuItem class="cursor-pointer" @click="handlerLogger(true, row)">
                    <Rss class="mr-2 h-4 w-4"/>
                    <span>{{ $t('pipeline.common.logger') }}</span>
                  </DropdownMenuItem>
                  <DropdownMenuItem class="cursor-pointer" :disabled="row.state == 'RUNNING'" @click="handlerDelete(true, row)">
                    <Delete class="mr-2 h-4 w-4"/>
                    <span>{{ $t('pipeline.common.delete') }}</span>
                  </DropdownMenuItem>
                  <DropdownMenuItem class="cursor-pointer" @click="handlerFlow(true, row)">
                    <Flower class="mr-2 h-4 w-4"/>
                    <span>{{ $t('pipeline.common.flow') }}</span>
                  </DropdownMenuItem>
                </DropdownMenuGroup>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </template>
      </TableCommon>
    </Card>
  </div>
  <MarkdownPreview v-if="dataMessageVisible && dataInfo" :is-visible="dataMessageVisible" :content="dataInfo.message" @close="handlerShowMessage(false, null)"/>
  <PipelineLogger v-if="dataLoggerVisible && dataInfo" :is-visible="dataLoggerVisible" :info="dataInfo" @close="handlerLogger(false, null)"/>
  <PipelineDelete v-if="dataDeleteVisible && dataInfo" :is-visible="dataDeleteVisible" :info="dataInfo" @close="handlerDelete(false, null)"/>
  <PipelineStop v-if="dataStopVisible && dataInfo" :is-visible="dataStopVisible" :info="dataInfo" @close="handlerStop(false, null)"/>
  <PipelineFlow v-if="dataFlowVisible && dataInfo" :is-visible="dataFlowVisible" :info="dataInfo" @close="handlerFlow(false, null)"/>
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
import { CircleStop, Cog, Delete, Flower, Plus, Rss, TriangleAlert } from 'lucide-vue-next'
import { PipelineModel } from '@/model/pipeline.ts'
import MarkdownPreview from '@/views/components/markdown/MarkdownView.vue'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import PipelineLogger from '@/views/pages/admin/pipeline/PipelineLogger.vue'
import PipelineDelete from '@/views/pages/admin/pipeline/PipelineDelete.vue'
import PipelineStop from '@/views/pages/admin/pipeline/PipelineStop.vue'
import PipelineFlow from '@/views/pages/admin/pipeline/PipelineFlow.vue'

export default defineComponent({
  name: 'PipelineHome',
  components: {
    PipelineFlow,
    PipelineStop,
    PipelineDelete,
    PipelineLogger,
    MarkdownPreview,
    Button,
    TableCommon,
    Card,
    Tooltip,
    Avatar,
    Tag,
    TriangleAlert, Cog, Rss, Delete, CircleStop, Flower, Plus,
    DropdownMenuItem, DropdownMenuGroup, DropdownMenuSeparator, DropdownMenuLabel, DropdownMenuContent, DropdownMenuTrigger, DropdownMenu
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
      pagination: {} as PaginationModel,
      dataInfo: null as PipelineModel | null,
      dataMessageVisible: false,
      dataLoggerVisible: false,
      dataDeleteVisible: false,
      dataStopVisible: false,
      dataFlowVisible: false
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
    },
    handlerShowMessage(opened: boolean, value: null | PipelineModel)
    {
      this.dataMessageVisible = opened
      this.dataInfo = value
    },
    handlerLogger(opened: boolean, value: null | PipelineModel)
    {
      this.dataLoggerVisible = opened
      this.dataInfo = value
    },
    handlerDelete(opened: boolean, value: null | PipelineModel)
    {
      this.dataDeleteVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerStop(opened: boolean, value: null | PipelineModel)
    {
      this.dataStopVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerFlow(opened: boolean, value: null | PipelineModel)
    {
      this.dataFlowVisible = opened
      this.dataInfo = value
    }
  }
})
</script>
