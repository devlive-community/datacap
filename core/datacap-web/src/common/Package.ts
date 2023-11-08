import pg from '../../package.json';

export class PackageCommon
{
  public static get(key: string)
  {
    return pg[key];
  }
}
