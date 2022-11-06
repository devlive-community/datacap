import {HttpCommon} from "@/common/HttpCommon";

const pluginHistoryAdmin = "/api/v1/audit/plugin/admin";
const sourceCount = "/api/v1/source/admin/count";

export class AdminService
{

  getSourceCount()
  {
    return new HttpCommon().get(sourceCount);
  }

  getPluginHistoryCount()
  {
    return new HttpCommon().get(pluginHistoryAdmin + '/count');
  }

  getUserContribution()
  {
    return new HttpCommon().get(pluginHistoryAdmin + '/contribution');
  }

  getUserContributionRadar()
  {
    return new HttpCommon().get(pluginHistoryAdmin + '/radar');
  }
}
