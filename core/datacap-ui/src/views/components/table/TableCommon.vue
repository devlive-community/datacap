<template>
  <div>
    <div class="rounded-md mt-4">
      <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
      <div v-else>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead v-for="item in columns" :key="item.key" :class="item.class ? item.class : ''" :style="{width: item.width ? item.width + 'px' : 'auto'}">
                {{ item.header }}
              </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            <TableRow v-for="row in data" :key="row.id">
              <TableCell v-for="column in columns" :key="column.key" :class="column.class ? column.class : ''" :style="{width: column.width ? column.width + 'px' : 'auto'}">
                <template v-if="column.slot">
                  <slot :name="column.slot" :row="row"/>
                </template>
                <template v-else-if="column.tooltip">
                  <Tooltip :content="row[column.key]">
                    <span class="cursor-pointer">{{ row[column.key] }}</span>
                  </Tooltip>
                </template>
                <template v-else-if="column.length">
                  <Tooltip :content="row[column.key]">
                    <div class="cursor-pointer flex">
                      {{ (row[column.key] as string).substring(0, column.length) }}
                      <BreadcrumbEllipsis class="-ml-2 -mt-1"/>
                    </div>
                  </Tooltip>
                </template>
                <template v-else-if="column.ellipsis">
                  <Tooltip :content="row[column.key]">
                    <div class="cursor-pointer flex">
                      {{ (row[column.key] as string).substring(0, 10) }}
                      <BreadcrumbEllipsis class="-ml-2 -mt-1"/>
                    </div>
                  </Tooltip>
                </template>
                <template v-else>{{ row[column.key] }}</template>
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>
        <TableCaption v-if="data.length === 0" class="flex w-full justify-center mt-10">
          {{ $t('common.noData') }}
        </TableCaption>
      </div>
    </div>
    <div v-if="pagination && !loading && data.length > 0" class="flex w-full justify-center mt-3">
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
            <SelectItem class="h-6 cursor-pointer" :value="'50'">50</SelectItem>
            <SelectItem class="h-6 cursor-pointer" :value="'100'">100</SelectItem>
          </SelectContent>
        </Select>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { DropdownMenu, DropdownMenuCheckboxItem, DropdownMenuContent, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { Input } from '@/components/ui/input'
import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { Button } from '@/components/ui/button'
import { ArrowUpDown, ChevronDown, Loader2 } from 'lucide-vue-next'
import { Pagination, PaginationEllipsis, PaginationFirst, PaginationLast, PaginationList, PaginationListItem, PaginationNext, PaginationPrev } from '@/components/ui/pagination'
import { PaginationModel } from '@/model/pagination'
import { cloneDeep, toNumber } from 'lodash'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import Tooltip from '@/views/ui/tooltip'
import { BreadcrumbEllipsis } from '@/components/ui/breadcrumb'

export default defineComponent({
  name: 'TableCommon',
  components: {
    BreadcrumbEllipsis,
    Loader2,
    SelectItem, SelectContent, SelectValue, SelectTrigger, Select,
    PaginationLast, PaginationNext, PaginationEllipsis, PaginationPrev, PaginationListItem, PaginationList, Pagination, PaginationFirst,
    DropdownMenuCheckboxItem, DropdownMenuContent, DropdownMenuTrigger, DropdownMenu,
    TableCell, TableRow, TableBody, TableHeader, TableHead, Table, TableCaption,
    Button, Input,
    ArrowUpDown, ChevronDown,
    Tooltip
  },
  props: {
    columns: {
      type: Array<any>(),
      default: () => []
    },
    data: {
      type: Array<any>(),
      default: () => []
    },
    pagination: {
      type: Object as () => PaginationModel
    },
    loading: {
      type: Boolean
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
