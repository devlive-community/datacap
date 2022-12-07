import {ResponseModel} from "@/model/ResponseModel";
import {HttpCommon} from "@/common/HttpCommon";
import {Filter} from "@/model/Filter";

const pluginAudit = "/api/v1/audit/plugin";

export class AuditService
{
  getPluginAudits(filter: Filter): Promise<ResponseModel>
  {
    return new HttpCommon().post(pluginAudit, filter);
  }
}
