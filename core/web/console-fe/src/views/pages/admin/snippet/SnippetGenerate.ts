const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      key: 'id',
    },
    {
      title: i18n.t('common.name'),
      key: 'name',
      ellipsis: true
    },
    {
      title: i18n.t('common.description'),
      key: 'description',
      ellipsis: true
    },
    {
      title: i18n.t('common.username'),
      key: 'username',
      slot: 'username',
      ellipsis: true
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
      ellipsis: true
    },
    {
      title: i18n.t('common.updateTime'),
      key: 'updateTime',
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

