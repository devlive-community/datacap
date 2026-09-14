import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

export function useHeaders()
{
    const { t } = useI18n()

    // ant-design-vue Table 列格式（SourceHome 已迁 antd）；type/public/version/available/action 走 #bodyCell
    const headers = computed(() => [
        { title: t('common.id'), dataIndex: 'id', key: 'id' },
        { title: t('common.name'), dataIndex: 'name', key: 'name' },
        { title: t('common.type'), key: 'type' },
        { title: t('common.protocol'), dataIndex: 'protocol', key: 'protocol' },
        { title: t('common.host'), dataIndex: 'host', key: 'host' },
        { title: t('common.port'), dataIndex: 'port', key: 'port' },
        { title: t('common.public'), key: 'public' },
        { title: t('common.version'), key: 'version' },
        { title: t('common.available'), key: 'available' },
        { title: t('common.createTime'), dataIndex: 'createTime', key: 'createTime' },
        { title: t('common.updateTime'), dataIndex: 'updateTime', key: 'updateTime' },
        { title: t('common.action'), key: 'action' }
    ])

    // TODO: SourceHistory 迁 antd 后改成 antd 列格式；目前仍是 ShadcnTable 的 {key,label,slot}
    const historyHeaders = computed(() => [
        { key: 'id', label: t('common.id'), width: 80 },
        { key: 'name', label: t('common.name') },
        { key: 'createTime', label: t('common.createTime') },
        { key: 'updateTime', label: t('common.updateTime') },
        { key: 'elapsed', label: t('common.elapsed'), slot: 'elapsed' },
        { key: 'state', label: t('common.state'), slot: 'state' },
        { key: 'result', label: t('common.result'), slot: 'result' }
    ])

    return {
        headers,
        historyHeaders
    }
}
