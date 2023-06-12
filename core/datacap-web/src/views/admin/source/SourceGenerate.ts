import {SourceModel} from "@/model/SourceModel";

const emptySource: SourceModel = {
  name: "",
  description: "",
  protocol: "",
  host: "",
  port: 0,
  username: "",
  password: "",
  catalog: "",
  database: "",
  type: "",
  configures: {}
};

const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      key: 'id',
    },
    {
      title: i18n.t('common.name'),
      key: 'name',
      slot: 'name',
      ellipsis: true
    },
    {
      title: i18n.t('common.type'),
      key: 'type',
      slot: 'type'
    },
    {
      title: i18n.t('common.protocol'),
      key: 'protocol',
      ellipsis: true
    },
    {
      title: i18n.t('common.host'),
      key: 'host',
      slot: 'host',
      ellipsis: true
    },
    {
      title: i18n.t('common.port'),
      key: 'port',
      ellipsis: true
    },
    {
      title: i18n.t('common.public'),
      slot: 'public',
      key: 'public'
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
      ellipsis: true,
      tooltip: true
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
  createHeaders,
  emptySource
}
