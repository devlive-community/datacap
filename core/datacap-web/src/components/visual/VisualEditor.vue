<template>
  <div>
    <Result v-if="configuration.headers.length === 0"
            type="warning">
      <template #desc>
        {{ $t('dataset.adhocDndTip') }}
      </template>
    </Result>
    <div v-else>
      <VisualTable v-if="configuration.type === Type.TABLE"
                   :configuration="configuration">
      </VisualTable>
      <VisualLine v-else-if="configuration.type === Type.LINE"
                  :configuration="configuration">
      </VisualLine>
    </div>
  </div>
</template>
<script lang="ts">
import {Configuration} from "@/components/visual/Configuration";
import VisualTable from "@/components/visual/components/VisualTable.vue";
import VisualLine from "@/components/visual/components/VisualLine.vue";
import {Type} from "@/components/visual/Type";

export default {
  name: 'VisualEditor',
  computed: {
    Type()
    {
      return Type
    }
  },
  components: {VisualLine, VisualTable},
  props: {
    configuration: {
      type: Configuration
    }
  }
};
</script>
