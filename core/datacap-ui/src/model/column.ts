import { BaseModel } from '@/model/base'

export interface ColumnModel
    extends BaseModel
{
    description?: string
    type?: string
    comment?: string
    defaultValue?: string
    position?: string
    isNullable?: boolean
    maximumLength?: string
    collation?: string
    isKey?: boolean
    privileges?: string
    dataType?: string
    extra?: string
    opened?: boolean
    length?: number
    primaryKey?: boolean
    autoIncrement?: boolean
    removed?: boolean
}

export class ColumnRequest
{
    public static of(): ColumnModel
    {
        return {
            name: 'column_name',
            type: undefined,
            comment: undefined,
            defaultValue: undefined,
            length: 0,
            primaryKey: false,
            autoIncrement: false,
            isNullable: false,
            removed: false
        }
    }
}
