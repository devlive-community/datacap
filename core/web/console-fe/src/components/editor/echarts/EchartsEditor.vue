<template>
  <div>
    <Drawer v-model="visible" title="Visualization" placement="right" :mask-closable="false"
            :width="'80%'" :transfer="false">
      <Split v-if="configure" v-model="editorSplit">
        <template #left>
          <div class="demo-split-pane">
            <EchartsPreview :key="referKey" :height="'500px'" :configure="chartConfigure"/>
          </div>
        </template>
        <template #right>
          <Form label-position="left" :label-width="50">
            <FormItem label="Type" style="margin-left: 10px;">
              <RadioGroup v-model="chartConfigure.type" @on-change="handlerChangeValue('series')">
                <Radio :label="chartType.LINE">Line</Radio>
                <Radio :label="chartType.BAR">Bar</Radio>
              </RadioGroup>
            </FormItem>
            <div class="demo-split-pane">
              <Collapse v-model="collapseValue">
                <Panel name="xAxis">
                  xAxis
                  <template #content>
                    <FormItem label="Value">
                      <Select v-model="defaultConfigure.xAxis" @on-change="handlerChangeValue('series')">
                        <Option v-for="value of configure.headers" :value="value" v-bind:key="value">{{ value }}</Option>
                      </Select>
                    </FormItem>
                    <FormItem label="Type">
                      <RadioGroup v-model="chartConfigure.xAxis.type" @on-change="handlerChangeValue">
                        <Radio label="value"></Radio>
                        <Radio label="category"></Radio>
                      </RadioGroup>
                    </FormItem>
                  </template>
                </Panel>
                <Panel name="yAxis">
                  yAxis
                  <template #content>
                    <FormItem label="Value">
                      <Select v-model="defaultConfigure.yAxis" @on-change="handlerChangeValue('y')">
                        <Option v-for="value of configure.headers" :value="value" v-bind:key="value">{{ value }}</Option>
                      </Select>
                    </FormItem>
                    <FormItem label="Type">
                      <RadioGroup v-model="chartConfigure.yAxis.type" @on-change="handlerChangeValue">
                        <Radio label="value"></Radio>
                        <Radio label="category"></Radio>
                      </RadioGroup>
                    </FormItem>
                  </template>
                </Panel>
                <Panel name="Series">
                  Series
                  <template #content>
                    <FormItem label="Value">
                      <Select v-model="defaultConfigure.series" @on-change="handlerChangeValue('series')">
                        <Option v-for="value of configure.headers" :value="value" v-bind:key="value">{{ value }}</Option>
                      </Select>
                    </FormItem>
                  </template>
                </Panel>
              </Collapse>
            </div>
          </Form>
        </template>
      </Split>
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
import {FormItem} from "view-ui-plus";
import {isEmpty} from "lodash";
import {EchartsConfigure} from "@/components/editor/echarts/EchartsConfigure";
import EchartsPreview from "@/components/editor/echarts/EchartsPreview.vue";
import {EchartsType} from "@/components/editor/echarts/EchartsType";

const chartType = EchartsType;

export default defineComponent({
  name: 'EchartsEditor',
  components: {EchartsPreview, FormItem},
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
      editorSplit: 0.7,
      collapseValue: 'xAxis',
      referKey: 0,
      defaultConfigure: {
        xAxis: '',
        yAxis: '',
        series: ''
      },
      chartConfigure: ChartConfigure,
      chartType: chartType
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.chartConfigure = new ChartConfigure();
    },
    handlerChangeValue(type: string)
    {
      this.referKey = getTimestamp();
      if (type === 'x') {
        this.chartConfigure.xAxis.data = getValueByKey(this.defaultConfigure.xAxis, this.configure.columns);
      }
      else if (type === 'y') {
        this.chartConfigure.yAxis.data = getValueByKey(this.defaultConfigure.yAxis, this.configure.columns);
      }
      else if (type === 'series') {
        const series: SeriesConfigure = new SeriesConfigure();
        series.data = getValueByKey(this.defaultConfigure.series, this.configure.columns);
        series.type = this.chartConfigure.type;
        this.chartConfigure.series = [];
        this.chartConfigure.series.push(series);
      }
      this.handlerSetDefaultValue();
    },
    handlerSetDefaultValue()
    {
      if (isEmpty(this.defaultConfigure.xAxis)) {
        this.chartConfigure.xAxis.data = null;
      }
      if (isEmpty(this.defaultConfigure.yAxis)) {
        this.chartConfigure.yAxis.data = null;
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
