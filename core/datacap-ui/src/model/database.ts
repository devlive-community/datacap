import { BaseModel } from '@/model/base'

export interface DatabaseModel
    extends BaseModel
{
    description?: string
    catalog?: string
}