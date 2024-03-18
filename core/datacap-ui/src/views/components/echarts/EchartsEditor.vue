<template>
  <div>
    <Modal v-model="visible"
           placement="right"
           :title="$t('common.visualization')"
           :mask-closable="false"
           :width="'90%'"
           :transfer="false">
      <Form :inline="true">
        <FormItem :label="$t('common.name')"
                  :label-width="80">
          <Input v-model="formState.name"/>
        </FormItem>
        <FormItem :label="$t('common.realtime')"
                  :label-width="80">
          <Switch v-model="formState.realtime"/>
        </FormItem>
      </Form>
      <Layout v-if="configure">
        <Layout>
          <Content>
            <EchartsPreview :key="referKey"
                            :height="'500px'"
                            :configure="chartOptions">
            </EchartsPreview>
          </Content>
          <Sider style="background-color: #FFFFFF;"
                 hide-trigger>
            <Form label-position="left"
                  :label-width="50">
              <Card padding="0"
                    dis-hover
                    :bordered="false">
                <template #title>
                  <RadioGroup v-model="chartType"
                              type="button"
                              @on-change="handlerChangeValue('Type')">
                    <Space>
                      <Radio label="line">
                        <FontAwesomeIcon icon="chart-line">
                        </FontAwesomeIcon>
                      </Radio>
                      <Radio label="bar">
                        <FontAwesomeIcon icon="chart-bar">
                        </FontAwesomeIcon>
                      </Radio>
                    </Space>
                  </RadioGroup>
                </template>
                <Collapse v-if="chartType"
                          v-model="collapseValue"
                          accordion>
                  <Panel name="xAxis">
                    {{ $t('common.xAxis') }}
                    <template #content>
                      <FormItem :label="$t('common.column')">
                        <Select v-model="defaultConfigure.xAxis" @on-change="handlerChangeValue('xAxis')">
                          <Option v-for="value of configure.headers" :value="value" v-bind:key="value">{{ value }}</Option>
                        </Select>
                      </FormItem>
                      <FormItem :label="$t('common.type')">
                        <RadioGroup v-model="chartOptions.xAxis.type" type="button" size="small" @on-change="handlerChangeValue">
                          <Radio label="value">{{ $t('common.column') }}</Radio>
                          <Radio label="category">{{ $t('common.tag') }}</Radio>
                        </RadioGroup>
                      </FormItem>
                    </template>
                  </Panel>
                  <Panel v-if="chartOptions.xAxis && !chartOptions.yAxis.disabled" disabled name="yAxis">
                    {{ $t('common.yAxis') }}
                    <template #content>
                      <FormItem label="Value">
                        <Select v-model="defaultConfigure.yAxis">
                          <Option v-for="value of configure.headers" :value="value" v-bind:key="value">{{ value }}</Option>
                        </Select>
                      </FormItem>
                      <FormItem label="Type">
                        <RadioGroup v-model="chartOptions.yAxis.type" @on-change="handlerChangeValue">
                          <Radio label="value"></Radio>
                          <Radio label="category"></Radio>
                        </RadioGroup>
                      </FormItem>
                    </template>
                  </Panel>
                  <Panel v-if="chartOptions.xAxis" name="Series">
                    {{ $t('common.data') }}
                    <template #content>
                      <FormItem :label="$t('common.column')">
                        <Select v-model="defaultConfigure.series" @on-change="handlerChangeValue('Series')">
                          <Option v-for="value of configure.headers" :value="value" v-bind:key="value">{{ value }}</Option>
                        </Select>
                      </FormItem>
                    </template>
                  </Panel>
                </Collapse>
              </Card>
            </Form>
          </Sider>
        </Layout>
      </Layout>
      <Result v-else
              type="warning">
        <template #desc>
        </template>
        <template #actions>
        </template>
      </Result>
      <template #footer>
        <Button type="primary"
                :loading="loading"
                @click="handlerPublish">
          {{ $t('common.publish') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {ChartConfigure} from "@/components/editor/echarts/configure/ChartConfigure";
import {getValueByKey} from "./DataUtils";
import {getTimestamp} from "@/common/DateCommon";
import {SeriesConfigure} from "@/components/editor/echarts/configure/SeriesConfigure";
import {isEmpty} from "lodash";
import {EchartsConfigure} from "@/components/editor/echarts/EchartsConfigure";
import EchartsPreview from "@/components/editor/echarts/EchartsPreview.vue";
import {AxisConfigure} from "@/components/editor/echarts/configure/AxisConfigure";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import ReportService from "@/services/admin/ReportService";

export default defineComponent({
  name: 'EchartsEditor',
  components: {FontAwesomeIcon, EchartsPreview},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    configure: {
      type: EchartsConfigure,
      default: () => null
    },
    sourceId: {
      type: Number
    },
    query: {
      type: String
    }
  },
  data()
  {
    return {
      collapseValue: 'xAxis',
      referKey: 0,
      defaultConfigure: {
        xAxis: '',
        yAxis: '',
        series: ''
      },
      chartOptions: null as ChartConfigure,
      chartType: null,
      formState: {name: null, realtime: null, type: 'QUERY', configure: null, source: {id: null}, query: null},
      loading: false
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.chartOptions = new ChartConfigure();
    },
    handlerChangeValue(type: string)
    {
      this.referKey = getTimestamp();

      switch (type) {
        case 'xAxis':
          this.chartOptions.xAxis = new AxisConfigure();
          this.chartOptions.xAxis.data = getValueByKey(this.defaultConfigure.xAxis, this.configure.columns);
          this.chartOptions.xAxis.meta.column = this.defaultConfigure.xAxis;
          this.chartOptions.yAxis = new AxisConfigure();
          this.chartOptions.yAxis.type = 'value';
          this.chartOptions.yAxis.data = getValueByKey(this.defaultConfigure.yAxis, this.configure.columns);
          this.chartOptions.yAxis.disabled = true;
          this.chartOptions.yAxis.meta.column = this.defaultConfigure.yAxis;
          break;
        case 'Series': {
          const series: SeriesConfigure = new SeriesConfigure();
          series.data = getValueByKey(this.defaultConfigure.series, this.configure.columns);
          series.type = this.chartType;
          series.meta.column = this.defaultConfigure.series;
          this.chartOptions.series = [];
          this.chartOptions.series.push(series);
          break;
        }
        case 'Type': {
          if (this.chartOptions.series) {
            this.chartOptions.series[0].type = this.chartType;
          }
        }
      }
      this.handlerSetDefaultValue();
    },
    handlerSetDefaultValue()
    {
      if (isEmpty(this.defaultConfigure.xAxis)) {
        this.chartOptions.xAxis.data = null;
      }
      if (isEmpty(this.defaultConfigure.yAxis)) {
        this.chartOptions.yAxis.data = null;
      }
    },
    handlerPublish()
    {
      this.loading = true;
      this.formState.configure = JSON.stringify(this.chartOptions);
      this.formState.source.id = this.sourceId;
      this.formState.query = this.query;
      ReportService.saveOrUpdate(this.formState)
        .then(response => {
          if (response.status) {
            this.$Message.success(this.$t('report.publishSuccess').replace('REPLACE_NAME', this.formState.name));
            this.visible = false;
          }
        })
        .finally(() => this.loading = false);
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible;
      },
      set(value: boolean)
      {
        this.$emit('close', value);
      }
    }
  }
});
</script>
<style scoped>
.ivu-drawer-body {
  padding: 0px;
}
</style>
