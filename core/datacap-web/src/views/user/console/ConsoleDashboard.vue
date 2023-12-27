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
                @click="$router.push('/admin/dashboard/create')">
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
            <DashboardView :configure="JSON.parse(item.configure)"
                           :fit-view="true">
            </DashboardView>
            <div style="text-align: right;">
              <Dropdown style="margin-left: 20px; padding: 5px;">
                <Button size="small">
                  <Icon type="ios-more"></Icon>
                </Button>
                <template #list>
                  <DropdownMenu>
                    <RouterLink target="_blank" :to="'/preview/dashboard/'+ item.id">
                      <DropdownItem>
                        <FontAwesomeIcon icon="eye"/>
                        {{ $t('common.preview') }}
                      </DropdownItem>
                    </RouterLink>
                    <DropdownItem @click="handlerInfo(true, item.id, item.version)">
                      <FontAwesomeIcon icon="pen-square"/>
                      {{ $t('common.modify') }}
                    </DropdownItem>
                    <DropdownItem @click="handlerDelete(true, item)">
                      <FontAwesomeIcon icon="trash"/>
                      {{ $t('common.delete') }}
                    </DropdownItem>
                  </DropdownMenu>
                </template>
              </Dropdown>
            </div>
          </Card>
        </Col>
      </Row>
    </Card>
    <DashboardInfo v-if="dashboardInfoVisible"
                   :is-visible="dashboardInfoVisible"
                   :id="dashboardId"
                   @close="handlerInfo(false, null)">
    </DashboardInfo>
    <DashboardDelete v-if="dashboardDeleteVisible"
                     :is-visible="dashboardDeleteVisible"
                     :data="contextData"
                     @close="handlerDelete(false, null)">
    </DashboardDelete>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import DashboardService from "@/services/DashboardService";
import {Filter} from "@/model/Filter";
import DashboardInfo from "@/views/user/console/DashboardInfo.vue";
import DashboardView from "@/components/editor/dashboard/DashboardView.vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import DashboardDelete from "@/views/user/console/components/DashboardDelete.vue";

export default defineComponent({
  name: "ConsoleDashboard",
  components: {DashboardDelete, FontAwesomeIcon, DashboardView, DashboardInfo, CircularLoading},
  data()
  {
    return {
      loading: false,
      dashboardInfoVisible: false,
      dashboardId: null,
      data: [],
      dashboardDeleteVisible: false,
      contextData: null
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
    handlerInfo(opened: boolean, id: number, version?: string)
    {
      this.dashboardInfoVisible = opened
      this.dashboardId = id
      if (!opened) {
        this.handlerInitialize()
      }
      if (version && version === 'V2') {
        this.$router.push('/admin/dashboard/edit/' + id)
      }
    },
    handlerDelete(opened: boolean, data: any)
    {
      this.dashboardDeleteVisible = opened
      this.contextData = data
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

