/**
 * Generates a table header based on the given data array.
 *
 * @param {never[]} data - the input data array
 * @return {object[]} an array of objects representing the table header
 */
const createdTableHeader = (data: never[]): object[] => {
    return data.map(item => {
        return {
            'field': item,
            'title': item,
            'width': 'auto'
        }
    })
}

export {
    createdTableHeader
}
