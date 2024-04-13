/**
 * Generates headers for a table based on i18n translations.
 *
 * @param {any} i18n - the internationalization object for translations
 * @return {Array} an array of header objects for the table
 */
const createHeaders = (i18n: any) => {
    return [
        {key: 'id', hidden: true, header: i18n.t('common.id'), width: 80},
        {key: 'name', hidden: true, header: i18n.t('common.name'), width: 150},
        {key: 'description', hidden: true, header: i18n.t('common.description'), width: 250},
        {key: 'source', hidden: true, header: i18n.t('common.source'), slot: 'source', width: 100},
        {key: 'syncMode', hidden: true, header: i18n.t('dataset.common.syncMode'), slot: 'syncMode', width: 90},
        {key: 'scheduler', hidden: true, header: i18n.t('common.scheduler'), width: 80},
        {key: 'executor', hidden: true, header: i18n.t('common.executor'), width: 80},
        {key: 'state', hidden: true, header: i18n.t('common.state'), slot: 'state', width: 80, class: 'text-center'},
        {key: 'totalRows', hidden: true, header: i18n.t('dataset.common.totalRows')},
        {key: 'totalSize', hidden: true, header: i18n.t('dataset.common.totalSize')},
        {key: 'createTime', hidden: true, header: i18n.t('common.createTime')},
        {key: 'updateTime', hidden: true, header: i18n.t('common.updateTime')},
        {key: 'action', hidden: true, header: i18n.t('common.action'), slot: 'action', width: 80, class: 'text-center'}
    ]
}

const createHistoryHeaders = (i18n: any) => {
    return [
        {key: 'id', hidden: true, header: i18n.t('common.id'), width: 80, class: 'text-center'},
        {key: 'elapsed', hidden: true, header: i18n.t('common.elapsed'), width: 80, class: 'text-center'},
        {key: 'count', hidden: true, header: i18n.t('common.count'), width: 80, class: 'text-center'},
        {key: 'createTime', hidden: true, header: i18n.t('common.createTime'), class: 'text-center'},
        {key: 'updateTime', hidden: true, header: i18n.t('common.updateTime'), class: 'text-center'},
        {key: 'state', hidden: true, header: i18n.t('common.state'), slot: 'state', width: 100, class: 'text-center'}
    ]
}

export {
    createHeaders,
    createHistoryHeaders
}
