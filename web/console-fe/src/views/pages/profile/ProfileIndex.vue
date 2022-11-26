<template>
  <div>
    <Row :gutter="16">
      <Col span="24">
        <Card style="width:100%">
          <template #title>
            {{ $t('common.contribution') }}
          </template>
          <div :style="{ background: '#fff', minHeight: '150px' }">
            <Spin fix v-if="loading"/>
            <CalendarChart v-else :configure="summary.contributionConfigure"></CalendarChart>
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
import CalendarChart from "@/charts/calendar/CalendarChart.vue";
import {AdminService} from "@/services/AdminService";
import RadarChart from "@/charts/radar/RadarChart.vue";
import {HttpCommon} from "@/common/HttpCommon";
import {CalendarConfigure} from "@/charts/calendar/CalendarConfigure";
import {RadarConfigure} from "@/charts/radar/RadarConfigure";

export default defineComponent({
  components: {CalendarChart, RadarChart},
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
