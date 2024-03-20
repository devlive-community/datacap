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
            <EchartsPreview :key="referKey" :height="'500px'" :configure="chartOptions"/>
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
                  <div v-if="chartOptions">
                    <Panel name="xAxis">
                      {{ $t('common.xAxis') }}
                      <template #content>
                        <FormItem :label="$t('common.column')">
                          <Select v-model="defaultConfigure.xAxis" @on-change="handlerChangeValue('xAxis')">
                            <Option v-for="value of configure.headers" :value="value" v-bind:key="value">{{ value }}</Option>
                          </Select>
                        </FormItem>
                        <FormItem v-if="chartOptions.xAxis" :label="$t('common.type')">
                          <RadioGroup v-model="chartOptions.xAxis.type" type="button" size="small" @on-change="handlerChangeValue">
                            <Radio label="value">{{ $t('common.column') }}</Radio>
                            <Radio label="category">{{ $t('common.tag') }}</Radio>
                          </RadioGroup>
                        </FormItem>
                      </template>
                    </Panel>
                    <Panel v-if="chartOptions.xAxis && chartOptions.yAxis && !chartOptions.yAxis.disabled" disabled name="yAxis">
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
                  </div>
                  <div v-if="chartOptions">
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
                  </div>
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
import { defineComponent } from 'vue';
import { ChartConfigure } from '@/views/components/echarts/configure/ChartConfigure'
import { getValueByKey } from './DataUtils'
import { SeriesConfigure } from '@/views/components/echarts/configure/SeriesConfigure'
import { isEmpty } from 'lodash'
import { EchartsConfigure } from '@/views/components/echarts/EchartsConfigure'
import EchartsPreview from '@/views/components/echarts/EchartsPreview.vue'
import { AxisConfigure } from '@/views/components/echarts/configure/AxisConfigure'
import ReportService from '@/services/report'
import { ObjectUtils } from '@/utils/object'
import { ToastUtils } from '@/utils/toast'

export default defineComponent({
  name: 'EchartsEditor',
  components: {EchartsPreview},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    configure: {
      type: Object as () => EchartsConfigure,
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
      chartOptions: null as ChartConfigure | null,
      chartType: null,
      formState: {
        name: null as string | null,
        realtime: null,
        type: 'QUERY',
        configure: null as string | null,
        source: {id: null as number | null},
        query: null as string | null
      },
      loading: false
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.chartOptions = new ChartConfigure()
    },
    handlerChangeValue(type: string)
    {
      this.referKey = ObjectUtils.getTimestamp()

      switch (type) {
        case 'xAxis':
          if (this.chartOptions && this.configure) {
            this.chartOptions.xAxis = new AxisConfigure()
            this.chartOptions.xAxis.data = getValueByKey(this.defaultConfigure.xAxis, this.configure.columns as never[]) as never[]
            this.chartOptions.xAxis.meta.column = this.defaultConfigure.xAxis
            this.chartOptions.yAxis = new AxisConfigure()
            this.chartOptions.yAxis.type = 'value'
            this.chartOptions.yAxis.data = getValueByKey(this.defaultConfigure.yAxis, this.configure.columns as never[]) as never[]
            this.chartOptions.yAxis.disabled = true
            this.chartOptions.yAxis.meta.column = this.defaultConfigure.yAxis
          }
          break
        case 'Series': {
          if (this.chartOptions && this.configure) {
            const series: SeriesConfigure = new SeriesConfigure()
            series.data = getValueByKey(this.defaultConfigure.series, this.configure.columns as never[])
            series.type = this.chartType as any
            series.meta.column = this.defaultConfigure.series
            this.chartOptions.series = []
            this.chartOptions.series.push(series)
          }
          break;
        }
        case 'Type': {
          if (this.chartOptions) {
            const array = this.chartOptions.series as never[]
            if (array.length > 0) {
              (array as any[])[0].type = this.chartType
            }
          }
        }
      }
      this.handlerSetDefaultValue();
    },
    handlerSetDefaultValue()
    {
      if (this.chartOptions) {
        if (isEmpty(this.defaultConfigure.xAxis)) {
          this.chartOptions.xAxis = new AxisConfigure()
        }
        if (isEmpty(this.defaultConfigure.yAxis)) {
          this.chartOptions.yAxis = new AxisConfigure()
        }
      }
    },
    handlerPublish()
    {
      if (this.formState) {
        this.loading = true
        this.formState.configure = JSON.stringify(this.chartOptions)
        this.formState.source.id = this.sourceId as number
        this.formState.query = this.query as string
        ReportService.saveOrUpdate(this.formState)
            .then(response => {
              if (response.status) {
                ToastUtils.success(this.$t('report.publishSuccess').replace('REPLACE_NAME', this.formState.name as string))
                this.visible = false
              }
            })
            .finally(() => this.loading = false)
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
