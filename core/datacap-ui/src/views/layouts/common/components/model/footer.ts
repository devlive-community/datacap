export interface FooterModel
{
    title?: string
    icon?: string
    link?: string
    external?: boolean
    children?: FooterModel[],
    copyright?: string
    blank?: string
}
