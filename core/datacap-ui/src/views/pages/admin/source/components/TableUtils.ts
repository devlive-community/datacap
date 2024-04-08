const createHeaders = (i18n: any) => {
    return [
        { key: 'name', hidden: true, header: i18n.t('common.name') },
        { key: 'dataType', hidden: true, header: i18n.t('common.type'), class: 'text-center' },
        { key: 'maximumLength', hidden: true, header: i18n.t('common.length') },
        { key: 'isNullable', hidden: true, header: i18n.t('source.common.isNullable'), slot: 'isNullable', class: 'text-center' },
        { key: 'defaultValue', hidden: true, header: i18n.t('source.common.defaultValue'), class: 'text-center' },
        { key: 'comment', hidden: true, header: i18n.t('source.common.comment'), class: 'text-center' },
        { key: 'extra', hidden: true, header: i18n.t('source.common.extra'), class: 'text-center' }
    ]
}

export {
    createHeaders
}
