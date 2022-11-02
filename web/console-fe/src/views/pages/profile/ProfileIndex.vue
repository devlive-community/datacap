<template>
  <a-layout-content>
    <a-card size="small">
      <template #title>
        {{$t('common.contribution')}}
      </template>
      <div :style="{ background: '#fff', minHeight: '280px' }">
        <CalendarChart v-if="data.length > 0" :data="data"></CalendarChart>
      </div>
    </a-card>
  </a-layout-content>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import CalendarChart from "@/charts/calendar/CalendarChart.vue";
import {AdminService} from "@/services/AdminService";

export default defineComponent({
  components: {CalendarChart},
  data()
  {
    return {
      data: []
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      new AdminService()
        .getUserContribution()
        .then((response) => {
          if (response.status) {
            this.data = response.data;
          }
        });
    }
  }
});
</script>
