<template>
  <div class="w-full">
    <div>
      <h3 class="text-lg font-medium">{{ $t('user.common.info') }}</h3>
      <p class="text-sm text-muted-foreground">{{ $t('user.tip.info') }}</p>
    </div>
    <Separator class="my-4"/>
    <Card>
      <CardHeader class="p-0">
        <CardTitle class="pt-3 pl-3 pb-2">{{ $t('user.common.contribution') }}</CardTitle>
        <Separator/>
        <CardDescription class="pt-3 pl-3 pb-2 pr-2 min-w-full">{{ $t('user.tip.contribution') }}</CardDescription>
      </CardHeader>
      <CardContent class="pt-2">
        <CircularLoading v-if="loading" :show="loading"/>
        <CalendarHeatmap v-else :tooltip-unit="$t('heatmap.common.query')" :end-date="heatmap.endDate"
                         :round="50" :values="heatmap.data"
                         :locale="{
                                  months: [
                                            $t('heatmap.common.jan'),
                                            $t('heatmap.common.feb'),
                                            $t('heatmap.common.mar'),
                                            $t('heatmap.common.apr'),
                                            $t('heatmap.common.mai'),
                                            $t('heatmap.common.jun'),
                                            $t('heatmap.common.jul'),
                                            $t('heatmap.common.aug'),
                                            $t('heatmap.common.sep'),
                                            $t('heatmap.common.okt'),
                                            $t('heatmap.common.nov'),
                                            $t('heatmap.common.dez')
                                          ],
                                  days  : [
                                            $t('heatmap.common.so'),
                                            $t('heatmap.common.mo'),
                                            $t('heatmap.common.di'),
                                            $t('heatmap.common.mi'),
                                            $t('heatmap.common.do'),
                                            $t('heatmap.common.fr'),
                                            $t('heatmap.common.sa')
                                           ],
                                  on    : $t('heatmap.common.am'),
                                  less  : $t('heatmap.common.less'),
                                  more  : $t('heatmap.common.more')
                             }"/>
      </CardContent>
    </Card>
    <div class="flex mt-2 space-x-4">
      <Card class="w-1/2">
        <CardHeader class="p-0">
          <CardTitle class="pt-3 pl-3 pb-2">{{ $t('user.common.radar7Days') }}</CardTitle>
          <Separator/>
          <CardDescription class="pt-3 pl-3 pb-2 pr-2 min-w-full">{{ $t('user.tip.radar7Days') }}</CardDescription>
        </CardHeader>
        <CardContent class="pt-2">
          <CircularLoading v-if="loading" :show="loading"/>
          <VisualPie v-else-if="radar.configuration" :configuration="radar.configuration" :height="'200px'" :submitted="false"/>
        </CardContent>
      </Card>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Separator } from '@/components/ui/separator'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { CalendarHeatmap } from 'vue3-calendar-heatmap'
import './vue3-calendar-heatmap.css'
import { HttpUtils } from '@/utils/http'
import UserService from '@/services/user'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { Configuration } from '@/views/components/visual/Configuration'
import VisualPie from '@/views/components/visual/components/VisualPie.vue'
import { DateUtils } from '@/utils/date'

export default defineComponent({
  name: 'InfoHome',
  components: {
    VisualPie,
    CircularLoading,
    CardContent, CardDescription, CardHeader, Card, CardTitle,
    Separator,
    CalendarHeatmap
  },
  data()
  {
    return {
      loading: false,
      heatmap: {
        data: [],
        endDate: null as string | null
      },
      radar: {
        configuration: null as Configuration | null
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
      axios.all([UserService.getUserContribution(), UserService.getUserContributionRadar()])
          .then(axios.spread((fetchContribution, fetchRadar) => {
            if (fetchContribution.status) {
              this.heatmap.data = fetchContribution.data
              if (fetchContribution.data.length > 0) {
                if (this.heatmap.data.length > 0) {
                  const item = this.heatmap.data[this.heatmap.data.length - 1] as any
                  this.heatmap.endDate = item.date
                }
              }
              else {
                const now = new Date()
                this.heatmap.endDate = DateUtils.formatTime(now, 'YYYY-MM-DD')
              }
            }
            if (fetchRadar.status) {
              const configuration = new Configuration()
              configuration.columns = fetchRadar.data
              configuration.chartConfigure = {yAxis: 'count', xAxis: 'label', outerRadius: [1.2]}
              this.radar.configuration = configuration
            }
          }))
          .finally(() => this.loading = false)
    }
  }
});
</script>
