const createdTableHeader = (data: []) => {
  return data.map(item => {
    return {
      "field": item,
      "title": item,
      "width": 'auto'
    }
  })
}

export {
  createdTableHeader
}
