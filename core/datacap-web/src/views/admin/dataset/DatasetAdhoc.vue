<template>
  <div>
    <Layout style="min-height: 600px; height: auto;">
      <Sider style="max-height: 500px; height: auto; background-color: white;">
        <Content style="height: 50%; background-color: white;">
          <Divider orientation="left">
            {{ $t('dataset.columnModeMetric') }}
          </Divider>
          <Card :bordered="false"
                dis-hover
                padding="10"
                style="height: 100%; overflow: auto;">
            <Draggable item-key="id"
                       :clone="handlerClone"
                       :group="{ name: 'metrics', pull: 'clone', put: false }"
                       :list="originalMetrics">
              <template #item="{ element }">
                <Tag size="medium">
                  {{ element.name }}
                </Tag>
              </template>
            </Draggable>
          </Card>
        </Content>
        <Content style="height: 50%; background-color: white; margin-top: 50px;">
          <Divider orientation="left">
            {{ $t('dataset.columnModeDimension') }}
          </Divider>
          <Card :bordered="false"
                dis-hover
                padding="10"
                style="height: 100%; overflow: auto;">
            <Draggable item-key="id"
                       :clone="handlerClone"
                       :group="{ name: 'dimensions', pull: 'clone', put: false }"
                       :list="originalDimensions">
              <template #item="{ element }">
                <Tag size="medium">
                  {{ element.name }}
                </Tag>
              </template>
            </Draggable>
          </Card>
        </Content>
      </Sider>
      <Layout>
        <Content style="background-color: white;">
          <List :border="false"
                style="margin-left: 20px;">
            <ListItem>
              {{ $t('dataset.columnModeMetric') }}: &nbsp;
              <Draggable group="metrics"
                         item-key="id"
                         :list="metrics">
                <template #item="{ element }">
                  <Tag size="medium"
                       closable>
                    {{ element.name }}
                  </Tag>
                </template>
              </Draggable>
            </ListItem>
            <ListItem>
              {{ $t('dataset.columnModeDimension') }}: &nbsp;
              <Draggable group="dimensions"
                         item-key="id"
                         :list="dimensions">
                <template #item="{ element }">
                  <Tag size="medium"
                       closable>
                    {{ element.name }}
                  </Tag>
                </template>
              </Draggable>
            </ListItem>
            <ListItem>
              &nbsp;
            </ListItem>
          </List>
        </Content>
        <Layout>
          <Content style="background-color: white;">
            <div style="width: 100%; height:400px;">
              <CircularLoading v-if="loading"
                               :show="loading">
              </CircularLoading>
              <VisualEditor v-else
                            :configuration="configuration">
              </VisualEditor>
            </div>
          </Content>
          <Sider style="background-color: white; padding: 0 10px;">
            <RadioGroup v-model="configuration.type"
                        type="button"
                        size="large">
              <Row :gutter="10">
                <Radio label="TABLE">
                  <FontAwesomeIcon icon="table"
                                   size="2x">
                  </FontAwesomeIcon>
                </Radio>
              </Row>
            </RadioGroup>
          </Sider>
        </Layout>
      </Layout>
    </Layout>
  </div>
</template>

<script lang="ts">
import Draggable from 'vuedraggable'
import DatasetService from '@/services/admin/DatasetService'
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {Configuration} from "@/components/visual/Configuration";
import VisualEditor from "@/components/visual/VisualEditor.vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";

export default {
  name: 'DatasetAdhoc',
  components: {CircularLoading, VisualEditor, FontAwesomeIcon, Draggable},
  data()
  {
    return {
      loading: false,
      code: null,
      originalMetrics: [],
      originalDimensions: [],
      metrics: [],
      dimensions: [],
      configuration: null as Configuration
    }
  },
  watch: {
    metrics: {
      handler: 'handlerAdhoc',
      deep: true
    },
    dimensions: {
      handler: 'handlerAdhoc',
      deep: true
    },
  },
  created()
  {
    this.configuration = new Configuration()
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      setTimeout(() => {
        const code = this.$route.params.code
        this.code = code
        DatasetService.getColumnsByCode(code)
          .then(response => {
            if (response.status) {
              this.originalMetrics = response.data.filter((item: { mode: string; }) => item.mode === 'METRIC')
              this.originalDimensions = response.data.filter((item: { mode: string; }) => item.mode === 'DIMENSION')
            }
            else {
              this.$Message.error(response.message)
            }
          })
      }, 0)
    },
    handlerAdhoc()
    {
      this.loading = true
      const columns = []
      this.metrics.map((item: { id: any; }) => columns.push({id: item.id}))
      this.dimensions.map((item: { id: any; }) => columns.push({id: item.id}))
      const configure = {
        columns: columns
      }
      DatasetService.adhoc(this.code, configure)
        .then(response => {
          if (response.status) {
            this.configuration.headers = response.data.headers
            this.configuration.columns = response.data.columns
          }
          else {
            this.$Message.error(response.message)
          }
        })
        .finally(() => this.loading = false)
    },
    handlerClone(value: any)
    {
      return value
    }
  }
};
</script>
<style scoped>
:deep(.ivu-radio-group-button .ivu-radio-wrapper) {
  border: 0px solid #dcdee2;
  padding: 0 13px;
}

:deep(.ivu-radio-group-button .ivu-radio-wrapper:first-child) {
  border-left: 0px solid #dcdee2;
}

:deep(.ivu-radio-group-button .ivu-radio-wrapper-checked.ivu-radio-focus:first-child) {
  box-shadow: 0 0 0 0;
}

:deep(.ivu-radio-group-button .ivu-radio-wrapper-checked) {
  box-shadow: 0 0 0 0;
}

:deep(.ivu-radio-group-button .ivu-radio-wrapper:after) {
  background: transparent;
}

:deep(.ivu-radio-group-button .ivu-radio-wrapper:before) {
  background: transparent;
}
</style>
