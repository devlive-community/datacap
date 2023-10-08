const createColumnDefs = (headers: any[], types: any[]) => {
  const columnDefs = [];
  headers.forEach((header, index) => {
    const columnDef = {
      headerName: header,
      field: header,
      headerTooltip: header + ' [' + types[index] + ']'
    };
    columnDefs.push(columnDef);
  })
  return columnDefs;
};

export {
  createColumnDefs
}
