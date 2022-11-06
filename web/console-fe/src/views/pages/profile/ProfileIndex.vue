<template>
  <a-layout-content>
    <a-card size="small" :loading="loading">
      <template #title>
        {{ $t('common.contribution') }}
      </template>
      <div :style="{ background: '#fff', minHeight: '150px' }">
        <CalendarChart v-if="summary.contribution.length > 0" :data="summary.contribution"></CalendarChart>
      </div>
    </a-card>
    <a-row :gutter="16" style="margin-top: 10px;">
      <a-col :span="8">
        <a-card size="small" :loading="loading">
          <template #title>
            {{ $t('profile.radar7Days') }}
          </template>
          <div>
            <RadarChart v-if="summary.contributionRadar.length > 0" :data="summary.contributionRadar"></RadarChart>
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

export default defineComponent({
  components: {RadarChart, CalendarChart},
  data()
  {
    return {
      loading: false,
      summary: {
        contribution: [],
        contributionRadar: []
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
            this.summary.contribution = contribution.data;
          }
          if (contributionRadar.status) {
            this.summary.contributionRadar = contributionRadar.data;
          }
        }))
        .finally(() => {
          this.loading = false;
        });
    }
  }
});
</script>
