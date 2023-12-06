const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.name'),
      key: 'name',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('common.type'),
      key: 'dataType',
      align: 'center'
    },
    {
      title: i18n.t('common.length'),
      key: 'maximumLength',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('source.manager.isNullable'),
      key: 'isNullable',
      slot: 'isNullable',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('source.manager.defaultValue'),
      key: 'defaultValue',
      ellipsis: true,
      tooltip: true,
      align: 'center'
    },
    {
      title: i18n.t('source.manager.comment'),
      key: 'comment',
      ellipsis: true,
      tooltip: true,
      align: 'center'
    },
    {
      title: i18n.t('source.manager.extra'),
      key: 'extra',
      ellipsis: true,
      tooltip: true,
      align: 'center'
    }
  ];
};

export {
  createHeaders
}
