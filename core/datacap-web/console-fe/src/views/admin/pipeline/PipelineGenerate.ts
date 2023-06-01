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
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.work'),
      key: 'work',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.endTime'),
      key: 'startTime',
    },
    {
      title: i18n.t('common.endTime'),
      key: 'startTime',
    },
    {
      title: i18n.t('common.elapsed'),
      key: 'elapsed',
    },
    {
      title: i18n.t('common.from'),
      key: 'from',
      slot: 'from',
    },
    {
      title: i18n.t('common.to'),
      key: 'to',
      slot: 'to',
    },
    {
      title: i18n.t('common.state'),
      key: 'state',
      tooltip: true
    },
    {
      title: i18n.t('common.action'),
      slot: 'action',
      key: 'action',
      width: 150,
      align: 'center'
    }
  ];
};

export {
  createHeaders
}
