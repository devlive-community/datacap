<template>
  <div>
    <Timeline>
      <TimelineItem v-for="state in states"
                    :key="state">
        <div v-if="state === 'START'">
          <p class="time">
            {{ $t('dataset.stateOfStarted') }}
            <span class="content">{{ $t('dataset.complete') }}</span>
          </p>
        </div>
        <div v-else-if="state.startsWith('METADATA_')">
          <p class="time">
            {{ $t('dataset.stateOfMetadata') }}
            <span v-if="state.endsWith('SUCCESS')"
                  class="content">
              {{ $t('dataset.complete') }}
            </span>
            <span v-else-if="state.endsWith('FAILED')"
                  class="content">
              {{ $t('dataset.failed') }}
            </span>
          </p>
        </div>
        <div v-else-if="state.startsWith('TABLE_')">
          <p class="time">
            {{ $t('dataset.stateOfCreateTable') }}
            <span v-if="state.endsWith('SUCCESS')"
                  class="content">
              {{ $t('dataset.complete') }}
            </span>
            <span v-else-if="state.endsWith('FAILED')"
                  class="content">
              {{ $t('dataset.failed') }}
            </span>
          </p>
        </div>
      </TimelineItem>
    </Timeline>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';

export default defineComponent({
  name: 'DatasetState',
  props: {
    states: {
      type: Array
    }
  }
});
</script>

<style scoped>
.time {
  font-size: 14px;
}

.content {
  margin-left: 30px;
  font-weight: bold;
}
</style>
