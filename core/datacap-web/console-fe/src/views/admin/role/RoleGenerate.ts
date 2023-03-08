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
      ellipsis: true
    },
    {
      title: i18n.t('common.description'),
      key: 'description',
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
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
