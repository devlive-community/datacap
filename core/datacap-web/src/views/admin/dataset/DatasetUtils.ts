const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      key: 'id',
      sortable: 'custom',
      align: 'center'
    },
    {
      title: i18n.t('common.name'),
      key: 'name',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.description'),
      key: 'description',
      ellipsis: true,
      align: 'center',
      tooltip: true
    },
    {
      title: i18n.t('common.source'),
      key: 'source',
      slot: 'source',
      align: 'center'
    },
    {
      title: i18n.t('common.syncMode'),
      key: 'syncMode',
      slot: 'syncMode',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('common.scheduler'),
      key: 'scheduler',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('common.executor'),
      key: 'executor',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('common.state'),
      key: 'state',
      slot: 'state',
      ellipsis: true,
      align: 'center'
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
      title: i18n.t('common.action'),
      slot: 'action',
      align: 'center',
      key: 'action'
    }
  ]
}

const createHistoryHeaders = (i18n: any): Array<any> => {
  return [
    {
      title: i18n.t('common.id'),
      key: 'id',
      ellipsis: true,
      tooltip: true,
      width: 100
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
      title: i18n.t('common.count'),
      key: 'count',
      align: 'center'
    },
    {
      title: i18n.t('common.state'),
      key: 'state',
      tooltip: true,
      slot: 'state',
      align: 'center',
      width: 120
    }
  ];
};

export {
  createHeaders,
  createHistoryHeaders
}
