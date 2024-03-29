export interface TemplateModel
{
    id?: number
    name?: string
    description?: string
    content?: string
    plugin?: string
    configure?: string
    system?: boolean
    createTime?: string
    updateTime?: string
}

export class TemplateRequest
{
    public static of(): TemplateModel
    {
        return {
            name: undefined,
            description: undefined,
            content: undefined,
            plugin: undefined,
            configure: undefined,
            system: false
        }
    }
}
