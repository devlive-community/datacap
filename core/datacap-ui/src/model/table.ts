import { PaginationResponseModel } from '@/model/pagination'
import { BaseModel } from '@/model/base'
import { DatabaseModel } from '@/model/database'
import { ColumnModel } from '@/model/column'

export interface TableModel
    extends BaseModel
{
    description?: string
    type?: string
    engine?: string
    format?: string
    rows?: string
    inCreateTime?: string
    inUpdateTime?: string
    collation?: string
    comment?: string
    avgRowLength?: string
    dataLength?: string
    indexLength?: string
    autoIncrement?: string
    database?: DatabaseModel
    columns?: Array<ColumnModel>
    statement?: string
    preview?: string
}

export class TableRequest
{
    public static of(): TableModel
    {
        return {
            name: undefined,
            comment: undefined,
            engine: undefined,
            columns: new Array<ColumnModel>()
        }
    }
}

export interface Filter
{
    condition: string
    filters: Array<ColumnFilter>
}

export interface OrderFilter
{
    column: string
    order: string
}

export interface TableFilter
{
    pagination?: PaginationResponseModel
    orders?: Array<OrderFilter>
    type?: SqlType
    columns?: Array<SqlColumn>
    original?: Map<string, string>
    preview?: boolean
    value?: string | number
    filter?: Filter
    newColumns?: Array<any>
    columnId?: number
}

export interface ColumnFilter
{
    index?: number
    column?: string
    operator?: string
    operations?: Array<Operator>
    value?: any
}

export interface SqlColumn
{
    column?: string
    value?: string
    original?: Map<string, object>
}

export interface TableExportModel
{
    count: number
    format: string
    path?: string
}

export class TableExportRequest
{
    public static of(): TableExportModel
    {
        return {
            count: 100,
            format: 'CSV'
        }
    }
}

export enum SqlType
{
    SELECT = 'SELECT',
    UPDATE = 'UPDATE',
    DELETE = 'DELETE',
    ALTER = 'ALTER',
    SHOW = 'SHOW',
    INSERT = 'INSERT',
    TRUNCATE = 'TRUNCATE',
    DROP = 'DROP',
    CREATE = 'CREATE',
    MODIFY = 'MODIFY',
}

export enum Operator
{
    EQ = ('='),
    NEQ = ('<>'),
    GT = ('>'),
    GTE = ('>='),
    LT = ('<'),
    LTE = ('<='),
    LIKE = ('LIKE'),
    NLIKE = ('NOT LIKE'),
    NULL = ('IS NULL'),
    NNULL = ('IS NOT NULL')
}
