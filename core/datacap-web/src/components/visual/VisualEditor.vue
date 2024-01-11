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
    </div>
  </div>
</template>
<script lang="ts">
import {Configuration} from "@/components/visual/Configuration";
import VisualTable from "@/components/visual/components/VisualTable.vue";
import VisualLine from "@/components/visual/components/VisualLine.vue";
import {Type} from "@/components/visual/Type";
import VisualBar from "@/components/visual/components/VisualBar.vue";

export default {
  name: 'VisualEditor',
  computed: {
    Type()
    {
      return Type
    }
  },
  components: {VisualBar, VisualLine, VisualTable},
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
