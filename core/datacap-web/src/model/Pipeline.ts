export class PipelineModel
{
  from: PipelineMetaModel = new PipelineMetaModel();
  to: PipelineMetaModel = new PipelineMetaModel();
  content: string;
  executor = 'Seatunnel';
  protocol: string;
}

export class PipelineMetaModel
{
  id: number;
  source: any = {};
  configures = {};
  protocol: string;
}
