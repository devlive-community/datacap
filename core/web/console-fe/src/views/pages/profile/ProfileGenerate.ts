const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.device'),
      key: 'device',
      ellipsis: true
    },
    {
      title: i18n.t('common.client'),
      key: 'client',
      ellipsis: true
    },
    {
      title: i18n.t('common.ip'),
      key: 'ip',
      ellipsis: true
    },
    {
      title: i18n.t('common.state'),
      slot: 'state',
      key: 'state'
    },
    {
      title: i18n.t('common.ua'),
      key: 'ua',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.loginTime'),
      key: 'createTime',
      ellipsis: true,
      tooltip: true
    }
  ];
}

export {
  createHeaders
};
