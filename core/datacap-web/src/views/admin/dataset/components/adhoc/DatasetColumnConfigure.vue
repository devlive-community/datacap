<template>
  <div>
    <Modal v-model="visible"
           :title="title"
           :closable="false"
           :mask-closable="false"
           @onOk="handlerCommit()"
           @cancel="handlerCancel()">
      <Form :model="formState"
            :label-width="80">
        <FormItem :label="$t('common.expression')">
          <Select v-model="formState.expression">
            <Option :value="Expression.SUM">{{ $t('dataset.columnExpressionSum') }}</Option>
            <Option :value="Expression.COUNT">{{ $t('dataset.columnExpressionCount') }}</Option>
            <Option :value="Expression.MAX">{{ $t('dataset.columnExpressionMax') }}</Option>
            <Option :value="Expression.MIN">{{ $t('dataset.columnExpressionMin') }}</Option>
            <Option :value="Expression.AVG">{{ $t('dataset.columnExpressionAvg') }}</Option>
          </Select>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, PropType} from 'vue'
import {Type} from "@/views/admin/dataset/Type";
import {Expression} from "@/views/admin/dataset/Expression";

export default defineComponent({
  name: 'DatasetColumnConfigure',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    columnType: {
      type: String as unknown as PropType<Type>,
      default: () => Type.DIMENSION
    },
    content: {
      type: Object,
      default: () => null
    },
    configure: {
      type: Object
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible;
      },
      set(value: boolean)
      {
        this.$emit('close', value);
      }
    },
    Expression()
    {
      return Expression
    }
  },
  data()
  {
    return {
      title: null,
      formState: {
        id: null,
        expression: null
      }
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.title = `${this.columnType === Type.METRIC ? this.$t('dataset.columnModeMetric') : this.$t('dataset.columnModeDimension')} [ ${this.content.name} ] ${this.$t(
        'common.configure')}`
      if (this.configure) {
        this.formState = this.configure
      }
      else {
        this.formState.id = this.content.id
      }
    },
    handlerCommit()
    {
      this.$emit('commit', this.formState)
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
