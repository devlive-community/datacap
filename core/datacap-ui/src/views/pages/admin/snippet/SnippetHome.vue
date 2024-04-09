<template>
  <div class="w-full">
    <Card>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
      </TableCommon>
    </Card>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import { FilterModel } from '@/model/filter.ts'
import { createHeaders } from '@/views/pages/admin/snippet/SnippetUtils'
import { useI18n } from 'vue-i18n'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import SnippetService from '@/services/snippet'
import { ToastUtils } from '@/utils/toast'

export default defineComponent({
  name: 'SnippetHome',
  components: {
    TableCommon,
    Card
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const headers = createHeaders(useI18n())
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
    handlerInitialize()
    {
      this.loading = true
      SnippetService.getAll(this.filter)
                    .then(response => {
                      if (response.status) {
                        this.data = response.data.content
                        this.pagination = PaginationRequest.of(response.data)
                      }
                      else {
                        ToastUtils.error(response.message)
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
