<template>
  <div>
    <Result v-if="configuration.headers.length === 0 && !configuration.message"
            type="warning">
      <template #desc>
        {{ $t('dataset.adhocDndTip') }}
      </template>
    </Result>
    <Result v-else-if="configuration.message"
            type="error">
      <template #desc>
        {{ configuration.message }}
      </template>
    </Result>
    <div v-else>
      <VisualTable v-if="configuration.type === Type.TABLE"
                   :configuration="configuration"
                   @commitOptions="handlerCommit">
      </VisualTable>
      <VisualLine v-else-if="configuration.type === Type.LINE"
                  :configuration="configuration"
                  @commitOptions="handlerCommit">
      </VisualLine>
      <VisualBar v-else-if="configuration.type === Type.BAR"
                 :configuration="configuration"
                 @commitOptions="handlerCommit">
      </VisualBar>
      <VisualArea v-else-if="configuration.type === Type.AREA"
                  :configuration="configuration"
                  @commitOptions="handlerCommit">
      </VisualArea>
      <VisualPie v-else-if="configuration.type === Type.PIE"
                 :configuration="configuration"
                 @commitOptions="handlerCommit">
      </VisualPie>
      <VisualHistogram v-else-if="configuration.type === Type.HISTOGRAM"
                       :configuration="configuration"
                       @commitOptions="handlerCommit">
      </VisualHistogram>
      <VisualWordCloud v-else-if="configuration.type === Type.WORDCLOUD"
                       :configuration="configuration"
                       @commitOptions="handlerCommit">
      </VisualWordCloud>
    </div>
  </div>
</template>
<script lang="ts">
import {Configuration} from "@/components/visual/Configuration";
import VisualTable from "@/components/visual/components/VisualTable.vue";
import VisualLine from "@/components/visual/components/VisualLine.vue";
import {Type} from "@/components/visual/Type";
import VisualBar from "@/components/visual/components/VisualBar.vue";
import VisualArea from "@/components/visual/components/VisualArea.vue";
import VisualPie from "@/components/visual/components/VisualPie.vue";
import VisualHistogram from "@/components/visual/components/VisualHistogram.vue";
import VisualWordCloud from "@/components/visual/components/VisualWordCloud.vue";

export default {
  name: 'VisualEditor',
  computed: {
    Type()
    {
      return Type
    }
  },
  components: {VisualWordCloud, VisualHistogram, VisualPie, VisualArea, VisualBar, VisualLine, VisualTable},
  props: {
    configuration: {
      type: Configuration
    }
  },
  methods: {
    handlerCommit(value: any)
    {
      this.$emit('commitOptions', value)
    }
  }
};
</script>
