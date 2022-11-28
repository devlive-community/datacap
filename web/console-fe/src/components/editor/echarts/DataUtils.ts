export function getValueByKey(key: string, columns: []): any[]
{
  const container: any[] = [];
  columns.forEach(column => {
    if (container.indexOf(column[key]) === -1) {
      container.push(column[key]);
    }
  });
  return container;
}
