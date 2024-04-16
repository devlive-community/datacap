package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.adhoc.Adhoc;
import io.edurt.datacap.service.entity.DataSetColumnEntity;
import io.edurt.datacap.service.entity.DataSetEntity;

import java.util.Set;

public interface DataSetService
        extends BaseService<DataSetEntity>
{
    CommonResponse<DataSetEntity> saveOrUpdate(DataSetEntity configure);

    CommonResponse<DataSetEntity> rebuild(Long id);

    CommonResponse<Set<DataSetColumnEntity>> getColumnsByCode(String code);

    CommonResponse<Boolean> syncData(Long id);

    CommonResponse<Boolean> clearData(String code);

    CommonResponse<Object> adhoc(String code, Adhoc configure);

    CommonResponse<Set<String>> getActuators();

    CommonResponse<DataSetEntity> getInfo(String code);

    CommonResponse<Object> getHistory(String code, FilterBody filter);
}
