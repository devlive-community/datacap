const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.id'),
      key: 'id',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.name'),
      key: 'name',
      slot: 'name',
      ellipsis: true
    },
    {
      title: i18n.t('common.expression'),
      key: 'expression',
      ellipsis: true
    },
    {
      title: i18n.t('common.active'),
      key: 'active',
      slot: 'active'
    },
    {
      title: i18n.t('common.system'),
      key: 'system',
      slot: 'system'
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.updateTime'),
      key: 'updateTime',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.action'),
      slot: 'action',
      align: 'center'
    }
  ];
};

/**
 * Generates the history headers for the createHistoryHeaders function.
 *
 * @param {any} i18n - the i18n object used for translation
 * @return {Array} an array of history headers
 */
const createHistoryHeaders = (i18n: any): Array<any> => {
  return [
    {
      title: i18n.t('common.id'),
      key: 'id',
      ellipsis: true,
      tooltip: true,
      width: 60
    },
    {
      title: i18n.t('common.name'),
      key: 'name',
      tooltip: true,
      ellipsis: true
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
      ellipsis: true,
      align: 'center',
      tooltip: true
    },
    {
      title: i18n.t('common.updateTime'),
      key: 'updateTime',
      ellipsis: true,
      align: 'center',
      tooltip: true
    },
    {
      title: i18n.t('common.elapsed'),
      slot: 'elapsed',
      align: 'center',
      key: 'elapsed'
    },
    {
      title: i18n.t('common.state'),
      key: 'state',
      tooltip: true,
      slot: 'state',
      align: 'center',
      width: 120
    },
    {
      title: i18n.t('common.result'),
      key: 'info',
      slot: 'info',
      align: 'center'
    }
  ];
};

export {
  createHeaders,
  createHistoryHeaders
}
