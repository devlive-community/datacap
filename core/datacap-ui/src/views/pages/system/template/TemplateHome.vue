<template>
  <div class="w-full">
    <Card>
      <template #title>{{ $t('template.common.list') }}</template>
      <template #extra>
        <Button size="icon" class="ml-auto gap-1 h-6 w-6" @click="handlerInfo(true, null)">
          <Plus :size="20"/>
        </Button>
      </template>
      <TableCommon :loading="loading" :columns="headers" :data="data" :pagination="pagination" @changePage="handlerChangePage">
        <template #plugin="{row}">
          <div class="flex items-center p-4 sm:justify-between">
            <div class="flex -space-x-2 overflow-hidden">
              <Avatar v-for="item in row?.plugin.split(',')" size="sm" class="border-2 border-background w-8 h-8">
                <AvatarImage :src="'/static/images/plugin/' + item + '.png'"/>
                <AvatarFallback>{{ item }}</AvatarFallback>
              </Avatar>
            </div>
          </div>
        </template>
        <template #system="{ row }">
          <Switch disabled :default-checked="row?.system"/>
        </template>
        <template #action="{row}">
          <TooltipProvider :delay-duration="0">
            <Tooltip>
              <TooltipTrigger as-child>
                <Button variant="outline" size="icon" class="rounded-full w-8 h-8" @click="handlerInfo(true, row)">
                  <Pencil :size="15"></Pencil>
                </Button>
              </TooltipTrigger>
              <TooltipContent>
                <p>{{ $t('common.editData') }}</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </template>
      </TableCommon>
    </Card>
  </div>
  <TemplateInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerInfo(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { Cog, Import, Pencil, Plus } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { FilterModel } from '@/model/filter'
import { createHeaders } from '@/views/pages/system/template/TemplateUtils'
import { useI18n } from 'vue-i18n'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import TemplateService from '@/services/template'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { TemplateModel } from '@/model/template'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Switch } from '@/components/ui/switch'
import TemplateInfo from '@/views/pages/system/template/TemplateInfo.vue'

export default defineComponent({
  name: 'TemplateHome',
  components: {
    TemplateInfo,
    Switch,
    Button,
    Pencil, Plus, Import, Cog,
    TableCommon,
    Card,
    Tooltip, TooltipContent, TooltipProvider, TooltipTrigger,
    Avatar, AvatarFallback, AvatarImage
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
      dataInfoVisible: false,
      data: [] as TemplateModel[],
      pagination: {} as PaginationModel,
      dataInfo: null as TemplateModel | null
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
      TemplateService.getAll(this.filter)
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
    handlerInfo(opened: boolean, value: null | TemplateModel)
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
