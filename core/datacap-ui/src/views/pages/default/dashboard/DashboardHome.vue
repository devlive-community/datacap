<template>
  <div class="hidden flex-col md:flex">
    <div class="flex-1 space-y-4 p-8 pt-6">
      <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle class="text-sm font-medium">{{ $t('user.common.sourceCount') }}</CardTitle>
          </CardHeader>
          <CardContent>
            <Loader2 v-if="loading" class="w-4 h-4 mr-2 animate-spin text-center"></Loader2>
            <div class="text-2xl font-bold">{{ summary.sourceCount }}</div>
            <p class="text-xs text-muted-foreground mt-2">{{ $t('user.tip.sourceCountTip') }}</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle class="text-sm font-medium">{{ $t('user.common.queryCount') }}</CardTitle>
          </CardHeader>
          <CardContent>
            <div class="text-2xl font-bold">{{ summary.queryCount }}</div>
            <p class="text-xs text-muted-foreground mt-2">{{ $t('user.tip.queryCountTip') }}</p>
          </CardContent>
        </Card>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Loader2 } from 'lucide-vue-next'
import { HttpUtils } from '@/utils/http'
import UserService from '@/services/user'

export default defineComponent({
  name: 'DashboardHome',
  components: {
    Loader2,
    CardContent, CardTitle, CardHeader, Card
  },
  data()
  {
    return {
      loading: false,
      summary: {
        sourceCount: 0,
        queryCount: 0
      }
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
      const axios = new HttpUtils().getAxios()
      axios.all([UserService.getSourceCount(), UserService.getQueryCount()])
           .then(axios.spread((source, query) => {
             if (source.status) {
               this.summary.sourceCount = source.data
             }
             if (query.status) {
               this.summary.queryCount = query.data
             }
           }))
           .finally(() => this.loading = false)
    }
  }
})
</script>
