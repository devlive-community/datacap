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
      title: i18n.t('common.protocol'),
      name: 'protocol',
      dataIndex: 'protocol',
      key: 'protocol',
      ellipsis: true
    },
    {
      title: i18n.t('common.host'),
      name: 'host',
      dataIndex: 'host',
      key: 'host',
      ellipsis: true
    },
    {
      title: i18n.t('common.port'),
      name: 'port',
      dataIndex: 'port',
      key: 'port',
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
      title: i18n.t('common.catalog'),
      name: 'catalog',
      dataIndex: 'catalog',
      key: 'catalog',
      ellipsis: true
    },
    {
      title: i18n.t('common.database'),
      name: 'database',
      dataIndex: 'database',
      key: 'database',
      ellipsis: true
    },
    {
      title: i18n.t('common.public'),
      name: 'public',
      dataIndex: 'public',
      key: 'public'
    },
    {
      title: i18n.t('common.createTime'),
      name: 'createTime',
      dataIndex: 'createTime',
      key: 'createTime',
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
  createHeaders,
  emptySource
}
