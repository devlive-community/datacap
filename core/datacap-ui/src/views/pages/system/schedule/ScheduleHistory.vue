<template>
  <Dialog :open="isVisible" persistent @update:open="handlerCancel">
    <DialogContent class="min-w-[60%]">
      <DialogHeader class="border-b">
        <DialogTitle class="pb-3.5">{{ $t('schedule.common.history') }}</DialogTitle>
        <DialogDescription></DialogDescription>
      </DialogHeader>
      <CardContent class="grid gap-4">
        <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage"></TableCommon>
      </CardContent>
    </DialogContent>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { ScheduleModel } from '@/model/schedule'
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { CardContent } from '@/components/ui/card'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { FilterModel } from '@/model/filter'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { useI18n } from 'vue-i18n'
import { createHistoryHeaders } from '@/views/pages/system/schedule/ScheduleUtils'
import ScheduleService from '@/services/schedule'
import { cn } from '@/lib/utils'

export default defineComponent({
  name: 'ScheduleHistory',
  components: {
    TableCommon,
    CardContent,
    DialogDescription, DialogTitle, DialogHeader, DialogContent, Dialog
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => ScheduleModel | null,
      default: null
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
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const headers = createHistoryHeaders(useI18n())

    return {
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
    cn,
    handlerInitialize()
    {
      this.loading = true
      ScheduleService.getScheduleHistory(this.filter, this.info?.id as number)
          .then((response) => {
            if (response.status) {
              this.data = response.data.content
              this.pagination = PaginationRequest.of(response.data)
            }
          })
          .finally(() => {
            this.loading = false
          })
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
    }
  }
})
</script>