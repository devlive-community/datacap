<template>
  <Dialog :is-visible="visible" :title="$t('report.common.list')" width="60%">
    <CircularLoading v-if="loading" :show="loading"/>
    <div class="p-2" v-else>
      <FormField type="radio" name="theme">
        <FormItem class="space-y-1">
          <FormMessage/>
          <RadioGroup v-model="report" class="grid w-full grid-cols-4 gap-8 pt-2">
            <FormItem v-for="item of data" :key="item.id">
              <FormLabel class="[&:has([data-state=checked])>div]:border-primary">
                <FormControl>
                  <RadioGroupItem :value="item.id as unknown as string" class="sr-only"/>
                </FormControl>
                <div class="items-center rounded-md border-4 border-muted p-1 hover:border-accent cursor-pointer text-center">
                  <VisualView width="200px" height="100px" :code="item.dataset?.code as string" :configuration="JSON.parse(item.configure as string)"
                              :query="JSON.parse(item.query as string)"/>
                </div>
                <span class="block w-full p-2 text-center font-normal">{{ item.name }}</span>
              </FormLabel>
            </FormItem>
          </RadioGroup>
        </FormItem>
      </FormField>
      <Pagination :pagination="pagination" @changePage="handlerChangePage"/>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="destructive" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" @click="handlerSave()">
          {{ $t('common.save') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import ReportService from '@/services/report.ts'
import { FilterModel } from '@/model/filter.ts'
import { ReportModel } from '@/model/report.ts'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group'
import VisualView from '@/views/components/visual/VisualView.vue'
import Button from '@/views/ui/button'
import { toNumber } from 'lodash'
import Pagination from '@/views/ui/pagination'
import { PaginationModel, PaginationRequest } from '@/model/pagination.ts'

export default defineComponent({
  name: 'ChartContainer',
  components: {
    Pagination,
    VisualView,
    CircularLoading,
    Dialog,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage,
    RadioGroup, RadioGroupItem,
    Button
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
  props: {
    isVisible: {
      type: Boolean
    }
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    filter.size = 12

    return {
      filter
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  data()
  {
    return {
      loading: false,
      data: [] as ReportModel[],
      report: '',
      pagination: {} as PaginationModel
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      ReportService.getAll(this.filter)
                   .then(response => {
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
    handlerSave()
    {
      const node = this.data.find(item => item.id === toNumber(this.report))
      this.$emit('change', node)
      this.handlerCancel()
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
