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
    </div>
  </div>
</template>
<script lang="ts">
import {Configuration} from "@/components/visual/Configuration";
import VisualTable from "@/components/visual/components/VisualTable.vue";

export default {
  name: 'VisualEditor',
  components: {VisualTable},
  props: {
    configuration: {
      type: Configuration
    }
  }
};
</script>
