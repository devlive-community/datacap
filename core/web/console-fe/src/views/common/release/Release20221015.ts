import {ReleaseNote} from "@/views/common/release/ReleaseNote";

const content = `
#### General
- Building SPI supports multiple data sources
- Supports web visualization based on Vue architecture
- Support Data source usage history
- Data statistics for data sources and history

#### Plugins
- Support ClickHouse
- Support MySQL
- Support Presto
- Support Redis
- Support PostgreSQL
- Support Trino
- Support ElasticSearch
- Support Apache Druid
- Support Apache Kyuubi
- Support Apache Hive
- Support Apache Kylin
- Support Apache Ignite
- Support IBM DB2
`;

const releaseNote: ReleaseNote = {
  content: content,
  latest: false,
  time: "2022-10-15",
  version: "1.0.0"
};

export default {
  releaseNote
}
