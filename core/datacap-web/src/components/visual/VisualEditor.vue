<template>
  <div>
    <Result type="warning">
      <template #desc>
        {{ $t('dataset.adhocDndTip') }}
      </template>
    </Result>
  </div>
</template>
<script lang="ts">
import {Configuration} from "@/components/visual/Configuration";

export default {
  name: 'VisualEditor',
  props: {
    configuration: {
      type: Configuration
    }
  }
};
</script>
