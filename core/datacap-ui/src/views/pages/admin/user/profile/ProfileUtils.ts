const createHeaders = (i18n: any) => {
    return [
        {key: 'id', hidden: true, header: i18n.t('common.id')},
        {key: 'device', hidden: true, header: i18n.t('common.device')},
        {key: 'client', hidden: true, header: i18n.t('common.client')},
        {key: 'ip', hidden: true, header: i18n.t('common.ip')},
        {key: 'state', hidden: true, header: i18n.t('common.state'), slot: 'state'},
        {key: 'ua', hidden: true, header: i18n.t('common.ua')},
        {key: 'createTime', hidden: true, header: i18n.t('common.loginTime')}
    ]
}

export {
    createHeaders
}
