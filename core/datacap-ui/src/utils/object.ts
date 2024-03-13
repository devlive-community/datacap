export class ObjectUtils
{
    /**
     * Check if the given object is empty.
     *
     * @param {any} obj - the object to be checked
     * @return {boolean} true if the object is empty, false otherwise
     */
    static isEmpty(obj: any): boolean
    {
        return Object.keys(obj).length === 0
    }

    /**
     * Check if the provided object is not empty by calling the isEmpty method and negating the result.
     *
     * @param {any} obj - the object to check for emptiness
     * @return {boolean} true if the object is not empty, false otherwise
     */
    static isNotEmpty(obj: any): boolean
    {
        return !this.isEmpty(obj)
    }
}
