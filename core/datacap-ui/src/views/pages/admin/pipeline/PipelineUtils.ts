const createHeaders = (i18n: any) => {
    return [
        { key: 'id', hidden: true, header: i18n.t('common.id'), class: 'text-center' },
        { key: 'name', hidden: true, header: i18n.t('common.name'), length: 20 },
        { key: 'work', hidden: true, header: i18n.t('common.work'), ellipsis: true },
        { key: 'startTime', hidden: true, header: i18n.t('common.createTime'), class: 'text-center' },
        { key: 'endTime', hidden: true, header: i18n.t('common.endTime'), class: 'text-center' },
        { key: 'elapsed', hidden: true, header: i18n.t('common.elapsed'), class: 'text-center' },
        { key: 'executor', hidden: true, header: i18n.t('common.executor'), slot: 'executor', class: 'text-center' },
        { key: 'from', hidden: true, header: i18n.t('common.from'), slot: 'from', class: 'text-center' },
        { key: 'to', hidden: true, header: i18n.t('common.to'), slot: 'to', class: 'text-center' },
        { key: 'state', hidden: true, header: i18n.t('common.state'), slot: 'state', class: 'text-center' },
        { key: 'action', hidden: true, header: i18n.t('common.action'), slot: 'action', class: 'text-center' }
    ]
}

export {
    createHeaders
}
