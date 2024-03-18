<template>
  <div v-for="state in states" :key="state">
    <Alert class="flex items-center" v-if="state === 'START'">
      <AlertTitle class="flex-grow">
        {{ $t('dataset.common.stateOfStarted') }}
      </AlertTitle>
      <AlertDescription class="ml-4">
        {{ $t('dataset.complete') }}
      </AlertDescription>
    </Alert>
    <Alert class="flex items-center mt-2" v-else-if="state.startsWith('METADATA_')">
      <AlertTitle class="flex-grow">
        {{ $t('dataset.common.stateOfMetadata') }}
      </AlertTitle>
      <AlertDescription class="ml-4">
        <span v-if="state.endsWith('SUCCESS')" class="content">
          {{ $t('dataset.common.complete') }}
        </span>
        <span v-else-if="state.endsWith('FAILED')" class="content">
          {{ $t('dataset.common.failed') }}
        </span>
      </AlertDescription>
    </Alert>
    <Alert class="flex items-center mt-2" v-else-if="state.startsWith('TABLE_')">
      <AlertTitle class="flex-grow">
        {{ $t('dataset.common.stateOfCreateTable') }}
      </AlertTitle>
      <AlertDescription class="ml-4">
        <span v-if="state.endsWith('SUCCESS')" class="content">
          {{ $t('dataset.common.complete') }}
        </span>
        <span v-else-if="state.endsWith('FAILED')" class="content">
          {{ $t('dataset.common.failed') }}
        </span>
      </AlertDescription>
    </Alert>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Alert, AlertTitle } from '@/components/ui/alert'

export default defineComponent({
  name: 'DatasetState',
  components: {AlertTitle, Alert},
  props: {
    states: {
      type: Array
    }
  }
});
</script>
