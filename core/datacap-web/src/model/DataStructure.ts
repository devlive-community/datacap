import {DataStructureEnum} from "@/enum/DataStructure";

export class DataStructureModel
{
  title: null;
  catalog: null;
  database: null;
  table: null;
  applyId: null;
  type: null;
  dataType: null;
  extra: null;
  engine: null;
  level: DataStructureEnum = DataStructureEnum.DATABASE;
  loading = false;
  render: any;
  isKey: null;
  defaultValue: null;
  comment: null;
  origin: any;
  selected: boolean;
  children: DataStructureModel[] = [];
}
