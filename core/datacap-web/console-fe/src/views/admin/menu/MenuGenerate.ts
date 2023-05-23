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
      ellipsis: true
    },
    {
      title: i18n.t('common.code'),
      key: 'code',
      tooltip: true,
      ellipsis: true
    },
    {
      title: i18n.t('common.description'),
      key: 'description',
      tooltip: true,
      ellipsis: true
    },
    {
      title: i18n.t('common.group'),
      key: 'group',
    },
    {
      title: i18n.t('common.sorted'),
      key: 'sorted',
    },
    {
      title: i18n.t('common.type'),
      key: 'type',
    },
    {
      title: i18n.t('common.active'),
      key: 'active',
      slot: 'active'
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
      tooltip: true,
      ellipsis: true
    },
    {
      title: i18n.t('common.updateTime'),
      key: 'updateTime',
      tooltip: true,
      ellipsis: true
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
