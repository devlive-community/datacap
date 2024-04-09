const createHeaders = (i18n: any) => {
    return [
        { key: 'id', hidden: true, header: i18n.t('common.id'), class: 'text-center' },
        { key: 'name', hidden: true, header: i18n.t('common.name'), class: 'text-center' },
        { key: 'type', hidden: true, header: i18n.t('common.type'), class: 'text-center' },
        { key: 'realtime', hidden: true, header: i18n.t('common.realtime'), slot: 'realtime', class: 'text-center' },
        { key: 'source', hidden: true, header: i18n.t('common.source'), slot: 'source', class: 'text-center' },
        { key: 'createTime', hidden: true, header: i18n.t('common.createTime'), class: 'text-center' },
        { key: 'updateTime', hidden: true, header: i18n.t('common.updateTime'), class: 'text-center' },
        { key: 'action', hidden: true, header: i18n.t('common.action'), slot: 'action', class: 'text-center' }
    ]
}

export {
    createHeaders
}