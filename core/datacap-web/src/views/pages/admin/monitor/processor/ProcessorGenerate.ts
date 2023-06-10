const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('monitor.id'),
      key: 'id',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('monitor.content'),
      key: 'content',
      ellipsis: true
    },
    {
      title: i18n.t('monitor.rows'),
      key: 'rows'
    },
    {
      title: i18n.t('monitor.elapsed'),
      key: 'elapsed'
    },
    {
      title: i18n.t('monitor.bytes'),
      key: 'bytes'
    },
    {
      title: i18n.t('monitor.memory'),
      key: 'memory'
    },
    {
      title: i18n.t('monitor.bytesRead'),
      key: 'bytesRead'
    },
    {
      title: i18n.t('monitor.bytesWritten'),
      key: 'bytesRead'
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
