import { RoleModel } from '@/model/role'

export interface UserModel
{
    id: number
    username: string
    chatConfigure?: UserChatModel
    system?: boolean
    editorConfigure?: string
    createTime?: string
    updateTime?: string
    roles?: RoleModel[] | []
    avatarConfigure?: UserAvatarModel
}

export interface UsernameModel
{
    oldUsername: string
    newUsername: string | undefined
    password: string | undefined
}

export interface UserChatModel
{
    host: string | undefined
    token: string | undefined
    timeout: number
    contentCount: number
}

export interface UserAvatarModel
{
    path?: string
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
