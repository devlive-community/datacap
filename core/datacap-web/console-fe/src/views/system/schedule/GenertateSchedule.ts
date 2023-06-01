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
    }
  ];
};

export {
  createHeaders
}
