const createHeaders = (i18n: any) => {
    return [
        { key: 'id', hidden: true, header: i18n.t('common.id') },
        { key: 'name', hidden: true, header: i18n.t('common.name') },
        { key: 'type', hidden: true, header: i18n.t('common.type'), slot: 'type', class: 'text-center' },
        { key: 'protocol', hidden: true, header: i18n.t('common.protocol') },
        { key: 'host', hidden: true, header: i18n.t('common.host') },
        { key: 'port', hidden: true, header: i18n.t('common.port') },
        { key: 'public', hidden: true, header: i18n.t('common.public'), slot: 'public', class: 'text-center' },
        { key: 'version', hidden: true, header: i18n.t('common.version'), slot: 'version', class: 'text-center' },
        { key: 'available', hidden: true, header: i18n.t('common.available'), slot: 'available' },
        { key: 'createTime', hidden: true, header: i18n.t('common.createTime') },
        { key: 'updateTime', hidden: true, header: i18n.t('common.updateTime') },
        { key: 'action', hidden: true, header: i18n.t('common.action'), slot: 'action', class: 'text-center' }
    ]
}

const createHistoryHeaders = (i18n: any) => {
    return [
        { key: 'id', hidden: true, header: i18n.t('common.id'), width: 80 },
        { key: 'name', hidden: true, header: i18n.t('common.name') },
        { key: 'createTime', hidden: true, header: i18n.t('common.createTime') },
        { key: 'updateTime', hidden: true, header: i18n.t('common.updateTime') },
        { key: 'elapsed', hidden: true, header: i18n.t('common.elapsed'), slot: 'elapsed', class: 'text-center' },
        { key: 'state', hidden: true, header: i18n.t('common.state'), slot: 'state', class: 'text-center' },
        { key: 'result', hidden: true, header: i18n.t('common.result'), slot: 'result' }
    ]
}

export {
    createHeaders,
    createHistoryHeaders
}
