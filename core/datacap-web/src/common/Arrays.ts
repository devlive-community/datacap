import {Configure} from "@/model/Configure";

export class Arrays
{
  public static arrayToMap(array: Array<Configure>): Map<string, any>
  {
    const map = new Map();
    array.forEach(element => {
      map.set(element.field, element.value);
    });
    return map;
  }

  public static arrayToObject(array: Array<Configure>): any
  {
    return Object.fromEntries(this.arrayToMap(array));
  }

  public static arrayToMapString(array: Array<Configure>): string
  {
    return JSON.stringify(Object.fromEntries(this.arrayToMap(array)));
  }
}
