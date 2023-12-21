const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      key: 'id',
      sortable: 'custom'
    },
    {
      title: i18n.t('common.name'),
      key: 'name',
      align: 'center'
    },
    {
      title: i18n.t('common.type'),
      key: 'type',
      align: 'center'
    },
    {
      title: i18n.t('common.realtime'),
      key: 'realtime',
      slot: 'realtime',
      align: 'center'
    },
    {
      title: i18n.t('common.source'),
      key: 'source',
      slot: 'source',
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
      title: i18n.t('common.updateTime'),
      key: 'updateTime',
      ellipsis: true,
      tooltip: true,
      align: 'center'
    },
    {
      title: i18n.t('common.action'),
      slot: 'action',
      align: 'center',
      key: 'action'
    }
  ];
}

export {
  createHeaders
};
