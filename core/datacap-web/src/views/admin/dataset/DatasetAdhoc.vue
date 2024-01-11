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
                <Tag size="medium"
                     class="point">
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
                <Tag size="medium"
                     class="point">
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
                <template #item="{ element, index }">
                  <Tag size="medium">
                    <DatasetColumnMetric :element="element"/> &nbsp;
                    <Tooltip transfer
                             class="point"
                             :content="$t('common.configure')">
                      <FontAwesomeIcon icon="gear"
                                       class="point"
                                       @click="handlerColumnConfigure(true, element, ColumnType.METRIC)">
                      </FontAwesomeIcon>
                    </Tooltip>&nbsp;
                    <Tooltip transfer
                             :content="$t('common.remove')">
                      <FontAwesomeIcon icon="trash"
                                       class="point"
                                       @click="handlerRemove(element.id, index, metrics)">
                      </FontAwesomeIcon>
                    </Tooltip>
                  </Tag>
                </template>
              </Draggable>
            </ListItem>
            <ListItem>
              {{ $t('dataset.columnModeDimension') }}: &nbsp;
              <Draggable group="dimensions"
                         item-key="id"
                         :list="dimensions">
                <template #item="{ element, index}">
                  <Tag size="medium">
                    {{ element.name }} &nbsp;
                    <Tooltip transfer
                             :content="$t('common.remove')">
                      <FontAwesomeIcon icon="trash"
                                       class="point"
                                       @click="handlerRemove(element.id, index, dimensions)">
                      </FontAwesomeIcon>
                    </Tooltip>
                  </Tag>
                </template>
              </Draggable>
            </ListItem>
            <ListItem style="padding: 10px;">
              <ListItemMeta>&nbsp;</ListItemMeta>
              <template #action>
                <Space>
                  <Button type="primary"
                          icon="md-arrow-dropright-circle"
                          shape="circle"
                          :loading="loading"
                          @click="handlerAdhoc">
                  </Button>
                  <Button icon="md-eye"
                          shape="circle"
                          :disabled="!showSql.content"
                          @click="handlerShowSql(true)">
                  </Button>
                  <Button type="primary"
                          :disabled="!isPublish"
                          @click="formState.visible = true">
                    {{ $t('common.publish') }}
                  </Button>
                </Space>
              </template>
            </ListItem>
            <ListItem style="padding: 0">&nbsp;</ListItem>
          </List>
        </Content>
        <Layout>
          <Content style="background-color: white;">
            <div style="width: 100%; height:400px;">
              <CircularLoading v-if="loading"
                               :show="loading">
              </CircularLoading>
              <VisualEditor v-else
                            :configuration="configuration"
                            @commitOptions="handlerCommitOptions">
              </VisualEditor>
            </div>
          </Content>
          <Sider style="background-color: white; padding: 0 10px;">
            <Divider orientation="left"
                     style="margin: 10px 0;">
              {{ $t('dataset.visualType') }}
            </Divider>
            <RadioGroup v-model="configuration.type"
                        type="button"
                        size="large">
              <Row :gutter="10">
                <Radio :label="Type.TABLE">
                  <Tooltip transfer
                           :content="$t('dataset.visualTypeTable')">
                    <FontAwesomeIcon icon="table"
                                     size="2x">
                    </FontAwesomeIcon>
                  </Tooltip>
                </Radio>
                <Radio :label="Type.LINE">
                  <Tooltip transfer
                           :content="$t('dataset.visualTypeLine')">
                    <FontAwesomeIcon icon="line-chart"
                                     size="2x">
                    </FontAwesomeIcon>
                  </Tooltip>
                </Radio>
                <Radio :label="Type.BAR">
                  <Tooltip transfer
                           :content="$t('dataset.visualTypeBar')">
                    <FontAwesomeIcon icon="bar-chart"
                                     size="2x">
                    </FontAwesomeIcon>
                  </Tooltip>
                </Radio>
              </Row>
            </RadioGroup>
            <Divider orientation="left"
                     style="margin: 10px 0;">
              {{ $t('dataset.visualConfigure') }}
            </Divider>
            <DatasetVisualConfigureLine v-if="configuration.type === Type.LINE"
                                        :columns="configuration.headers"
                                        @commit="handlerCommit">
            </DatasetVisualConfigureLine>
            <DatasetVisualConfigureBar v-else-if="configuration.type === Type.BAR"
                                       :columns="configuration.headers"
                                       @commit="handlerCommit">
            </DatasetVisualConfigureBar>
            <Result v-else>
              <template #desc>
                {{ $t('dataset.visualConfigureNotSpecified') }}
              </template>
            </Result>
          </Sider>
        </Layout>
      </Layout>
    </Layout>
    <SqlDetail v-if="showSql.visible"
               :is-visible="showSql.visible"
               :content="showSql.content"
               @close="handlerShowSql(false)">
    </SqlDetail>
    <DatasetColumnConfigure v-if="columnContent.visible"
                            :is-visible="columnContent.visible"
                            :column-type="columnContent.type"
                            :content="columnContent.content"
                            :configure="columnContent.configure"
                            @close="handlerColumnConfigure(false, null, null)"
                            @commit="handlerCommitColumnConfigure">
    </DatasetColumnConfigure>
    <Modal v-model="formState.visible"
           :title="$t('common.configure')">
      <Form :model="formState"
            :label-width="80">
        <FormItem :label="$t('common.name')"
                  prop="name">
          <Input v-model="formState.name"/>
        </FormItem>
      </Form>
      <template #footer>
        <Button @click="formState.visible = false">
          {{ $t('common.cancel') }}
        </Button>
        <Button type="primary"
                :disabled="!formState.name"
                @click="handlerPublish">
          {{ $t('common.publish') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>

<script lang="ts">
import Draggable from 'vuedraggable'
import DatasetService from '@/services/admin/DatasetService'
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {Configuration} from "@/components/visual/Configuration";
import VisualEditor from "@/components/visual/VisualEditor.vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import DatasetVisualConfigureLine from "@/views/admin/dataset/components/adhoc/DatasetVisualConfigureLine.vue";
import {Type} from "@/components/visual/Type";
import {Type as ColumnType} from './Type';
import DatasetVisualConfigureBar from "@/views/admin/dataset/components/adhoc/DatasetVisualConfigureBar.vue";
import SqlDetail from "@/components/sql/SqlDetail.vue";
import DatasetColumnConfigure from "@/views/admin/dataset/components/adhoc/DatasetColumnConfigure.vue";
import DatasetColumnMetric from "@/views/admin/dataset/components/adhoc/DatasetColumnMetric.vue";
import ReportService from "@/services/admin/ReportService";

export default {
  name: 'DatasetAdhoc',
  computed: {
    Type()
    {
      return Type
    },
    ColumnType()
    {
      return ColumnType
    }
  },
  components: {
    DatasetColumnMetric,
    DatasetColumnConfigure, SqlDetail, DatasetVisualConfigureBar, DatasetVisualConfigureLine, CircularLoading, VisualEditor, FontAwesomeIcon, Draggable
  },
  data()
  {
    return {
      loading: false,
      code: null,
      originalMetrics: [],
      originalDimensions: [],
      metrics: [],
      dimensions: [],
      configure: {
        columns: []
      },
      configuration: null as Configuration,
      showSql: {
        visible: false,
        content: null
      },
      columnContent: {
        visible: false,
        type: null as ColumnType,
        content: null,
        configure: null
      },
      isPublish: false,
      commitOptions: null,
      formState: {
        visible: false,
        name: null
      },
      published: false
    }
  },
  watch: {
    metrics: {
      handler: 'handlerApplyAdhoc',
      deep: true
    },
    dimensions: {
      handler: 'handlerApplyAdhoc',
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
    handlerApplyAdhoc()
    {
      const columns = []
      this.metrics
        .filter((value: { id: unknown; }) => this.configure.columns.findIndex((item: { id: unknown }) => item.id === value.id) === -1)
        .map((item: { id: unknown; type: unknown; mode: unknown; }) => columns.push({id: item.id, type: item.type, mode: item.mode}))
      this.dimensions
        .filter((value: { id: unknown; }) => this.configure.columns.findIndex((item: { id: unknown }) => item.id === value.id) === -1)
        .map((item: { id: unknown; type: unknown; mode: unknown; }) => columns.push({id: item.id, type: item.type, mode: item.mode}))
      if (this.configure.columns.length > 0) {
        this.configure.columns = [...this.configure.columns, ...columns]
      }
      else {
        this.configure.columns = columns
      }
      this.handlerAdhoc()
    },
    handlerAdhoc()
    {
      this.isPublish = true
      this.loading = true
      DatasetService.adhoc(this.code, this.configure)
        .then(response => {
          if (response.status) {
            if (response.data.isSuccessful) {
              this.configuration.headers = response.data.headers
              this.configuration.columns = response.data.columns
              this.showSql.content = response.data.content
              this.configuration.message = null
            }
            else {
              this.configuration.headers = []
              this.configuration.columns = []
              this.configuration.message = response.data.message
            }
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
    },
    handlerRemove(id: number, index: number, array: [])
    {
      array.splice(index, 1)
      this.configure.columns = this.configure.columns.filter((item: { id: number; }) => item.id !== id)
      this.handlerAdhoc()
    },
    handlerCommit(value: any)
    {
      this.configuration.chartConfigure = value
    },
    handlerShowSql(opened: boolean)
    {
      this.showSql.visible = opened
    },
    handlerColumnConfigure(opened: boolean, record: any, type: ColumnType)
    {
      this.columnContent.visible = opened
      this.columnContent.type = type
      this.columnContent.content = record
      if (record) {
        const foundIndex = this.configure.columns.findIndex((item: { id: unknown }) => item.id === record.id)
        if (foundIndex !== -1) {
          this.columnContent.configure = this.configure.columns[foundIndex]
        }
      }
      else {
        this.columnContent.configure = null
      }
    },
    handlerCommitColumnConfigure(value: any)
    {
      const foundIndex = this.configure.columns.findIndex((item: { id: unknown }) => item.id === value.id)
      if (foundIndex !== -1) {
        this.configure.columns.splice(foundIndex, 1, value)
        this.metrics.find((item: { id: any; }) => item.id === value.id).expression = value.expression
        this.handlerAdhoc()
      }
    },
    handlerCommitOptions(value: any)
    {
      this.commitOptions = value
    },
    handlerPublish()
    {
      this.published = true
      const configure = {
        name: this.formState.name,
        realtime: true,
        type: 'DATASET',
        configure: JSON.stringify(this.commitOptions),
        dataset: {
          id: this.originalDimensions[0].dataset.id
        },
        query: JSON.stringify(this.configure)
      }
      ReportService.saveOrUpdate(configure)
        .then(response => {
          if (response.status) {
            this.$Message.success(this.$t('report.publishSuccess').replace('REPLACE_NAME', this.formState.name))
            this.formState.visible = false
          }
        })
        .finally(() => this.published = false)
    }
  }
}
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

.point {
  cursor: pointer;
}
</style>
