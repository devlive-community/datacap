import {FunctionsImportMode} from "@/enum/FunctionsImportMode";

export interface FunctionsImport
{
  content: string;
  plugin: string;
  type: string;
  mode: FunctionsImportMode;
}
