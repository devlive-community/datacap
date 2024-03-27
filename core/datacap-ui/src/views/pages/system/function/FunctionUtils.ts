const createHeaders = (i18n: any) => {
    return [
        {key: 'id', hidden: true, header: i18n.t('common.id')},
        {key: 'name', hidden: true, header: i18n.t('common.name')},
        {key: 'description', hidden: true, header: i18n.t('common.description')},
        {key: 'plugin', hidden: true, header: i18n.t('common.code'), slot: 'plugin'},
        {key: 'type', hidden: true, header: i18n.t('common.type'), slot: 'type'},
        {key: 'createTime', hidden: true, header: i18n.t('common.createTime')},
        {key: 'updateTime', hidden: true, header: i18n.t('common.updateTime')},
        {key: 'action', hidden: true, header: i18n.t('common.action'), slot: 'action', class: 'text-right'}
    ]
}

export {
    createHeaders
}
