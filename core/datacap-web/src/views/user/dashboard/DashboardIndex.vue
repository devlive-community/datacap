<template>
  <div class="home">
    <Row style="margin: 0px 5px;" :gutter="16">
      <Col span="6">
        <Card class="content-center" dis-hover>
          <NumberInfo>
            <template #title>
              Source Count
              <Tooltip>
                <template #content>
                  <div style="white-space: normal;">
                    Statistics on the total number of access data sources
                  </div>
                </template>
                <Icon type="md-help-circle"/>
              </Tooltip>
            </template>
            <template #total>
              <Numeral :value="count.source" format="0,0"/>
            </template>
          </NumberInfo>
        </Card>
      </Col>
      <Col span="6">
        <Card class="content-center" dis-hover>
          <NumberInfo>
            <template #title>
              History Count
              <Tooltip>
                <template #content>
                  <div style="white-space: normal;">
                    Statistics The total number of query histories of access data sources
                  </div>
                </template>
                <Icon type="md-help-circle"/>
              </Tooltip>
            </template>
            <template #total>
              <Numeral :value="count.history" format="0,0"/>
            </template>
          </NumberInfo>
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts">
import {HttpCommon} from "@/common/HttpCommon";
import {AdminService} from "@/services/AdminService";
import {defineComponent} from "vue";

export default defineComponent({
  name: "DashboardIndex",
  data()
  {
    return {
      summaryLoading: false,
      count: {
        source: 0,
        history: 0
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
<style scoped>
.content-center {
  text-align: center;
}
</style>
