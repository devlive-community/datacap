<template>
  <div class="home">
    <div style="">
      <a-row :gutter="16">
        <a-col :span="6">
          <a-card :loading="summaryLoading" :bodyStyle="{'text-align': 'center'}">
            <a-statistic :value="count.source" :value-style="{ color: '#3f8600' }">
              <template #title>
                <span>Source Count</span>
                <a-tooltip placement="right">
                  <template #title>
                    <span>Statistics on the total number of access data sources</span>
                  </template>
                  <question-circle-two-tone style="margin-left: 5px" />
                </a-tooltip>
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card :loading="summaryLoading" :bodyStyle="{'text-align': 'center'}">
            <a-statistic :value="count.history" :value-style="{ color: '#3f8600' }">
              <template #title>
                <span>History Count</span>
                <a-tooltip placement="right">
                  <template #title>
                    <span>Statistics The total number of query histories of access data sources</span>
                  </template>
                  <question-circle-two-tone style="margin-left: 5px" />
                </a-tooltip>
              </template>
            </a-statistic>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
import { HttpCommon } from "@/common/HttpCommon";
import { AdminService } from "@/services/AdminService";
import { defineComponent } from "vue";

export default defineComponent({
  name: "DashboardConsoleView",
  components: {},
  data() {
    return {
      summaryLoading: false,
      count: {
        source: 0,
        history: 0
      }
    }
  },
  created() {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize() {
      this.summaryLoading = true;
      const adminService = new AdminService();
      const axios = new HttpCommon().getAxios();
      axios.all([adminService.getSourceCount(), adminService.getPluginHistoryCount()])
        .then(axios.spread((sourceCount, pluginHistoryCount) => {
          if (sourceCount.status) {
            this.count.source = sourceCount.data;
          }
          if (pluginHistoryCount.status) {
            this.count.history = pluginHistoryCount.data;
          }
        })).finally(() => {
          this.summaryLoading = false;
        });
    }
  }
});
</script>
