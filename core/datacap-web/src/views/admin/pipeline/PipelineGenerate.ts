const createHeaders = (i18n: any) => {
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
      ellipsis: true,
      tooltip: true,
      width: 200
    },
    {
      title: i18n.t('common.work'),
      key: 'work',
      ellipsis: true,
      tooltip: true,
      width: 200
    },
    {
      title: i18n.t('common.endTime'),
      key: 'startTime',
      ellipsis: true,
      tooltip: true,
      align: 'center',
      width: 200
    },
    {
      title: i18n.t('common.endTime'),
      key: 'startTime',
      ellipsis: true,
      tooltip: true,
      align: 'center',
      width: 200
    },
    {
      title: i18n.t('common.elapsed'),
      key: 'elapsed',
      ellipsis: true,
      tooltip: true,
      align: 'center',
      width: 100
    },
    {
      title: i18n.t('common.executor'),
      key: 'executor',
      ellipsis: true,
      tooltip: true,
      slot: 'executor',
      align: 'center',
      width: 100
    },
    {
      title: i18n.t('common.from'),
      key: 'from',
      slot: 'from',
      align: 'center',
      width: 100
    },
    {
      title: i18n.t('common.to'),
      key: 'to',
      slot: 'to',
      align: 'center',
      width: 100
    },
    {
      title: i18n.t('common.state'),
      key: 'state',
      tooltip: true,
      align: 'center',
      width: 100
    },
    {
      title: i18n.t('common.action'),
      slot: 'action',
      key: 'action',
      width: 150,
      fixed: 'right',
      align: 'center'
    }
  ];
};

export {
  createHeaders
}
