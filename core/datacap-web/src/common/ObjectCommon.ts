export class ObjectCommon
{
  /**
   * Generate a dynamic object with a single key-value pair.
   *
   * @param {string} key - The key of the object.
   * @param {any} value - The value associated with the key.
   * @return {{ [key: string]: any }} - The dynamic object with the key-value pair.
   */
  public static getDynamicObject(key: string, value: any): { [key: string]: any }
  {
    const dynamicObject: { [key: string]: any } = {};
    dynamicObject[key] = value;
    return dynamicObject;
  }
}
