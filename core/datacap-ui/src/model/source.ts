export interface SourceModel
{
    id: string
    name: string
    description: string
    type: string
    protocol: string
    host: string
    port: number
    username: string
    password: string
    catalog: string
    database: string
    ssl: boolean
    publish: boolean
    configure: string
    usedConfig: boolean
    version: string
    available: boolean
    message: string
    createTime: string
    updateTime: string
    configures: Map<string, string>
}