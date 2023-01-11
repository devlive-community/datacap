<template>
  <div>
    <Alert>
      <Form :model="formInline" inline>
        <FormItem :label="$t('common.column')" prop="column">
          <Select v-model="formInline.column" size="small" style="min-width: 200px;">
            <Option v-for="column in columns" :value="column.title" v-bind:key="column.title">{{ column.title }}</Option>
          </Select>
        </FormItem>
        <FormItem :label="$t('common.sort')" prop="sort">
          <Select v-model="formInline.sort" size="small" style="min-width: 150px;">
            <Option value="ASC">{{ $t('common.asc') }}</Option>
            <Option value="DESC">{{ $t('common.desc') }}</Option>
          </Select>
        </FormItem>
        <FormItem label="&nbsp;">
          <Button :disabled="!(formInline.column && formInline.sort)" size="small" type="primary" @click="handleApply('formInline')">{{ $t('common.apply') }}</Button>
        </FormItem>
      </Form>
    </Alert>
  </div>
</template>
<script>
import {Sort} from "@/model/sql/Sort";

export default {
  name: 'SortByComponent',
  props: {
    columns: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      formInline: {
        column: null,
        sort: null
      }
    }
  },
  methods: {
    handleApply() {
      this.$emit('getValue', this.formInline);
    }
  }
}
</script>
<style scoped>
.ivu-form-item {
  margin-bottom: 0;
}

.ivu-alert {
  margin-bottom: 0;
  padding: 0;
}

.ivu-alert-info {
  border: 0px solid #abdcff;
  background-color: transparent;
}
</style>
