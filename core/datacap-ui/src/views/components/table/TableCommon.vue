<template>
  <div>
    <!--    <div class="flex gap-2 items-center py-2">-->
    <!--      <DropdownMenu>-->
    <!--        <DropdownMenuTrigger as-child>-->
    <!--          <Button variant="outline" class="ml-auto">-->
    <!--            {{ $t('common.column') }}-->
    <!--            <ChevronDown class="ml-2 h-4 w-4"/>-->
    <!--          </Button>-->
    <!--        </DropdownMenuTrigger>-->
    <!--        <DropdownMenuContent align="end">-->
    <!--          <DropdownMenuCheckboxItem v-for="item in columns" class="capitalize" :key="item.id" :checked="item.hidden" @update:checked="(value) => { item.hidden = value}">-->
    <!--            {{ item.header }}-->
    <!--          </DropdownMenuCheckboxItem>-->
    <!--        </DropdownMenuContent>-->
    <!--      </DropdownMenu>-->
    <!--    </div>-->
    <div class="rounded-md mt-4">
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead v-for="item in columns" :key="item.key">{{ item.header }}</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="row in data" :key="row.id">
            <TableCell v-for="column in columns" :key="column.key">
              <template v-if="column.slot">
                <slot :name="column.slot" :row="row"></slot>
              </template>
              <template v-else>{{ row[column.key] }}</template>
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </div>
    <div v-if="pagination" class="flex w-full justify-center mt-3">
      <Pagination v-slot="{ page }" :total="pagination.total" :items-per-page="pagination.pageSize" :sibling-count="1" show-edges
                  :default-page="pagination.currentPage + 1" @update:page="handlerChangePage($event)">
        <PaginationList v-slot="{ items }" class="flex items-center gap-1">
          <PaginationFirst></PaginationFirst>
          <PaginationPrev></PaginationPrev>
          <template v-for="(item, index) in items">
            <PaginationListItem v-if="item.type === 'page'" :key="index" :value="item.value" as-child>
              <Button class="w-10 h-10 p-0" size="sm" :variant="item.value === page ? 'default' : 'outline'">
                {{ item.value }}
              </Button>
            </PaginationListItem>
            <PaginationEllipsis v-else :key="item.type" :index="index"/>
          </template>
          <PaginationNext></PaginationNext>
          <PaginationLast></PaginationLast>
        </PaginationList>
      </Pagination>
      <div class="mt-0.5 ml-2">
        <Select v-model="pagination.pageSize" :defaultValue="pagination.pageSize" @update:modelValue="handlerChangeSize">
          <SelectTrigger class="w-[70px]">
            <SelectValue placeholder="Select a fruit"/>
          </SelectTrigger>
          <SelectContent>
            <SelectItem :value="10">10</SelectItem>
            <SelectItem :value="20">20</SelectItem>
            <SelectItem :value="50">50</SelectItem>
            <SelectItem :value="100">100</SelectItem>
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
import { ArrowUpDown, ChevronDown } from 'lucide-vue-next'
import { Pagination, PaginationEllipsis, PaginationFirst, PaginationLast, PaginationNext, PaginationPrev } from '@/components/ui/pagination'
import { PaginationList, PaginationListItem } from 'radix-vue'
import { PaginationModel } from '@/model/pagination'
import { cloneDeep, toNumber } from 'lodash'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';

export default defineComponent({
  name: 'TableCommon',
  components: {
    SelectItem, SelectContent, SelectValue, SelectTrigger, Select,
    PaginationLast, PaginationNext, PaginationEllipsis, PaginationPrev, PaginationListItem, PaginationList, Pagination, PaginationFirst,
    DropdownMenuCheckboxItem, DropdownMenuContent, DropdownMenuTrigger, DropdownMenu,
    TableCell, TableRow, TableBody, TableHeader, TableHead, Table, TableCaption,
    Button, Input,
    ArrowUpDown, ChevronDown
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
      type: PaginationModel
    }
  },
  methods: {
    handlerChangePage(value: number)
    {
      const pagination: PaginationModel = cloneDeep(this.pagination)
      pagination.currentPage = value
      this.$emit('changePage', pagination)
    },
    handlerChangeSize(value: string)
    {
      const pagination: PaginationModel = cloneDeep(this.pagination)
      pagination.pageSize = toNumber(value)
      this.$emit('changePage', pagination)
    }
  }
})
</script>
