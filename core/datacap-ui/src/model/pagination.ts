export class PaginationModel
{
    pageSize: number = 10
    currentPage: number = 0
    total: number = 0
}

export interface PaginationResponseModel
{
    size: number
    total: number
    page: number
}

export class PaginationRequest
{
    /**
     * Creates a PaginationModel from a PaginationResponseModel.
     *
     * @param {PaginationResponseModel} data - the pagination response data
     * @return {PaginationModel} the created pagination model
     */
    public static of(data: PaginationResponseModel): PaginationModel
    {
        return {
            pageSize: data.size,
            total: data.total,
            currentPage: data.page
        }
    }
}
