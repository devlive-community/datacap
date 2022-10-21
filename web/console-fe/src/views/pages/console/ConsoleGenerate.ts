const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      name: 'id',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: i18n.t('common.plugin'),
      name: 'plugin',
      dataIndex: 'plugin',
      key: 'plugin',
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
      title: i18n.t('common.endTime'),
      name: 'endTime',
      dataIndex: 'endTime',
      key: 'endTime',
      ellipsis: true
    },
    {
      title: i18n.t('common.elapsed'),
      name: 'elapsed',
      dataIndex: 'elapsed',
      key: 'elapsed'
    },
    {
      title: i18n.t('common.state'),
      name: 'state',
      dataIndex: 'state',
      key: 'state'
    },
    {
      title: i18n.t('common.action'),
      name: 'action',
      dataIndex: 'action',
      key: 'action'
    }
  ];
}

export {
  createHeaders
};
