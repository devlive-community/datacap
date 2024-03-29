const createHeaders = (i18n: any) => {
    return [
        {key: 'id', hidden: true, header: i18n.t('common.id')},
        {key: 'name', hidden: true, header: i18n.t('common.name')},
        {key: 'description', hidden: true, header: i18n.t('common.description')},
        {key: 'plugin', hidden: true, header: i18n.t('common.plugin'), slot: 'plugin', class: 'text-center'},
        {key: 'system', hidden: true, header: i18n.t('common.system'), slot: 'system', width: 120, class: 'text-center'},
        {key: 'createTime', hidden: true, header: i18n.t('common.createTime'), width: 160, class: 'text-center'},
        {key: 'updateTime', hidden: true, header: i18n.t('common.updateTime'), width: 160, class: 'text-center'},
        {key: 'action', hidden: true, header: i18n.t('common.action'), slot: 'action', class: 'text-right'}
    ]
}

export {
    createHeaders
}
