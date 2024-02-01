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
      <FormItem prop="outerRadius"
                :label="$t('dataset.visualConfigureOuterRadius')">
        <Slider v-model="formState.outerRadius"
                :min="0.01"
                :max="10"
                step="0.1">
        </Slider>
      </FormItem>
    </Form>
  </div>
</template>
<script lang="ts">

export default {
  name: 'DatasetVisualConfigurePie',
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
        outerRadius: 0.8
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
