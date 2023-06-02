const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      key: 'id',
      sortable: 'custom'
    },
    {
      title: i18n.t('common.plugin'),
      key: 'plugin',
      slot: 'plugin',
      ellipsis: true
    },
    {
      title: i18n.t('common.type'),
      slot: 'type'
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.endTime'),
      key: 'endTime',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.elapsed'),
      slot: 'elapsed',
      key: 'elapsed'
    },
    {
      title: i18n.t('common.count'),
      key: 'count'
    },
    {
      title: i18n.t('common.state'),
      slot: 'state',
      key: 'state'
    },
    {
      title: i18n.t('common.action'),
      slot: 'action',
      key: 'action'
    }
  ];
}

export {
  createHeaders
};
