const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      key: 'id',
      sortable: 'custom',
      align: 'center'
    },
    {
      title: i18n.t('common.name'),
      key: 'name',
      ellipsis: true
    },
    {
      title: i18n.t('common.description'),
      key: 'description',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('common.source'),
      key: 'source',
      slot: 'source',
      align: 'center'
    },
    {
      title: i18n.t('common.syncMode'),
      key: 'syncMode',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('common.updateTime'),
      key: 'updateTime',
      ellipsis: true,
      align: 'center'
    },
    {
      title: i18n.t('common.action'),
      slot: 'action',
      align: 'center',
      key: 'action'
    }
  ]
}

export {
  createHeaders
}
