<template>
  <div>
    <Alert>
      <Form inline>
        <div v-for="(item, index) in sortColumnItems" v-bind:key="index">
          <FormItem :label="$t('common.column')" prop="column">
            <Select v-model="item.column" size="small" style="min-width: 200px;">
              <Option v-for="column in columns" :value="column.title" v-bind:key="column.title">{{ column.title }}</Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('common.sort')" prop="sort">
            <Select v-model="item.sort" size="small" style="min-width: 150px;">
              <Option value="ASC">{{ $t('common.asc') }}</Option>
              <Option value="DESC">{{ $t('common.desc') }}</Option>
            </Select>
          </FormItem>
          <FormItem prop="action">
            <template #label>
              <Button @click="handlePlusItem(index)" type="primary" icon="md-add" size="small"/>&nbsp;
              <Button :disabled="sortColumnItems.length===1" @click="handleRemoveItem(index)" type="error" icon="md-remove" size="small"/>
            </template>
          </FormItem>
        </div>
        <FormItem>
          <Button :disabled="sortColumnItems.length === 0" size="small" type="primary" @click="handleApply('formInline')">{{ $t('common.apply') }}</Button>
        </FormItem>
      </Form>
    </Alert>
  </div>
</template>
<script>
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
      index: 0,
      sortColumnItems: [
        {
          column: null,
          sort: null
        }
      ]
    }
  },
  methods: {
    handlePlusItem() {
      this.index++;
      this.sortColumnItems.push({column: null, sort: null});
    },
    handleRemoveItem() {
      this.sortColumnItems.pop(this.index);
    },
    handleApply() {
      this.$emit('getValue', this.sortColumnItems);
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
