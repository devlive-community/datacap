<template>
  <div class="dc-table">
    <!-- 顶部筛选区：各页面自定义（关键词 / 状态 / 归属等 + 查询 / 重置） -->
    <div v-if="$slots.filter" class="dc-table__filter">
      <slot name="filter"/>
    </div>

    <!-- 操作条：左侧页面功能（新建 / 导入…），右侧通用工具（刷新 / 列设置 / 下载） -->
    <div class="dc-table__toolbar">
      <div class="dc-table__actions">
        <slot name="actions"/>
      </div>

      <div class="dc-table__tools">
        <slot name="tools"/>

        <a-tooltip :title="t('common.refresh')">
          <a-button type="text" size="small" @click="emit('refresh')">
            <template #icon>
              <ReloadOutlined/>
            </template>
          </a-button>
        </a-tooltip>

        <a-popover trigger="click" placement="bottomRight">
          <template #content>
            <div class="dc-table__columns">
              <a-checkbox v-for="col in toggleableColumns"
                          :key="col.key"
                          :checked="!hiddenKeys.has(col.key)"
                          @change="(e: any) => toggleColumn(col.key, e.target.checked)">
                {{ col.title }}
              </a-checkbox>
            </div>
          </template>
          <a-tooltip :title="columnsText">
            <a-button type="text" size="small">
              <template #icon>
                <SettingOutlined/>
              </template>
            </a-button>
          </a-tooltip>
        </a-popover>

        <a-tooltip :title="downloadText">
          <a-button type="text" size="small" @click="onDownload">
            <template #icon>
              <ExportOutlined/>
            </template>
          </a-button>
        </a-tooltip>
      </div>
    </div>

    <!-- 表格 + 分页 -->
    <a-spin :spinning="loading">
      <a-table :columns="displayColumns"
               :data-source="dataSource"
               :pagination="false"
               :row-key="rowKey"
               :size="size"
               :scroll="{ x: 'max-content' }">
        <template #bodyCell="slotProps">
          <slot name="bodyCell" v-bind="slotProps"/>
        </template>
      </a-table>

      <div v-if="total > 0" class="dc-table__pagination">
        <a-pagination :current="pageIndex"
                      :page-size="pageSize"
                      :total="total"
                      show-size-changer
                      :page-size-options="pageSizeOptions"
                      @change="(page: number, size: number) => emit('page-change', page, size)"
                      @show-size-change="(current: number, size: number) => emit('size-change', current, size)"/>
      </div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { ExportOutlined, ReloadOutlined, SettingOutlined } from '@ant-design/icons-vue'

interface TableColumn
{
  key: string
  title?: string
  dataIndex?: string

  [prop: string]: any
}

const props = withDefaults(defineProps<{
  columns: TableColumn[]
  dataSource: any[]
  loading?: boolean
  pageIndex?: number
  pageSize?: number
  total?: number
  rowKey?: string
  size?: 'small' | 'middle' | 'large'
  pageSizeOptions?: string[]
  /** 不允许在“列设置”里隐藏的列（如操作列） */
  lockedColumnKeys?: string[]
}>(), {
  loading: false,
  pageIndex: 1,
  pageSize: 10,
  total: 0,
  rowKey: 'id',
  size: 'small',
  pageSizeOptions: () => ['10', '20', '50'],
  lockedColumnKeys: () => ['action']
})

const emit = defineEmits<{
  (e: 'refresh'): void
  (e: 'download'): void
  (e: 'page-change', page: number, size: number): void
  (e: 'size-change', current: number, size: number): void
}>()

const { t } = useI18n()

// download / columns 键在服务端 i18n 可能尚未提供，缺失时回退到中性文案
const columnsText = computed(() => {
  const value = t('common.columns')
  return value === 'common.columns' ? 'Columns' : value
})
const downloadText = computed(() => {
  const value = t('common.download')
  return value === 'common.download' ? 'Download' : value
})

const hiddenKeys = ref<Set<string>>(new Set())

// 可在“列设置”里切换显隐的列：有 title、且不在锁定列表内
const toggleableColumns = computed(() =>
  props.columns.filter(col => col.key && col.title && !props.lockedColumnKeys.includes(col.key))
)

const displayColumns = computed(() =>
  props.columns.filter(col => !hiddenKeys.value.has(col.key))
)

const toggleColumn = (key: string, checked: boolean) => {
  const next = new Set(hiddenKeys.value)
  if (checked) {
    next.delete(key)
  }
  else {
    next.add(key)
  }
  hiddenKeys.value = next
}

// 默认下载：把当前页数据按可见列导出为 CSV；父组件可监听 @download 覆盖
const onDownload = () => {
  emit('download')

  const cols = displayColumns.value.filter(col => col.dataIndex)
  if (!cols.length || !props.dataSource.length) {
    return
  }
  const escape = (v: any) => {
    const s = v === null || v === undefined ? '' : String(v)
    return /[",\n]/.test(s) ? `"${ s.replace(/"/g, '""') }"` : s
  }
  const header = cols.map(col => escape(col.title)).join(',')
  const rows = props.dataSource.map(row => cols.map(col => escape(row[col.dataIndex as string])).join(','))
  const csv = [header, ...rows].join('\n')

  const blob = new Blob(['﻿' + csv], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = `export-${ Date.now() }.csv`
  link.click()
  URL.revokeObjectURL(url)
}
</script>

<style scoped>
/* 自带布局样式，不依赖工具类样式文件 */
.dc-table__filter {
    margin-bottom: 12px;
}

.dc-table__toolbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    margin-bottom: 12px;
}

.dc-table__actions {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-wrap: wrap;
}

.dc-table__tools {
    display: flex;
    align-items: center;
    gap: 6px;
}

.dc-table__pagination {
    display: flex;
    justify-content: flex-end;
    padding: 12px 0 4px;
}

.dc-table__columns {
    display: flex;
    flex-direction: column;
    gap: 6px;
    min-width: 150px;
    max-height: 320px;
    overflow: auto;
}
</style>
