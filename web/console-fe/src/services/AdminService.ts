import { HttpCommon } from "@/common/HttpCommon";

const pluginHistoryCount = "/api/v1/audit/plugin/admin/count";
const sourceCount = "/api/v1/source/admin/count";

export class AdminService {

  getSourceCount() {
    return new HttpCommon().get(sourceCount);
  }

  getPluginHistoryCount() {
    return new HttpCommon().get(pluginHistoryCount);
  }
}
