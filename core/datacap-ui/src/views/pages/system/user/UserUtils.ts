const createHeaders = (i18n: any) => {
    return [
        {key: 'id', hidden: true, header: i18n.t('common.id')},
        {key: 'username', hidden: true, header: i18n.t('common.username')},
        {key: 'createTime', hidden: true, header: i18n.t('common.createTime')},
        {key: 'updateTime', hidden: true, header: i18n.t('common.updateTime')},
        {key: 'role', hidden: true, header: i18n.t('common.role'), slot: 'role', width: 100},
        {key: 'action', hidden: true, header: i18n.t('common.action'), slot: 'action', class: 'text-right'}
    ]
}

export {
    createHeaders
}
