export interface FunctionModel {
    id?: number
    name?: string | undefined
    content?: string
    description?: string
    plugin?: string | string[] | undefined
    example?: string
    type?: string
    createTime?: string
    updateTime?: string
}