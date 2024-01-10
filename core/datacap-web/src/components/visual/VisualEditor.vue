<template>
  <div>
    <Result v-if="configuration.headers.length === 0"
            type="warning">
      <template #desc>
        {{ $t('dataset.adhocDndTip') }}
      </template>
    </Result>
    <div v-else>
      <VisualTable v-if="configuration.type === 'TABLE'"
                   :configuration="configuration">
      </VisualTable>
      <VisualLine v-else-if="configuration.type === 'LINE'"
                  :configuration="configuration">
      </VisualLine>
    </div>
  </div>
</template>
<script lang="ts">
import {Configuration} from "@/components/visual/Configuration";
import VisualTable from "@/components/visual/components/VisualTable.vue";
import VisualLine from "@/components/visual/components/VisualLine.vue";

export default {
  name: 'VisualEditor',
  components: {VisualLine, VisualTable},
  props: {
    configuration: {
      type: Configuration
    }
  }
};
</script>
