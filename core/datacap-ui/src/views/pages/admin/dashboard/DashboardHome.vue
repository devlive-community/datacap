<template>
  <div class="w-full">
    <Card>
      <CardHeader class="border-b p-4">
        <CardTitle>{{ $t('dashboard.common.list') }}</CardTitle>
      </CardHeader>
      <CardContent>
        <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
        <div v-else class="hidden flex-col md:flex">
          <div class="flex-1 space-y-4 pt-6">
            <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
              <Card v-for="item in data">
                <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle class="text-sm font-medium">{{ item.name }}</CardTitle>
                </CardHeader>
                <CardContent>
                  <div class="text-2xl font-bold"></div>
                  <p class="text-xs text-muted-foreground mt-2">{{ item.createTime }}</p>
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Loader2 } from 'lucide-vue-next';
import DashboardService from '@/services/dashboard'
import { FilterModel } from '@/model/filter'
import { PaginationModel, PaginationRequest } from '@/model/pagination'

export default defineComponent({
  name: 'DashboardHome',
  components: {
    Loader2,
    CardContent, CardHeader, CardTitle, Card
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
      DashboardService.getAll(this.filter)
          .then(response => {
            if (response.status) {
              this.data = response.data.content
              this.pagination = PaginationRequest.of(response.data)
            }
          })
          .finally(() => this.loading = false)
    }
  }
})
</script>
