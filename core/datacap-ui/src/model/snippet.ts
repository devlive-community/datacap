export interface SnippetModel
{
    id?: number
    name?: string
    description?: string
    code?: string
    createTime?: string
    updateTime?: string
}

export class SnippetRequest
{
    public static of(): SnippetModel
    {
        return {
            name: undefined,
            description: undefined,
            code: ''
        }
    }
}
