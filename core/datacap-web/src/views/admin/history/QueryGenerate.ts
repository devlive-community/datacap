const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      key: 'id',
      sortable: 'custom',
      align: 'center'
    },
    {
      title: i18n.t('common.source'),
      key: 'plugin',
      slot: 'plugin',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('common.type'),
      slot: 'type',
      align: 'center'
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
      ellipsis: true,
      tooltip: true,
      align: 'center'
    },
    {
      title: i18n.t('common.endTime'),
      key: 'endTime',
      ellipsis: true,
      tooltip: true,
      align: 'center'
    },
    {
      title: i18n.t('common.elapsed'),
      slot: 'elapsed',
      key: 'elapsed',
      align: 'center'
    },
    {
      title: i18n.t('common.from'),
      slot: 'mode',
      align: 'center'
    },
    {
      title: i18n.t('common.count'),
      key: 'count',
      align: 'center'
    },
    {
      title: i18n.t('common.state'),
      slot: 'state',
      key: 'state',
      align: 'center'
    },
    {
      title: i18n.t('common.action'),
      slot: 'action',
      key: 'action',
      align: 'center'
    }
  ];
}

export {
  createHeaders
};
