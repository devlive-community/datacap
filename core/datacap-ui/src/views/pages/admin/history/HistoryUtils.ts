const createHeaders = (i18n: any) => {
    return [
        { key: 'id', hidden: true, header: i18n.t('common.id'), class: 'text-center' },
        { key: 'source', hidden: true, header: i18n.t('common.plugin'), slot: 'source', class: 'text-center' },
        { key: 'type', hidden: true, header: i18n.t('common.type'), slot: 'type', class: 'text-center' },
        { key: 'createTime', hidden: true, header: i18n.t('common.createTime'), class: 'text-center' },
        { key: 'updateTime', hidden: true, header: i18n.t('common.endTime'), class: 'text-center' },
        { key: 'elapsed', hidden: true, header: i18n.t('common.elapsed'), class: 'text-center' },
        { key: 'mode', hidden: true, header: i18n.t('common.from'), slot: 'mode', class: 'text-center' },
        { key: 'count', hidden: true, header: i18n.t('common.count'), class: 'text-center' },
        { key: 'state', hidden: true, header: i18n.t('common.state'), slot: 'state', class: 'text-center' },
        { key: 'action', hidden: true, header: i18n.t('common.action'), slot: 'action', class: 'text-center' }
    ]
}

export {
    createHeaders
}
