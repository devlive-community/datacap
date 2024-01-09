const createdTableHeader = (data: []) => {
  return data.map((item: { name: string }) => {
    return {
      field: item.name,
      title: item.name,
      width: 'auto'
    }
  })
}

export {
  createdTableHeader
}
