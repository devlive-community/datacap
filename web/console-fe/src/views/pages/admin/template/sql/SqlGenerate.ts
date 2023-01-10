import {TemplateSql} from "@/model/template/Sql";

const emptyEntity: TemplateSql = {
  system: false,
  configure: {},
  content: '',
  description: '',
  name: '',
  plugin: ''
};

const createHeaders = (i18n: any) => {
  return [
    {
      title: i18n.t('common.no'),
      key: 'id',
      sortable: 'custom'
    },
    {
      title: i18n.t('common.name'),
      key: 'name',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.description'),
      key: 'description',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.plugin'),
      key: 'plugin',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.createTime'),
      key: 'createTime',
      ellipsis: true,
      tooltip: true
    },
    {
      title: i18n.t('common.updateTime'),
      key: 'updateTime',
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
  emptyEntity
}
