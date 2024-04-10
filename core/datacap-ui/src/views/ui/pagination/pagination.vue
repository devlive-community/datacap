<template>
  <div v-if="pagination" class="flex w-full justify-center mt-3">
    <Pagination v-slot="{ page }" :total="pagination.total" :items-per-page="pagination.pageSize" :sibling-count="1" show-edges
                :default-page="pagination.currentPage + 1" @update:page="handlerChangePage($event)">
      <PaginationList v-slot="{ items }" class="flex items-center gap-1">
        <PaginationFirst class="w-6 h-6"/>
        <PaginationPrev class="w-6 h-6"/>
        <template v-for="(item, index) in items">
          <PaginationListItem v-if="item.type === 'page'" :key="index" :value="item.value" class="w-6 h-6" as-child>
            <Button class="w-6 h-6 p-0" size="sm" :variant="item.value === page ? 'default' : 'outline'">
              {{ item.value }}
            </Button>
          </PaginationListItem>
          <PaginationEllipsis class="w-6 h-6" v-else :key="item.type" :index="index"/>
        </template>
        <PaginationNext class="w-6 h-6"/>
        <PaginationLast class="w-6 h-6"/>
      </PaginationList>
    </Pagination>
    <div v-if="pagination" class="ml-2">
      <Select :defaultValue="pagination.pageSize.toString()" @update:modelValue="handlerChangeSize">
        <SelectTrigger class="h-6">
          <SelectValue placeholder="Select a size"/>
        </SelectTrigger>
        <SelectContent>
          <SelectItem class="h-6 cursor-pointer" :value="'10'">10</SelectItem>
          <SelectItem class="h-6 cursor-pointer" :value="'20'">20</SelectItem>
          <SelectItem class="h-6 cursor-pointer" :value="'30'">30</SelectItem>
          <SelectItem class="h-6 cursor-pointer" :value="'50'">50</SelectItem>
          <SelectItem class="h-6 cursor-pointer" :value="'100'">100</SelectItem>
        </SelectContent>
      </Select>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Pagination, PaginationEllipsis, PaginationFirst, PaginationLast, PaginationList, PaginationListItem, PaginationNext, PaginationPrev } from '@/components/ui/pagination'
import { PaginationModel } from '@/model/pagination.ts'
import { cloneDeep, toNumber } from 'lodash'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import Button from '@/views/ui/button'

export default defineComponent({
  name: 'DcPagination',
  components: {
    Pagination, PaginationEllipsis, PaginationFirst, PaginationLast, PaginationList, PaginationListItem, PaginationNext, PaginationPrev,
    SelectItem, SelectContent, SelectValue, SelectTrigger, Select,
    Button
  },
  props: {
    pagination: {
      type: Object as () => PaginationModel
    }
  },
  methods: {
    handlerChangePage(value: number)
    {
      const pagination: PaginationModel = cloneDeep(this.pagination) as PaginationModel
      pagination.currentPage = value
      this.$emit('changePage', pagination)
    },
    handlerChangeSize(value: string)
    {
      const pagination: PaginationModel = cloneDeep(this.pagination) as PaginationModel
      pagination.pageSize = toNumber(value)
      this.$emit('changePage', pagination)
    }
  }
})
</script>
