<template>
  <a-modal v-model:open="visible" :title="title" :footer="null" @cancel="onCancel">
    <a-form :model="formState" layout="vertical" @finish="onCommit">
      <div v-if="String(columnType) === 'FILTER'">
        <a-form-item name="expression" :label="$t('common.expression')">
          <a-select v-model:value="formState.expression" :placeholder="$t('dataset.tip.selectExpression')">
            <a-select-option :value="Expression.IS_NULL">{{ $t('dataset.common.columnExpressionIsNull') }}</a-select-option>
            <a-select-option :value="Expression.IS_NOT_NULL">{{ $t('dataset.common.columnExpressionIsNotNull') }}</a-select-option>
            <a-select-option :value="Expression.IS_LIKE">{{ $t('dataset.common.columnExpressionIsLike') }}</a-select-option>
            <a-select-option :value="Expression.IS_NOT_LIKE">{{ $t('dataset.common.columnExpressionIsNotLike') }}</a-select-option>
            <a-select-option :value="Expression.EQ">{{ $t('dataset.common.columnExpressionEquals') }}</a-select-option>
            <a-select-option :value="Expression.NE">{{ $t('dataset.common.columnExpressionNotEquals') }}</a-select-option>
            <a-select-option :value="Expression.GT">{{ $t('dataset.common.columnExpressionGreaterThan') }}</a-select-option>
            <a-select-option :value="Expression.GTE">{{ $t('dataset.common.columnExpressionGreaterThanOrEquals') }}</a-select-option>
            <a-select-option :value="Expression.LT">{{ $t('dataset.common.columnExpressionLessThan') }}</a-select-option>
            <a-select-option :value="Expression.LTE">{{ $t('dataset.common.columnExpressionLessThanOrEquals') }}</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item v-if="formState.expression && formState.expression !== Expression.IS_NULL && formState.expression !== Expression.IS_NOT_NULL"
                     name="value"
                     :label="$t('common.value')">
          <a-input v-model:value="formState.value"/>
        </a-form-item>
      </div>

      <div v-else>
        <a-space direction="vertical" :style="{ width: '100%' }">
          <a-form-item v-if="String(columnType) === 'METRIC'"
                       class="w-full"
                       name="expression"
                       :label="$t('common.expression')">
            <a-select v-model:value="formState.expression" :placeholder="$t('dataset.tip.selectExpression')" @change="onUpdateAlias">
              <a-select-option v-if="formState.type === ColumnType.NUMBER" :value="Expression.SUM">{{ $t('dataset.common.columnExpressionSum') }}</a-select-option>
              <a-select-option :value="Expression.COUNT">{{ $t('dataset.common.columnExpressionCount') }}</a-select-option>
              <a-select-option :value="Expression.MAX">{{ $t('dataset.common.columnExpressionMax') }}</a-select-option>
              <a-select-option :value="Expression.MIN">{{ $t('dataset.common.columnExpressionMin') }}</a-select-option>
              <a-select-option v-if="formState.type === ColumnType.NUMBER" :value="Expression.AVG">{{ $t('dataset.common.columnExpressionAvg') }}</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item class="w-full" name="alias" :label="$t('common.alias')">
            <a-input v-model:value="formState.alias"/>
          </a-form-item>

          <a-form-item class="w-full" name="order" :label="$t('common.sort')">
            <a-radio-group v-model:value="formState.order" @change="onChangeToggle">
              <a-radio-button value="">{{ $t('dataset.common.columnSortNone') }}</a-radio-button>
              <a-radio-button value="ASC">{{ $t('dataset.common.columnOrderAsc') }}</a-radio-button>
              <a-radio-button value="DESC">{{ $t('dataset.common.columnOrderDesc') }}</a-radio-button>
            </a-radio-group>
          </a-form-item>

          <a-form-item v-if="String(columnType) === 'DIMENSION'"
                       class="w-full"
                       name="function"
                       :label="$t('dataset.common.customFunction')">
            <a-input v-model:value="formState.function"/>
          </a-form-item>
        </a-space>
      </div>

      <div class="flex justify-end">
        <a-space>
          <a-button @click="onCancel">
            {{ $t('common.cancel') }}
          </a-button>

          <a-button type="primary" html-type="submit">
            {{ $t('common.save') }}
          </a-button>
        </a-space>
      </div>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { cloneDeep } from 'lodash'
import { ColumnType, Type } from '@/views/pages/admin/dataset/Type'
import { Expression } from '@/views/pages/admin/dataset/Expression'
import { Model } from '../../model/model'

defineOptions({ name: 'DatasetColumnConfigure' })

const props = withDefaults(defineProps<{
  isVisible?: boolean
  columnType?: Type | null
  content?: any
  configure?: any
}>(), {
  isVisible: false,
  columnType: () => Type.DIMENSION,
  content: () => null
})
const emit = defineEmits<{
  (e: 'close', value: boolean): void
  (e: 'commit', value: Model): void
}>()

const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const title = ref('')
const formState = ref<Model>({} as Model)

const prefixText = () => {
  let prefix = `${ t('dataset.common.columnModeMetric') }`
  if (props.columnType === Type.DIMENSION) {
    prefix = `${ t('dataset.common.columnModeDimension') }`
  }
  else if (props.columnType === Type.FILTER) {
    prefix = `${ t('dataset.common.columnModeFilter') }`
  }
  return prefix
}

const handleInitialize = () => {
  formState.value = {} as Model
  const prefix = prefixText()
  title.value = `${ prefix } [ ${ props.content.aliasName ? props.content.aliasName : props.content.name } ] ${ t('common.configure') }`
  if (props.configure) {
    formState.value = cloneDeep(props.configure) as Model
  }
  else {
    formState.value.id = props.content.id
  }
}

const onUpdateAlias = () => {
  const startWith = `${ prefixText() } [ ${ props.content.aliasName ? props.content.aliasName : props.content.name } ]`
  if (formState.value) {
    if (formState.value.expression === Expression.SUM) {
      formState.value.alias = `${ startWith } ${ t('dataset.common.columnExpressionSum') }`
    }
    else if (formState.value.expression === Expression.COUNT) {
      formState.value.alias = `${ startWith } ${ t('dataset.common.columnExpressionCount') }`
    }
    else if (formState.value.expression === Expression.MAX) {
      formState.value.alias = `${ startWith } ${ t('dataset.common.columnExpressionMax') }`
    }
    else if (formState.value.expression === Expression.MIN) {
      formState.value.alias = `${ startWith } ${ t('dataset.common.columnExpressionMin') }`
    }
    else if (formState.value.expression === Expression.AVG) {
      formState.value.alias = `${ startWith } ${ t('dataset.common.columnExpressionAvg') }`
    }
    else {
      formState.value.alias = startWith
    }
  }
}

const onChangeToggle = (event: any) => {
  formState.value.order = event?.target ? event.target.value : event
}

const onCancel = () => {
  visible.value = false
}

const onCommit = () => {
  if (formState.value) {
    formState.value.mode = String(props.columnType)
    emit('commit', formState.value)
    onCancel()
  }
}

handleInitialize()
</script>
