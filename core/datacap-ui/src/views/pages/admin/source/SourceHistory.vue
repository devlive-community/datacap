<template>
  <Dialog :is-visible="visible" :title="$t('source.common.syncHistory')" :width="'60%'">
    <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
      <template #elapsed="{row}">
        {{ (getTime(row.updateTime) - getTime(row.createTime)) / 1000 }}
      </template>
      <template #state="{row}">
        <Badge :style="{backgroundColor: Common.getColor(row?.state)}">
          <HoverCard v-if="row?.state === 'FAILURE'">
            <HoverCardTrigger as-child>
              <Button variant="link">
                {{ getStateText(row?.state) }}
              </Button>
            </HoverCardTrigger>
            <HoverCardContent class="w-full">
              {{ row?.message }}
            </HoverCardContent>
          </HoverCard>
          <span v-else>{{ getStateText(row?.state) }}</span>
        </Badge>
      </template>
      <template #result="{row}">
        <HoverCard>
          <HoverCardTrigger as-child>
            <Eye class="cursor-pointer"/>
          </HoverCardTrigger>
          <HoverCardContent class="w-full">
            <MdPreview :modelValue="toMarkdown(row.info)" style="padding: 0"/>
          </HoverCardContent>
        </HoverCard>
      </template>
    </TableCommon>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import { SourceModel } from '@/model/source'
import SourceService from '@/services/source'
import Button from '@/views/ui/button'
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { createHistoryHeaders } from '@/views/pages/admin/source/SourceUtils'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { Badge } from '@/components/ui/badge'
import { HoverCard, HoverCardContent, HoverCardTrigger } from '@/components/ui/hover-card'
import Common from '@/utils/common'
import { Eye } from 'lucide-vue-next'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

export default defineComponent({
  name: 'SourceHistory',
  components: {
    Badge,
    Button,
    Dialog,
    TableCommon,
    HoverCard, HoverCardContent, HoverCardTrigger,
    Eye,
    MdPreview
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
    },
    Common()
    {
      return Common
    }
  },
  props: {
    isVisible: {
      type: Boolean
    },
    info: {
      type: Object as () => SourceModel | null
    }
  },
  setup()
  {
    const i18n = useI18n()
    const filter: FilterModel = new FilterModel()
    const headers = createHistoryHeaders(i18n)

    return {
      filter,
      headers,
      i18n
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
      SourceService.getHistory(this.info?.id as number, this.filter)
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
    handlerCancel()
    {
      this.visible = false
    },
    getTime(time: any)
    {
      return time ? new Date(time).getTime() : 0
    },
    getStateText(origin: string): string
    {
      return Common.getText(this.i18n, origin)
    },
    toMarkdown(content: string)
    {
      return '```json\n' + JSON.stringify(content, null, 4) + '\n```'
    }
  }
})
</script>
