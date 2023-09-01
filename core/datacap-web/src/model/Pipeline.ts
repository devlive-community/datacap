export class PipelineModel
{
  from: PipelineMetaModel = new PipelineMetaModel();
  to: PipelineMetaModel = new PipelineMetaModel();
  content: string;
  executor = 'Seatunnel';
}

export class PipelineMetaModel
{
  id: number;
  source: any = {};
  configures = {};
}
