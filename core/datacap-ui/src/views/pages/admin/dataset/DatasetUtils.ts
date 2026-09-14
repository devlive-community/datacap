import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

/**
 * Hook for dataset table headers
 */
export function useDatasetHeaders()
{
    const { t } = useI18n()

    // ant-design-vue Table 列格式（DatasetHome 已迁 antd）；source/syncMode/state/action 走 #bodyCell
    const headers = computed(() => [
        { title: t('common.id'), dataIndex: 'id', key: 'id' },
        { title: t('common.name'), dataIndex: 'name', key: 'name' },
        { title: t('common.description'), dataIndex: 'description', key: 'description' },
        { title: t('common.source'), key: 'source' },
        { title: t('dataset.common.syncMode'), key: 'syncMode' },
        { title: t('common.scheduler'), dataIndex: 'scheduler', key: 'scheduler' },
        { title: t('common.executor'), dataIndex: 'executor', key: 'executor' },
        { title: t('common.state'), key: 'state' },
        { title: t('dataset.common.totalRows'), dataIndex: 'totalRows', key: 'totalRows' },
        { title: t('dataset.common.totalSize'), dataIndex: 'totalSize', key: 'totalSize' },
        { title: t('common.createTime'), dataIndex: 'createTime', key: 'createTime' },
        { title: t('common.updateTime'), dataIndex: 'updateTime', key: 'updateTime' },
        { title: t('common.action'), key: 'action' }
    ])

    // TODO: DatasetHistory 迁移到 antd 后，这里同样改成 antd 列格式（title/dataIndex）
    // 目前 DatasetHistory 仍是 view-shadcn-ui 的 ShadcnTable，保持 {key,label,slot} 格式
    const historyHeaders = computed(() => [
        { key: 'id', label: t('common.id') },
        { key: 'elapsed', label: t('common.elapsed') },
        { key: 'totalCount', label: t('dataset.history.totalCount') },
        { key: 'processedCount', label: t('dataset.history.processedCount') },
        { key: 'progress', label: t('dataset.history.progress'), slot: 'progress' },
        { key: 'count', label: t('common.count') },
        { key: 'createTime', label: t('common.createTime') },
        { key: 'updateTime', label: t('common.updateTime') },
        { key: 'state', label: t('common.state'), slot: 'state' },
        { key: 'action', label: t('common.action'), slot: 'action' }
    ])

    return {
        headers,
        historyHeaders
    }
}
