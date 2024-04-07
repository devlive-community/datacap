export interface StructureModel
{
    title?: null | string
    catalog?: null
    database?: null | any
    databaseId?: null
    table?: null
    tableId?: null
    applyId?: null | number
    type?: null
    dataType?: null
    extra?: null
    engine?: null
    level?: null | StructureEnum
    loading?: false
    render?: any
    isKey?: null
    defaultValue?: null
    comment?: null
    origin?: any
    selected?: boolean
    contextmenu?: true
    children?: StructureModel[]
}

export enum StructureEnum
{
    CATALOG,
    DATABASE,
    TABLE,
    COLUMN
}
