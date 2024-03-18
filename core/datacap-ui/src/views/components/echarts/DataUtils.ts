/**
 * Returns an array of unique values extracted from the specified key in each object within the provided array.
 *
 * @param {string} key - The key to extract values from each object in the array.
 * @param {any[]} columns - The array of objects to extract values from.
 * @return {any[]} An array containing unique values extracted from the specified key in each object.
 */
export function getValueByKey(key: string, columns: []): any[]
{
    const container: any[] = []
    columns.forEach(column => {
        if (container.indexOf(column[key]) === -1) {
            container.push(column[key])
        }
    });
    return container
}
