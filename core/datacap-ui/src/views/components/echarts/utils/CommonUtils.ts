class CommonUtils
{
    getXAxis(type = 'value', items: any[])
    {
        return {
            type: type,
            data: items
        }
    }

    userEditorConfigure = 'DataCapUserEditorConfigure'
}

export default new CommonUtils()
