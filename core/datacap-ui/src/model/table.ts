import { PaginationResponseModel } from '@/model/pagination'

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
    value?: string
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

export interface ExportBody
{
    count: number
    format: string
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
