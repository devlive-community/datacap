/**
 * Creates headers for a table using the given internationalization object.
 *
 * @param {any} i18n - the internationalization object used for translating header labels
 * @return {Array} an array of header objects
 */
const createHeaders = (i18n: any) => {
    return [
        {key: 'id', hidden: true, header: i18n.t('common.id'), width: 80},
        {key: 'name', hidden: true, header: i18n.t('common.name'), width: 100},
        {key: 'description', hidden: true, header: i18n.t('common.description')},
        {key: 'expression', hidden: true, header: i18n.t('common.expression'), width: 100},
        {key: 'active', hidden: true, header: i18n.t('common.active'), slot: 'active', width: 80},
        {key: 'system', hidden: true, header: i18n.t('common.system'), slot: 'system', width: 80},
        {key: 'type', hidden: true, header: i18n.t('common.type')},
        {key: 'createTime', hidden: true, header: i18n.t('common.createTime')},
        {key: 'updateTime', hidden: true, header: i18n.t('common.updateTime')},
        {key: 'action', hidden: true, header: i18n.t('common.action'), slot: 'action', width: 80, class: 'text-right'}
    ]
}

/**
 * Creates history headers with internationalization support.
 *
 * @param {any} i18n - the internationalization object
 * @return {Array} an array of history headers
 */
const createHistoryHeaders = (i18n: any) => {
    return [
        {key: 'id', hidden: true, header: i18n.t('common.id'), width: 80},
        {key: 'name', hidden: true, header: i18n.t('common.name'), width: 100},
        {key: 'createTime', hidden: true, header: i18n.t('common.createTime')},
        {key: 'updateTime', hidden: true, header: i18n.t('common.updateTime')},
        {key: 'elapsed', hidden: true, header: i18n.t('common.elapsed')},
        {key: 'state', hidden: true, header: i18n.t('common.state')},
        {key: 'result', hidden: true, header: i18n.t('common.result')}
    ]
}

export {
    createHeaders,
    createHistoryHeaders
}
