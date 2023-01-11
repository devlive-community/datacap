<template>
  <div>
    <Result type="error">
      <template #desc>
        {{ $t('alert.currentSourceNotSupportOperator') }}
      </template>
      <template #extra>
        <div>{{ $t('alert.currentSourceFixedTooltip') }}:</div>
        <div>
          <Icon type="md-bulb" color="blue"/>
          {{ $t('alert.currentSourceFixedTooltipStep1') }}
          <a class="ivu-ml-16" target="_blank" href="https://github.com/EdurtIO/incubator-datacap/issues/new/choose">
            Go
            <Icon type="ios-arrow-forward"/>
          </a>
        </div>
        <div>
          <Icon type="md-bulb" color="blue"/>
          {{ $t('alert.currentSourceFixedTooltipStep2') }}
          <a class="ivu-ml-16">
            <router-link to="/admin/template/sql">
              Go
              <Icon type="ios-arrow-forward"/>
            </router-link>
          </a>
        </div>
        <div>
          <Icon type="md-bulb" color="blue"/>
          {{ $t('alert.currentTemplateName') }}
          <a class="ivu-ml-16">
            <router-link to="/admin/template/sql">
              Go
              <Icon type="ios-arrow-forward"/>
            </router-link>
          </a>
          <p v-for="name in templateName" v-bind:key="name" style="margin-left: 20px;">{{ name }}</p>
        </div>
      </template>
    </Result>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "SourceNotSupported",
  props: {
    templateName: {
      type: Array,
      default: () => []
    }
  }
});
</script>
