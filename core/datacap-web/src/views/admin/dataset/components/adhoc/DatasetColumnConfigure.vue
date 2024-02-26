<template>
  <div>
    <Modal v-model="visible"
           :title="title"
           :closable="false"
           :mask-closable="false"
           @onOk="handlerCommit()"
           @cancel="handlerCancel()">
      <Form :model="formState"
            :label-width="90">
        <div v-if="columnType === 'FILTER'">
          <FormItem :label="$t('common.expression')">
            <Select v-model="formState.expression">
              <Option :value="Expression.IS_NULL">{{ $t('dataset.columnExpressionIsNull') }}</Option>
              <Option :value="Expression.IS_NOT_NULL">{{ $t('dataset.columnExpressionIsNotNull') }}</Option>
            </Select>
          </FormItem>
          <FormItem>

          </FormItem>
        </div>
        <div v-else>
          <FormItem v-if="columnType === 'METRIC'"
                    :label="$t('common.expression')">
            <Select v-model="formState.expression">
              <Option v-if="formState.type === ColumnType.NUMBER"
                      :value="Expression.SUM">
                {{ $t('dataset.columnExpressionSum') }}
              </Option>
              <Option :value="Expression.COUNT">{{ $t('dataset.columnExpressionCount') }}</Option>
              <Option :value="Expression.MAX">{{ $t('dataset.columnExpressionMax') }}</Option>
              <Option :value="Expression.MIN">{{ $t('dataset.columnExpressionMin') }}</Option>
              <Option v-if="formState.type === ColumnType.NUMBER"
                      :value="Expression.AVG">
                {{ $t('dataset.columnExpressionAvg') }}
              </Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('common.alias')">
            <Input v-model="formState.alias"/>
          </FormItem>
          <FormItem :label="$t('common.sort')">
            <RadioGroup v-model="formState.order">
              <Radio label="">{{ $t('dataset.columnSortNone') }}</Radio>
              <Radio label="ASC">{{ $t('dataset.columnOrderAsc') }}</Radio>
              <Radio label="DESC">{{ $t('dataset.columnOrderDesc') }}</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem v-if="columnType === 'DIMENSION'"
                    :label="$t('dataset.customFunction')">
            <Input v-model="formState.function"/>
          </FormItem>
        </div>
      </Form>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, PropType} from 'vue'
import {ColumnType, Type} from "@/views/admin/dataset/Type";
import {Expression} from "@/views/admin/dataset/Expression";
import {cloneDeep} from "lodash";

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
    },
    ColumnType()
    {
      return ColumnType
    }
  },
  data()
  {
    return {
      title: null,
      formState: {
        id: null,
        type: null,
        alias: null,
        expression: null,
        order: null,
        function: null,
        value: null
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
      let prefix = `${this.$t('dataset.columnModeMetric')}`
      if (this.columnType === Type.DIMENSION) {
        prefix = `${this.$t('dataset.columnModeDimension')}`
      }
      else if (this.columnType === Type.FILTER) {
        prefix = `${this.$t('dataset.columnModeFilter')}`
      }
      this.title = `${prefix} [ ${this.content.aliasName ? this.content.aliasName : this.content.name} ] ${this.$t('common.configure')}`
      if (this.configure) {
        const cloneValue = cloneDeep(this.configure)
        this.formState = cloneValue
      }
      else {
        this.formState.id = this.content.id
      }
    },
    handlerCommit()
    {
      this.formState.mode = this.columnType
      this.$emit('commit', this.formState)
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
