import { RoleModel } from '@/model/role'

export interface UserModel
{
    id: number
    username: string
    chatConfigure?: string
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
