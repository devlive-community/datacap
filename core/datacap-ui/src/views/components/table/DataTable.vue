<template>
  <div class="dc-table">
    <!-- 卡片头：标题 + 描述 + 页面动作（新增等） + 通用工具（刷新 / 列设置 / 下载） -->
    <div v-if="title || description || $slots.actions || $slots.tools" class="dc-table__head">
      <div class="dc-table__head-main">
        <div v-if="title" class="dc-table__title">{{ title }}</div>
        <div v-if="description" class="dc-table__desc">{{ description }}</div>
      </div>
      <div class="dc-table__head-tools">
        <slot name="tools"/>
        <a-tooltip :title="t('common.refresh')">
          <a-button type="text" @click="emit('refresh')">
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
            <a-button>
              <template #icon>
                <SettingOutlined/>
              </template>
            </a-button>
          </a-tooltip>
        </a-popover>

        <a-tooltip :title="downloadText">
          <a-button @click="onDownload">
            <template #icon>
              <DownloadOutlined/>
            </template>
          </a-button>
        </a-tooltip>
      </div>
    </div>

    <!-- 页面动作区（新增等大按钮） -->
    <div v-if="$slots.actions" class="dc-table__actions">
      <slot name="actions"/>
    </div>

    <!-- 顶部筛选区：各页面自定义（关键词 / 状态 / 归属等 + 查询 / 重置） -->
    <div v-if="$slots.filter" class="dc-table__filter">
      <slot name="filter"/>
    </div>

    <!-- 表格 + 分页 -->
    <a-spin :spinning="loading">
      <a-table :columns="displayColumns"
               :data-source="dataSource"
               :pagination="false"
               :row-key="rowKey"
               :size="size"
               :row-selection="rowSelection ? rowSelectionConfig : undefined"
               :scroll="{ x: 'max-content' }">
        <template #bodyCell="slotProps">
          <slot name="bodyCell" v-bind="slotProps"/>
        </template>
      </a-table>

      <!-- 页脚：已选择 + 批量操作 | 分页 -->
      <div class="dc-table__footer">
        <div v-if="rowSelection" class="dc-table__batch">
          <span class="dc-table__selected">{{ $t('dataTable.selected', { count: selectedRowKeys.length }) }}</span>
          <a-button v-for="action in batchActions"
                    :key="action.key"
                    size="small"
                    :danger="action.danger"
                    :disabled="selectedRowKeys.length === 0 || loading"
                    @click="emit('batch', action.key, selectedRows)">
            {{ action.label }}
          </a-button>
        </div>

        <div v-if="total > 0" class="dc-table__pagination">
          <span class="dc-table__total">{{ $t('dataTable.total', { count: total }) }}</span>
          <a-pagination :current="pageIndex"
                        :page-size="pageSize"
                        :total="total"
                        show-size-changer
                        :page-size-options="pageSizeOptions"
                        @change="(page: number, size: number) => emit('page-change', page, size)"
                        @show-size-change="(current: number, size: number) => emit('size-change', current, size)"/>
        </div>
      </div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { DownloadOutlined, ReloadOutlined, SettingOutlined } from '@ant-design/icons-vue'

interface TableColumn
{
  key: string
  title?: string
  dataIndex?: string

  [prop: string]: any
}

export interface TableBatchAction
{
  key: string
  label: string
  danger?: boolean
}

const props = withDefaults(defineProps<{
  title?: string
  description?: string
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
  /** 是否启用行多选（复选框列 + 底部已选择计数） */
  rowSelection?: boolean
  /** 批量操作按钮（行多选启用时渲染于表格底部） */
  batchActions?: TableBatchAction[]
}>(), {
  title: '',
  description: '',
  loading: false,
  pageIndex: 1,
  pageSize: 10,
  total: 0,
  rowKey: 'id',
  size: 'small',
  pageSizeOptions: () => ['10', '20', '50'],
  lockedColumnKeys: () => ['action'],
  rowSelection: false,
  batchActions: () => []
})

const emit = defineEmits<{
  (e: 'refresh'): void
  (e: 'download'): void
  (e: 'page-change', page: number, size: number): void
  (e: 'size-change', current: number, size: number): void
  (e: 'batch', key: string, rows: any[]): void
  (e: 'selection-change', keys: any[], rows: any[]): void
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

// ---- 行多选 ----
const selectedRowKeys = ref<any[]>([])

const rowSelectionConfig = computed(() => ({
  type: 'checkbox' as const,
  selectedRowKeys: selectedRowKeys.value,
  onChange: (keys: any[], rows: any[]) => {
    selectedRowKeys.value = keys
    emit('selection-change', keys, rows)
  },
  onSelectAll: (selected: boolean) => {
    if (!selected) {
      selectedRowKeys.value = []
    }
  }
}))

const selectedRows = computed(() =>
  props.dataSource.filter(row => selectedRowKeys.value.includes(row[props.rowKey as string]))
)

// 数据刷新后清理已不存在的选中项
watch(() => props.dataSource, () => {
  const keys = new Set(props.dataSource.map(row => row[props.rowKey as string]))
  selectedRowKeys.value = selectedRowKeys.value.filter(key => keys.has(key))
})

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
  const header = cols.filter(col => col.dataIndex).map(col => escape(col.title))
  const rows = props.dataSource.map(row => cols.map(col => escape(row[col.dataIndex as string])))
  const csv = [header.join(','), ...rows.map(r => r.join(','))].join('\n')

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
.dc-table__head {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 16px;
    margin-bottom: 14px;
}

.dc-table__title {
    font-size: 16px;
    font-weight: 600;
    color: var(--dc-text-title);
}

.dc-table__desc {
    font-size: 12px;
    color: var(--dc-text-secondary);
    margin-top: 4px;
}

.dc-table__head-tools {
    display: flex;
    align-items: center;
    gap: 6px;
    flex-shrink: 0;
}

.dc-table__actions {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-wrap: wrap;
    margin-bottom: 12px;
}

.dc-table__filter {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 14px;
}

.dc-table__footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    flex-wrap: wrap;
    padding: 12px 0 4px;
}

.dc-table__batch {
    display: flex;
    align-items: center;
    gap: 8px;
}

.dc-table__selected {
    font-size: 12px;
    color: var(--dc-text-secondary);
}

.dc-table__pagination {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-left: auto;
}

.dc-table__total {
    font-size: 12px;
    color: var(--dc-text-secondary);
    white-space: nowrap;
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
