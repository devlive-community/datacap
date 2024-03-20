<template>
  <div>
    <Alert v-if="configuration?.headers.length === 0 && !configuration?.message" class="mt-20">
      <AlertDescription>
        {{ $t('dataset.tip.adhocDnd') }}
      </AlertDescription>
    </Alert>
    <Alert v-else-if="configuration?.message" variant="destructive" class="mt-20">
      <AlertDescription>
        {{ configuration.message }}
      </AlertDescription>
    </Alert>
    <div v-else>
      <VisualTable v-if="configuration?.type === Type.TABLE" :configuration="configuration" @commitOptions="handlerCommit"/>
      <VisualLine v-else-if="configuration?.type === Type.LINE" :configuration="configuration" @commitOptions="handlerCommit"/>
      <VisualBar v-else-if="configuration?.type === Type.BAR" :configuration="configuration" @commitOptions="handlerCommit"/>
      <VisualArea v-else-if="configuration?.type === Type.AREA" :configuration="configuration" @commitOptions="handlerCommit"/>
      <VisualPie v-else-if="configuration?.type === Type.PIE" :configuration="configuration" @commitOptions="handlerCommit"/>
      <VisualHistogram v-else-if="configuration?.type === Type.HISTOGRAM" :configuration="configuration" @commitOptions="handlerCommit"/>
      <VisualWordCloud v-else-if="configuration?.type === Type.WORDCLOUD" :configuration="configuration" @commitOptions="handlerCommit"/>
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
import { Alert, AlertDescription } from '@/components/ui/alert'

export default defineComponent({
  name: 'VisualEditor',
  computed: {
    Type()
    {
      return Type
    }
  },
  components: {
    AlertDescription, Alert,
    VisualWordCloud, VisualHistogram, VisualPie, VisualArea, VisualBar, VisualLine, VisualTable
  },
  props: {
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
