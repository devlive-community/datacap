import { ResponseModel } from "@/model/ResponseModel";
import { HttpCommon } from "@/common/HttpCommon";
import { FormatModel } from "@/model/FormatModel";

const format = "/api/v1/format";

export class FormatService {
    formatSql(configure: FormatModel): Promise<ResponseModel> {
        return new HttpCommon().post(format + '/sql', configure);
    }
}
