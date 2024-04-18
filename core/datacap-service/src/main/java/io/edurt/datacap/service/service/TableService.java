package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.ExportBody;
import io.edurt.datacap.service.body.TableBody;
import io.edurt.datacap.service.body.TableFilter;
import io.edurt.datacap.service.entity.TableEntity;

import java.util.List;

public interface TableService
        extends BaseService<TableEntity>
{
    /**
     * Retrieves all records from the specified database.
     *
     * @param code the code of the database
     * @return a common response containing a list of table entities
     */
    CommonResponse<List<TableEntity>> getAllByDatabase(String code);

    /**
     * Retrieves data from the database based on the provided ID and table filter.
     *
     * @param id the ID of the data to retrieve
     * @param configure the table filter to apply to the data retrieval
     * @return a common response object containing the retrieved data
     */
    CommonResponse<Object> fetchDataById(Long id, TableFilter configure);

    /**
     * Generates a function comment for the given function body in a markdown code block with the correct language syntax.
     *
     * @param id the ID of the data to be exported
     * @param configure the export configuration
     * @return the response containing the exported data
     */
    CommonResponse exportDataById(Long id, ExportBody configure);

    Object dataDownload(String username, String filename);

    CommonResponse<Object> createTable(Long databaseId, TableBody configure);

    CommonResponse<Object> manageColumn(String code, TableBody configure);
}
