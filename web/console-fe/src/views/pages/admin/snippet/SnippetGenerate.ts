const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      name: 'id',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: i18n.t('common.name'),
      name: 'name',
      dataIndex: 'name',
      key: 'name',
      ellipsis: true
    },
    {
      title: i18n.t('common.description'),
      name: 'description',
      dataIndex: 'description',
      key: 'description',
      ellipsis: true
    },
    {
      title: i18n.t('common.username'),
      name: 'username',
      dataIndex: 'username',
      key: 'username',
      ellipsis: true
    },
    {
      title: i18n.t('common.createTime'),
      name: 'createTime',
      dataIndex: 'createTime',
      key: 'createTime',
      ellipsis: true
    },
    {
      title: i18n.t('common.updateTime'),
      name: 'updateTime',
      dataIndex: 'updateTime',
      key: 'updateTime',
      ellipsis: true
    },
    {
      title: i18n.t('common.action'),
      name: 'action',
      dataIndex: 'action',
      key: 'action'
    }
  ];
};

export {
  createHeaders
}

