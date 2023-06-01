const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.id'),
      key: 'id',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.username'),
      key: 'username',
      tooltip: true,
      ellipsis: true
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
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
