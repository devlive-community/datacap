<template>
  <span>{{ element.name }}</span>
  <span v-if="element.expression">
      (
      <Text strong
            v-if="element.expression === Expression.SUM">
        {{ $t('dataset.columnExpressionSum') }}
      </Text>
      <Text strong
            v-else-if="element.expression === Expression.COUNT">
        {{ $t('dataset.columnExpressionCount') }}
      </Text>
      <Text strong
            v-else-if="element.expression === Expression.MAX">
        {{ $t('dataset.columnExpressionMax') }}
      </Text>
      <Text strong
            v-else-if="element.expression === Expression.MIN">
        {{ $t('dataset.columnExpressionMin') }}
      </Text>
      <Text strong
            v-else-if="element.expression === Expression.AVG">
        {{ $t('dataset.columnExpressionAvg') }}
      </Text>
      )
    </span>
</template>
<script lang="ts">
import {defineComponent, PropType} from 'vue'
import {Expression} from "@/views/admin/dataset/Expression";

interface ColumnMetric
{
  name: string
  expression: Expression
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
