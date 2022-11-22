<template>
  <a-layout-content>
    <a-card size="small" :loading="loading">
      <template #title>
        {{ $t('common.contribution') }}
      </template>
      <div :style="{ background: '#fff', minHeight: '150px' }">
        <CalendarChart :configure="summary.contributionConfigure"></CalendarChart>
      </div>
    </a-card>
    <a-row :gutter="16" style="margin-top: 10px;">
      <a-col :span="8">
        <a-card size="small" :loading="loading">
          <template #title>
            {{ $t('profile.radar7Days') }}
          </template>
          <div>
            <RadarChart :configure="summary.radarConfigure"></RadarChart>
          </div>
        </a-card>
      </a-col>
    </a-row>
  </a-layout-content>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import CalendarChart from "@/charts/calendar/CalendarChart.vue";
import {AdminService} from "@/services/AdminService";
import RadarChart from "@/charts/radar/RadarChart.vue";
import {HttpCommon} from "@/common/HttpCommon";
import {CalendarConfigure} from "@/charts/calendar/CalendarConfigure";
import {RadarConfigure} from "@/charts/radar/RadarConfigure";

export default defineComponent({
  components: {RadarChart, CalendarChart},
  data()
  {
    return {
      loading: false,
      summary: {
        contributionConfigure: CalendarConfigure,
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
            const contributionConfigure = new CalendarConfigure();
            contributionConfigure.data = contribution.data;
            this.summary.contributionConfigure = contributionConfigure;
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
