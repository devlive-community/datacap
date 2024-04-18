import { ResponseModel } from '@/model/response'
import { BaseService } from '@/services/base'
import { HttpUtils } from '@/utils/http'
import { TableExportModel, TableFilter, TableModel } from '@/model/table'

const DEFAULT_PATH = '/api/v1/table'

class TableService
    extends BaseService
{
    constructor()
    {
        super(DEFAULT_PATH)
    }

    /**
     * Retrieves all data from the database by the specified ID.
     *
     * @param {number} code - The code of the database.
     * @return {Promise<ResponseModel>} A promise that resolves to a ResponseModel object.
     */
    getAllByDatabase(code: string): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${ DEFAULT_PATH }/database/${ code }`)
    }

    /**
     * Retrieves data for a specific ID using the provided table filter configuration.
     *
     * @param {number} code - The code of the data to retrieve.
     * @param {TableFilter} configure - The table filter configuration.
     * @return {Promise<ResponseModel>} - A promise that resolves to the response model.
     */
    getData(code: string, configure: TableFilter): Promise<ResponseModel>
    {
        if (!configure) {
            configure = <TableFilter>{}
        }
        return new HttpUtils().post(`${ DEFAULT_PATH }/${ code }`, configure)
    }

    /**
     * A description of the entire function.
     *
     * @param {string} code - The identifier of the data.
     * @param {any} configure - The configuration object.
     * @return {Promise<ResponseModel>} A promise that resolves to the response model.
     */
    putData(code: string, configure: TableFilter): Promise<ResponseModel>
    {
        return new HttpUtils().put(`${ DEFAULT_PATH }/${ code }`, configure)
    }

    /**
     * Export data with the given id and configuration.
     *
     * @param {number} id - The id of the data to export.
     * @param {TableExportModel} configure - The configuration for the export.
     * @return {Promise<ResponseModel>} - A promise that resolves to a ResponseModel.
     */
    exportData(id: number, configure: TableExportModel): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${ DEFAULT_PATH }/export/${ id }`, configure)
    }

    createTable(databaseId: number, configure: TableModel): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${ DEFAULT_PATH }/createTable/${ databaseId }`, configure)
    }

    manageColumn(code: string, configure: any): Promise<ResponseModel>
    {
        return new HttpUtils().post(`${ DEFAULT_PATH }/manageColumn/${ code }`, configure)
    }
}

export default new TableService()