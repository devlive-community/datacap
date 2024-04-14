<template>
  <div class="flex h-full space-x-1 mt-2">
    <div class="left flex-1 justify-center">
      <CircularLoading v-if="loading" :show="loading"/>
      <div v-else>
        <Alert v-if="configuration?.headers.length === 0 && !configuration?.message" class="mt-20" :title="$t('dataset.tip.adhocDnd')"/>
        <Alert v-else-if="configuration?.message" class="mt-20" :title="configuration.message" type="error"/>
        <div v-else>
          <VisualTable v-if="configuration?.type === Type.TABLE" :configuration="configuration" @change="handlerCommit"/>
          <VisualLine v-else-if="configuration?.type === Type.LINE" :configuration="configuration" @change="handlerCommit"/>
          <VisualBar v-else-if="configuration?.type === Type.BAR" :configuration="configuration" @change="handlerCommit"/>
          <VisualArea v-else-if="configuration?.type === Type.AREA" :configuration="configuration" @change="handlerCommit"/>
          <VisualPie v-else-if="configuration?.type === Type.PIE" :configuration="configuration" @change="handlerCommit"/>
          <VisualHistogram v-else-if="configuration?.type === Type.HISTOGRAM" :configuration="configuration" @change="handlerCommit"/>
          <VisualWordCloud v-else-if="configuration?.type === Type.WORDCLOUD" :configuration="configuration" @change="handlerCommit"/>
        </div>
      </div>
    </div>
    <div class="right w-[210px] space-y-2">
      <Card body-class="p-2">
        <template #title>{{ $t('dataset.common.visualType') }}</template>
        <CircularLoading v-if="loading" :show="loading"/>
        <div v-else-if="configuration">
          <ToggleGroup v-model="configuration.type" type="single">
            <div class="grid grid-cols-4 items-center space-x-1 space-y-1">
              <ToggleGroupItem class="mt-1" :value="Type.TABLE">
                <Tooltip :content="$t('dataset.common.visualTypeTable')">
                  <Table/>
                </Tooltip>
              </ToggleGroupItem>
              <ToggleGroupItem :value="Type.LINE">
                <Tooltip :content="$t('dataset.common.visualTypeLine')">
                  <LineChart/>
                </Tooltip>
              </ToggleGroupItem>
              <ToggleGroupItem :value="Type.BAR">
                <Tooltip :content="$t('dataset.common.visualTypeBar')">
                  <BarChart4/>
                </Tooltip>
              </ToggleGroupItem>
              <ToggleGroupItem :value="Type.AREA">
                <Tooltip :content="$t('dataset.common.visualTypeArea')">
                  <AreaChart/>
                </Tooltip>
              </ToggleGroupItem>
              <ToggleGroupItem :value="Type.PIE">
                <Tooltip :content="$t('dataset.common.visualTypePie')">
                  <PieChart/>
                </Tooltip>
              </ToggleGroupItem>
              <ToggleGroupItem :value="Type.HISTOGRAM">
                <Tooltip :content="$t('dataset.common.visualTypeHistogram')">
                  <BarChartHorizontal/>
                </Tooltip>
              </ToggleGroupItem>
              <ToggleGroupItem :value="Type.WORDCLOUD">
                <Tooltip :content="$t('dataset.common.visualTypeWordCloud')">
                  <Baseline/>
                </Tooltip>
              </ToggleGroupItem>
            </div>
          </ToggleGroup>
        </div>
      </Card>
      <Card body-class="p-2">
        <template #title>{{ $t('dataset.common.visualConfigure') }}</template>
        <CircularLoading v-if="loading" :show="loading"/>
        <div v-else-if="configuration">
          <VisualLineConfigure v-if="configuration.type === Type.LINE" :configuration="configuration" @change="configuration.chartConfigure = $event"/>
          <VisualBarConfigure v-else-if="configuration.type === Type.BAR" :configuration="configuration" @change="configuration.chartConfigure = $event"/>
          <VisualAreaConfigure v-else-if="configuration.type === Type.AREA" :configuration="configuration" @change="configuration.chartConfigure = $event"/>
          <VisualPieConfigure v-else-if="configuration.type === Type.PIE" :configuration="configuration" @change="configuration.chartConfigure = $event"/>
          <VisualHistogramConfigure v-else-if="configuration.type === Type.HISTOGRAM" :configuration="configuration" @change="configuration.chartConfigure = $event"/>
          <VisualWordCloudConfigure v-else-if="configuration.type === Type.WORDCLOUD" :configuration="configuration" @change="configuration.chartConfigure = $event"/>
          <Alert v-else :title="$t('dataset.common.visualConfigureNotSpecified')"/>
        </div>
      </Card>
    </div>
  </div>
</template>
<script lang="ts">
import { Type } from '@/views/components/visual/Type'
import VisualWordCloud from '@/views/components/visual/components/VisualWordCloud.vue'
import VisualHistogram from '@/views/components/visual/components/VisualHistogram.vue'
import VisualPie from '@/views/components/visual/components/VisualPie.vue'
import VisualArea from '@/views/components/visual/components/VisualArea.vue'
import { Configuration } from './Configuration'
import VisualBar from '@/views/components/visual/components/VisualBar.vue'
import VisualLine from '@/views/components/visual/components/VisualLine.vue'
import VisualTable from '@/views/components/visual/components/VisualTable.vue'
import { defineComponent, PropType } from 'vue'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { AreaChart, BarChart4, BarChartHorizontal, Baseline, LineChart, PieChart, Table } from 'lucide-vue-next'
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group'
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group'
import Card from '@/views/ui/card'
import Tooltip from '@/views/ui/tooltip'
import Alert from '@/views/ui/alert'
import VisualLineConfigure from '@/views/components/visual/components/VisualLineConfigure.vue'
import VisualBarConfigure from '@/views/components/visual/components/VisualBarConfigure.vue'
import VisualAreaConfigure from '@/views/components/visual/components/VisualAreaConfigure.vue'
import VisualPieConfigure from '@/views/components/visual/components/VisualPieConfigure.vue'
import VisualHistogramConfigure from '@/views/components/visual/components/VisualHistogramConfigure.vue'
import VisualWordCloudConfigure from '@/views/components/visual/components/VisualWordCloudConfigure.vue'

export default defineComponent({
  name: 'VisualEditor',
  computed: {
    Type()
    {
      return Type
    }
  },
  components: {
    VisualWordCloudConfigure,
    VisualHistogramConfigure,
    VisualPieConfigure,
    VisualAreaConfigure,
    VisualBarConfigure,
    VisualLineConfigure,
    Card,
    Tooltip,
    RadioGroup, RadioGroupItem,
    ToggleGroup, ToggleGroupItem,
    Table, Baseline, BarChart4, LineChart, PieChart, AreaChart, BarChartHorizontal,
    CircularLoading,
    Alert,
    VisualWordCloud, VisualHistogram, VisualPie, VisualArea, VisualBar, VisualLine, VisualTable
  },
  props: {
    loading: {
      type: Boolean,
      default: false
    },
    configuration: {
      type: Object as PropType<Configuration | null>
    }
  },
  methods: {
    handlerCommit(value: any)
    {
      this.$emit('commitOptions', value)
    }
  }
})
</script>
