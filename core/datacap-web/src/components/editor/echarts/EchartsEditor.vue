<template>
  <div>
    <Drawer v-model="visible" :title="$t('common.visualization')" placement="right" :mask-closable="false"
            :width="'90%'" :transfer="false">
      <Layout v-if="configure">
        <Layout>
          <Content>
            <EchartsPreview :key="referKey" :height="'500px'" :configure="chartOptions"/>
          </Content>
          <Sider style="background-color: #FFFFFF;" hide-trigger>
            <Form label-position="left" :label-width="50">
              <Card padding="0" shadow>
                <template #title>
                  <RadioGroup v-model="chartType" type="button" @on-change="handlerChangeValue('Type')">
                    <Space>
                      <Radio label="line">Line</Radio>
                      <Radio label="bar">Bar</Radio>
                    </Space>
                  </RadioGroup>
                </template>
                <Collapse v-if="chartType" v-model="collapseValue" accordion>
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
      <Result v-else type="warning">
        <template #desc>
        </template>
        <template #actions>
        </template>
      </Result>
    </Drawer>
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

export default defineComponent({
  name: 'EchartsEditor',
  components: {EchartsPreview},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    configure: {
      type: EchartsConfigure,
      default: () => null
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
      chartType: null
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
          this.chartOptions.yAxis = new AxisConfigure();
          this.chartOptions.yAxis.type = 'value';
          this.chartOptions.yAxis.disabled = true;
          break;
        case 'Series': {
          const series: SeriesConfigure = new SeriesConfigure();
          series.data = getValueByKey(this.defaultConfigure.series, this.configure.columns);
          series.type = this.chartType;
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
