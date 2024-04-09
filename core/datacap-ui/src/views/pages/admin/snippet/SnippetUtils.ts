const createHeaders = (i18n: any) => {
    return [
        { key: 'id', hidden: true, header: i18n.t('common.id'), class: 'text-center' },
        { key: 'name', hidden: true, header: i18n.t('common.name'), class: 'text-center' },
        { key: 'description', hidden: true, header: i18n.t('common.description'), class: 'text-center' },
        { key: 'username', hidden: true, header: i18n.t('common.username'), slot: 'username', class: 'text-center' },
        { key: 'createTime', hidden: true, header: i18n.t('common.createTime'), class: 'text-center' },
        { key: 'updateTime', hidden: true, header: i18n.t('common.updateTime'), class: 'text-center' },
        { key: 'action', hidden: true, header: i18n.t('common.action'), slot: 'action', class: 'text-center' }
    ]
}

export {
    createHeaders
}
