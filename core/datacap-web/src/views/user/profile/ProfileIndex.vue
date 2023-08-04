<template>
  <div>
    <Row :gutter="16">
      <Col span="24">
        <Card style="width:100%">
          <template #title>
            {{ $t('common.contribution') }}
          </template>
          <div :style="{ background: '#fff', minHeight: '250px' }">
            <Spin v-if="loading"
                  fix>
            </Spin>
            <CalendarHeatmap v-else
                             :tooltip-unit="$t('calendarHeatmap.query')"
                             :end-date="endDate"
                             :round="50"
                             :locale="{
                                  months: [
                                            $t('calendarHeatmap.jan'),
                                            $t('calendarHeatmap.feb'),
                                            $t('calendarHeatmap.mar'),
                                            $t('calendarHeatmap.apr'),
                                            $t('calendarHeatmap.mai'),
                                            $t('calendarHeatmap.jun'),
                                            $t('calendarHeatmap.jul'),
                                            $t('calendarHeatmap.aug'),
                                            $t('calendarHeatmap.sep'),
                                            $t('calendarHeatmap.okt'),
                                            $t('calendarHeatmap.nov'),
                                            $t('calendarHeatmap.dez')
                                          ],
                                  days  : [
                                            $t('calendarHeatmap.so'),
                                            $t('calendarHeatmap.mo'),
                                            $t('calendarHeatmap.di'),
                                            $t('calendarHeatmap.mi'),
                                            $t('calendarHeatmap.do'),
                                            $t('calendarHeatmap.fr'),
                                            $t('calendarHeatmap.sa')
                                           ],
                                  on    : $t('calendarHeatmap.am'),
                                  less  : $t('calendarHeatmap.less'),
                                  more  : $t('calendarHeatmap.more')
                             }"
                             :values="heatmapActivity">
            </CalendarHeatmap>
          </div>
        </Card>
      </Col>
      <Col span="8" style="margin-top: 10px;">
        <Card style="width:100%">
          <template #title>
            {{ $t('profile.radar7Days') }}
          </template>
          <div :style="{ minHeight: '150px' }">
            <Spin fix v-if="loading"/>
            <RadarChart v-else :configure="summary.radarConfigure"></RadarChart>
          </div>
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {AdminService} from "@/services/AdminService";
import RadarChart from "@/charts/radar/RadarChart.vue";
import {HttpCommon} from "@/common/HttpCommon";
import {RadarConfigure} from "@/charts/radar/RadarConfigure";
import {CalendarHeatmap} from "vue3-calendar-heatmap";
import "@/css/vue3-calendar-heatmap.css"

export default defineComponent({
  components: {RadarChart, CalendarHeatmap},
  data()
  {
    return {
      loading: false,
      heatmapActivity: null,
      endDate: null,
      summary: {
        radarConfigure: RadarConfigure
      }
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      const adminService = new AdminService();
      const axios = new HttpCommon().getAxios();
      axios.all([adminService.getUserContribution(), adminService.getUserContributionRadar()])
        .then(axios.spread((contribution, contributionRadar) => {
          if (contribution.status) {
            this.heatmapActivity = contribution.data;
            this.endDate = contribution.data[contribution.data.length - 1].date;
          }
          if (contributionRadar.status) {
            const radarConfigure = new RadarConfigure();
            radarConfigure.data = contributionRadar.data;
            this.summary.radarConfigure = radarConfigure;
          }
        }))
        .finally(() => {
          this.loading = false;
        });
    }
  }
});
</script>
