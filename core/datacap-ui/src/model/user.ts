import { RoleModel } from '@/model/role'

export interface UserModel
{
    id: number
    username: string
    chatConfigure?: any
    system?: boolean
    editorConfigure?: string
    createTime?: string
    updateTime?: string
    roles?: RoleModel[] | []
}

export interface UserRoleModel
{
    roles: number[]
    userId: number
}

export interface UserEditor
{
    fontSize: number
    theme: string
}

export interface UserQuestionModel
{
    content: string
    type: string
    newChat: true
    locale?: string
    engine?: string
    error?: string
    transType?: string
    from?: string
    model?: string
}
