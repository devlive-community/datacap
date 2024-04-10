<template>
  <div class="w-full">
    <Card>
      <template #title>{{ $t('dashboard.common.list') }}</template>
      <template #extra></template>
      <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
      <div v-else class="hidden flex-col md:flex">
        <div class="flex-1 space-y-4 pt-6">
          <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
            <Card title-class="p-2 pl-4 pr-4" v-for="item in data">
              <template #title>
                <RouterLink :to="`/admin/dashboard/info/${item.id}/preview`" target="_blank">{{ item.name }}</RouterLink>
              </template>
              <template #extra>
                <DropdownMenu class="justify-items-end">
                  <DropdownMenuTrigger>
                    <Cog :size="20"/>
                  </DropdownMenuTrigger>
                  <DropdownMenuContent>
                    <DropdownMenuItem class="cursor-pointer">
                      <RouterLink :to="`/admin/dashboard/info/${item.id}`" target="_blank" class="flex items-center">
                        <Pencil :size="15" class="mr-1"/>
                        {{ $t('dashboard.common.modify') }}
                      </RouterLink>
                    </DropdownMenuItem>
                    <DropdownMenuItem class="cursor-pointer" @click="handlerDelete(true, item)">
                      <Trash :size="15" class="mr-1"/>
                      {{ $t('dashboard.common.delete') }}
                    </DropdownMenuItem>
                  </DropdownMenuContent>
                </DropdownMenu>
              </template>
              <div class="text-2xl font-bold"></div>
              <p class="text-xs text-muted-foreground mt-2">{{ item.createTime }}</p>
            </Card>
          </div>
        </div>
      </div>
    </Card>
    <DashboardDelete v-if="deleteVisible" :is-visible="deleteVisible" :data="dataInfo" @close="handlerDelete(false, null)"></DashboardDelete>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Cog, Loader2, Pencil, Trash } from 'lucide-vue-next'
import DashboardService from '@/services/dashboard'
import { FilterModel } from '@/model/filter'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { DashboardModel } from '@/model/dashboard'
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import DashboardDelete from '@/views/pages/admin/dashboard/DashboardDelete.vue'
import Card from '@/views/ui/card'

export default defineComponent({
  name: 'DashboardHome',
  components: {
    DashboardDelete,
    DropdownMenuItem, DropdownMenuSeparator, DropdownMenuLabel, DropdownMenuContent, DropdownMenuTrigger, DropdownMenu,
    Loader2, Cog, Trash, Pencil,
    Card
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()

    return {
      filter
    }
  },
  data()
  {
    return {
      loading: false,
      deleteVisible: false,
      data: [] as DashboardModel[],
      pagination: {} as PaginationModel,
      dataInfo: null as DashboardModel | null
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
      DashboardService.getAll(this.filter)
                      .then(response => {
                        if (response.status) {
                          this.data = response.data.content
                          this.pagination = PaginationRequest.of(response.data)
                        }
                      })
                      .finally(() => this.loading = false)
    },
    handlerDelete(opened: boolean, data: DashboardModel | null)
    {
      this.deleteVisible = opened
      this.dataInfo = data
      if (!opened) {
        this.handlerInitialize()
      }
    }
  }
})
</script>
