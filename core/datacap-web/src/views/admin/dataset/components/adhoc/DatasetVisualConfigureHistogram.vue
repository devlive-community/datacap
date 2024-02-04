<template>
  <div>
    <Form :model="formState"
          :label-width="60">
      <FormItem prop="xAxis"
                :label="$t('dataset.visualConfigureCategoryLeftField')">
        <Select v-model="formState.xAxis">
          <Option v-for="item in columns"
                  :key="item"
                  :value="item">
            {{ item }}
          </Option>
        </Select>
      </FormItem>
      <FormItem prop="x2Axis"
                :label="$t('dataset.visualConfigureCategoryRightField')">
        <Select v-model="formState.x2Axis">
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
    </Form>
  </div>
</template>
<script lang="ts">

export default {
  name: 'DatasetVisualConfigureHistogram',
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
        x2Axis: null,
        yAxis: null
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
