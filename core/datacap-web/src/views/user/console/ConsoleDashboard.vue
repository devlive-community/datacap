<template>
  <div class="home">
    <CircularLoading v-if="loading"
                     :show="loading">
    </CircularLoading>
    <Card v-else
          dis-hover
          :bordered="false">
      <template #title>
        {{ $t('common.myDashboard') }}
      </template>
      <template #extra>
        <Button type="primary"
                shape="circle"
                icon="md-add"
                size="small"
                @click="handlerInfo(true, null)">
        </Button>
      </template>
      <Row :gutter="16">
        <Col v-for="item in data"
             :key="item.id"
             span="4">
          <Card class="content-center"
                padding="0"
                style="margin-top: 5px;"
                dis-hover>
            <template #title>
              {{ item.name }}
            </template>
            <DashboardView :configure="JSON.parse(item.configure)"/>
          </Card>
        </Col>
      </Row>
    </Card>
    <DashboardInfo v-if="dashboardInfoVisible"
                   :is-visible="dashboardInfoVisible"
                   :id="dashboardId"
                   @close="handlerInfo(false, null)">
    </DashboardInfo>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import DashboardService from "@/services/DashboardService";
import {Filter} from "@/model/Filter";
import DashboardInfo from "@/views/user/console/DashboardInfo.vue";
import DashboardView from "@/components/editor/dashboard/DashboardView.vue";

export default defineComponent({
  name: "ConsoleDashboard",
  components: {DashboardView, DashboardInfo, CircularLoading},
  data()
  {
    return {
      loading: false,
      dashboardInfoVisible: false,
      dashboardId: null,
      data: []
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      const filter = new Filter();
      filter.size = 1000;
      DashboardService.getAll(filter)
        .then(response => {
          if (response.status) {
            this.data = response.data.content;
          }
        })
        .finally(() => this.loading = false)
    },
    handlerInfo(opened: boolean, id: number)
    {
      this.dashboardInfoVisible = opened
      this.dashboardId = id
      if (!opened) {
        this.handlerInitialize()
      }
    }
  }
});
</script>
<style scoped>
.content-center {
  text-align: center;
  min-height: 200px;
}
</style>

