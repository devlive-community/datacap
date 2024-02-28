const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      key: 'id',
      width: 100,
      sortable: 'custom',
      align: 'center'
    },
    {
      title: i18n.t('common.name'),
      key: 'name',
      minWidth: 150,
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.description'),
      key: 'description',
      ellipsis: true,
      width: 200,
      align: 'center',
      tooltip: true
    },
    {
      title: i18n.t('common.source'),
      key: 'source',
      slot: 'source',
      width: 100,
      align: 'center'
    },
    {
      title: i18n.t('common.syncMode'),
      key: 'syncMode',
      slot: 'syncMode',
      ellipsis: true,
      width: 100,
      align: 'center'
    },
    {
      title: i18n.t('common.scheduler'),
      key: 'scheduler',
      ellipsis: true,
      width: 100,
      align: 'center'
    },
    {
      title: i18n.t('common.executor'),
      key: 'executor',
      ellipsis: true,
      width: 100,
      align: 'center'
    },
    {
      title: i18n.t('common.state'),
      key: 'state',
      slot: 'state',
      ellipsis: true,
      minWidth: 150,
      align: 'center'
    },
    {
      title: i18n.t('common.totalRows'),
      key: 'totalRows',
      ellipsis: true,
      align: 'center',
      minWidth: 150,
      tooltip: true
    },
    {
      title: i18n.t('common.totalSize'),
      key: 'totalSize',
      ellipsis: true,
      minWidth: 150,
      align: 'center',
      tooltip: true
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
      ellipsis: true,
      minWidth: 170,
      align: 'center',
      tooltip: true
    },
    {
      title: i18n.t('common.updateTime'),
      key: 'updateTime',
      ellipsis: true,
      minWidth: 170,
      align: 'center',
      tooltip: true
    },
    {
      title: i18n.t('common.action'),
      slot: 'action',
      align: 'center',
      minWidth: 150,
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
