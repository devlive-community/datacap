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
      </Form>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, PropType} from 'vue'
import {ColumnType, Type} from "@/views/admin/dataset/Type";
import {Expression} from "@/views/admin/dataset/Expression";
import {FormItem} from "view-ui-plus";

export default defineComponent({
  name: 'DatasetColumnConfigure',
  components: {FormItem},
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
      this.title = `${this.columnType === Type.METRIC ? this.$t('dataset.columnModeMetric') : this.$t('dataset.columnModeDimension')} [ ${this.content.aliasName ?
        this.content.aliasName :
        this.content.name} ] ${this.$t(
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
