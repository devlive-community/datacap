<template>
  <span>{{ element?.aliasName ? element?.aliasName : element?.name }}</span>
  <span v-if="element?.expression">
      (
      <Text strong v-if="element.expression === Expression.SUM">
        {{ $t('dataset.common.columnExpressionSum') }}
      </Text>
      <Text strong v-else-if="element.expression === Expression.COUNT">
        {{ $t('dataset.common.columnExpressionCount') }}
      </Text>
      <Text strong v-else-if="element.expression === Expression.MAX">
        {{ $t('dataset.common.columnExpressionMax') }}
      </Text>
      <Text strong v-else-if="element.expression === Expression.MIN">
        {{ $t('dataset.common.columnExpressionMin') }}
      </Text>
      <Text strong v-else-if="element.expression === Expression.AVG">
        {{ $t('dataset.common.columnExpressionAvg') }}
      </Text>
      )
    </span>
</template>
<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { Expression } from '@/views/pages/admin/dataset/Expression'

interface ColumnMetric
{
  name: string
  expression: Expression
  aliasName: string
}

export default defineComponent({
  name: 'DatasetColumnMetric',
  props: {
    element: {
      type: Object as unknown as PropType<ColumnMetric>
    }
  },
  computed: {
    Expression()
    {
      return Expression
    }
  }
})
</script>
