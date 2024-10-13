<template>
  <div class="hidden flex-col md:flex">
    <div class="flex-1 space-y-4 p-8 pt-6">
      <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <DataCapCard :loading="loading">
          <template #title>{{ $t('user.common.sourceCount') }}</template>
          <template #content>
            <div class="p-5">
              <div class="text-2xl font-bold">{{ summary.sourceCount }}</div>
              <p class="text-xs text-muted-foreground mt-2">{{ $t('user.tip.sourceCountTip') }}</p>
            </div>
          </template>
        </DataCapCard>
        <DataCapCard :loading="loading">
          <template #title>{{ $t('user.common.queryCount') }}</template>
          <template #content>
            <div class="p-5">
              <div class="text-2xl font-bold">{{ summary.queryCount }}</div>
              <p class="text-xs text-muted-foreground mt-2">{{ $t('user.tip.queryCountTip') }}</p>
            </div>
          </template>
        </DataCapCard>
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
import { DataCapCard } from '@/views/ui/card'

export default defineComponent({
  name: 'DashboardHome',
  components: {
    DataCapCard,
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
