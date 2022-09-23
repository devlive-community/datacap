import { ResponseModel } from "@/model/ResponseModel";
import { HttpCommon } from "@/common/HttpCommon";

const pluginAudit = "/api/v1/audit/plugin";

export class AuditService {
    getPluginAudits(page: number, size: number): Promise<ResponseModel> {
        return new HttpCommon().get(pluginAudit, { page: page, size: size });
    }
}
