<template>
  <Dialog :open="isVisible" persistent @update:open="handlerCancel">
    <DialogContent class="min-w-[60%]">
      <DialogHeader class="border-b">
        <DialogTitle class="pb-3.5">
          {{ `[ ${info?.name} ] ${$t('dataset.common.history')}` }}
        </DialogTitle>
        <DialogDescription></DialogDescription>
      </DialogHeader>
      <CardContent class="grid gap-4">
        <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
          <template #state="{ row }">
            <Badge :style="{backgroundColor: Common.getColor(row?.state)}">
              <HoverCard v-if="row?.state === 'FAILURE'">
                <HoverCardTrigger as-child>
                  <Button variant="link">
                    {{ getStateText(row?.state) }}
                  </Button>
                </HoverCardTrigger>
                <HoverCardContent class="w-80">
                  {{ row?.message }}
                </HoverCardContent>
              </HoverCard>
              <span v-else>{{ getStateText(row?.state) }}</span>
            </Badge>
          </template>
        </TableCommon>
      </CardContent>
    </DialogContent>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { CardContent } from '@/components/ui/card'
import { Dialog, DialogContent, DialogHeader } from '@/components/ui/dialog'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { createHistoryHeaders } from './DatasetUtils'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import DatasetService from '@/services/dataset'
import { DatasetModel } from '@/model/dataset'
import { HoverCard, HoverCardContent, HoverCardTrigger } from '@/components/ui/hover-card'
import { Badge } from '@/components/ui/badge'
import Common from '@/utils/common'

export default defineComponent({
  name: 'DatasetHistory',
  components: {
    Badge,
    TableCommon,
    DialogHeader, Dialog, CardContent, DialogContent,
    HoverCard, HoverCardContent, HoverCardTrigger
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => DatasetModel | null
    }
  },
  computed: {
    Common()
    {
      return Common
    },
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
  setup()
  {
    const i18n = useI18n()
    const filter: FilterModel = new FilterModel()
    const headers = createHistoryHeaders(i18n)

    return {
      i18n,
      filter,
      headers
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
      DatasetService.getHistory(this.info?.code as string, this.filter)
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
    getStateText(origin: string): string
    {
      return Common.getText(this.i18n, origin)
    }
  }
})
</script>
