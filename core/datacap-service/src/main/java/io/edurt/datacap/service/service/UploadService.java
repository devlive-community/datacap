package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.UploadBody;

public interface UploadService
{
    CommonResponse upload(UploadBody configure);
}
