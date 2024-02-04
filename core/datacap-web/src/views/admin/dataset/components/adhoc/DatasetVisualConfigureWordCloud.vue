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
    </Form>
  </div>
</template>
<script lang="ts">

export default {
  name: 'DatasetVisualConfigureWordCloud',
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
        series: null
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
