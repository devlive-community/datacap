class CommonUtils
{
  getXAxis(type = 'value', items: any[])
  {
    return {
      type: type,
      data: items
    }
  }

}

export default new CommonUtils();
