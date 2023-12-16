const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.id'),
      key: 'id',
      ellipsis: true,
      tooltip: true,
      width: 60
    },
    {
      title: i18n.t('common.name'),
      key: 'name',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.work'),
      key: 'work',
      ellipsis: true,
      tooltip: true,
      width: 200
    },
    {
      title: i18n.t('common.createTime'),
      key: 'startTime',
      ellipsis: true,
      tooltip: true,
      align: 'center',
      width: 200
    },
    {
      title: i18n.t('common.endTime'),
      key: 'startTime',
      ellipsis: true,
      tooltip: true,
      align: 'center',
      width: 200
    },
    {
      title: i18n.t('common.elapsed'),
      key: 'elapsed',
      ellipsis: true,
      tooltip: true,
      align: 'center',
      width: 100
    },
    {
      title: i18n.t('common.executor'),
      key: 'executor',
      ellipsis: true,
      tooltip: true,
      slot: 'executor',
      align: 'center'
    },
    {
      title: i18n.t('common.from'),
      key: 'from',
      slot: 'from',
      align: 'center'
    },
    {
      title: i18n.t('common.to'),
      key: 'to',
      slot: 'to',
      align: 'center'
    },
    {
      title: i18n.t('common.state'),
      key: 'state',
      tooltip: true,
      slot: 'state',
      align: 'center',
      fixed: 'right',
      width: 120
    },
    {
      title: i18n.t('common.action'),
      slot: 'action',
      key: 'action',
      fixed: 'right',
      align: 'center'
    }
  ];
};

/**
 * Retrieves the text based on the given origin value.
 *
 * @param {any} i18n - the internationalization object
 * @param {string} origin - the origin value to determine the text to retrieve
 * @return {string} the text based on the origin value
 */
const getText = (i18n: any, origin: string): string => {
  switch (origin) {
    case 'CREATED':
      return i18n.t('pipeline.common.create');
    case 'RUNNING':
      return i18n.t('pipeline.common.running');
    case 'SUCCESS':
      return i18n.t('pipeline.common.success');
    case 'FAILURE':
      return i18n.t('pipeline.common.failure');
    case 'STOPPED':
      return i18n.t('pipeline.common.stop');
    case 'TIMEOUT':
      return i18n.t('pipeline.common.timeout');
    case 'QUEUE':
      return i18n.t('pipeline.common.queue');
    default:
      return origin;
  }
}

/**
 * Returns the color based on the origin.
 *
 * @param {string} origin - The origin value.
 * @return {string} The color based on the origin.
 */
const getColor = (origin: string): string => {
  switch (origin) {
    case 'CREATED':
      return 'cyan';
    case 'RUNNING':
      return 'primary';
    case 'SUCCESS':
      return 'success';
    case 'FAILURE':
      return 'error';
    case 'STOPPED':
      return '#17233d';
    case 'TIMEOUT':
      return 'warning';
    default:
      return 'blue';
  }
}

export {
  createHeaders,
  getText,
  getColor
}
