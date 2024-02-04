<template>
  <div>
    <Form :model="formState"
          :label-width="60">
      <FormItem prop="xAxis"
                :label="$t('dataset.visualConfigureCategoryField')">
        <Select v-model="formState.xAxis">
          <Option v-for="item in columns"
                  :key="item"
                  :value="item">
            {{ item }}
          </Option>
        </Select>
      </FormItem>
      <FormItem prop="yAxis"
                :label="$t('dataset.visualConfigureValueField')">
        <Select v-model="formState.yAxis">
          <Option v-for="item in columns"
                  :key="item"
                  :value="item">
            {{ item }}
          </Option>
        </Select>
      </FormItem>
      <FormItem prop="series"
                :label="$t('dataset.visualConfigureSeriesField')">
        <Select v-model="formState.series">
          <Option v-for="item in columns"
                  :key="item"
                  :value="item">
            {{ item }}
          </Option>
        </Select>
      </FormItem>
      <FormItem prop="invalidType"
                :label="$t('dataset.visualConfigureDataBreakpoint')">
        <Select v-model="formState.invalidType">
          <Option key="break"
                  value="break">
            {{ $t('dataset.visualConfigureDataBreakpointBreak') }}
          </Option>
          <Option key="link"
                  value="link">
            {{ $t('dataset.visualConfigureDataBreakpointContinuous') }}
          </Option>
          <Option key="zero"
                  value="zero">
            {{ $t('dataset.visualConfigureDataBreakpointZero') }}
          </Option>
          <Option key="ignore"
                  value="ignore">
            {{ $t('dataset.visualConfigureDataBreakpointIgnore') }}
          </Option>
        </Select>
      </FormItem>
    </Form>
  </div>
</template>
<script lang="ts">

export default {
  name: 'DatasetVisualConfigureLine',
  props: {
    columns: {
      type: Array,
      default: () => []
    }
  },
  watch: {
    formState: {
      handler: 'handlerCommit',
      deep: true
    }
  },
  data()
  {
    return {
      formState: {
        xAxis: null,
        yAxis: null,
        series: null,
        invalidType: null
      }
    }
  },
  methods: {
    handlerCommit()
    {
      this.$emit('commit', this.formState)
    }
  }
};
</script>
