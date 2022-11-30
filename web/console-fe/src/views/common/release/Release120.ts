import {ReleaseNote} from "./ReleaseNote";

const content = `
> The current release involves several major updates. The following link is [**Roadmap**](https://github.com/orgs/EdurtIO/projects/2)

#### General

---

- Support http protocol

#### Web

---

- Support for data result column header hiding [#139](https://github.com/EdurtIO/incubator-datacap/pull/139)
- Support data result filtering [#132](https://github.com/EdurtIO/incubator-datacap/pull/132) [#140](https://github.com/EdurtIO/incubator-datacap/pull/140)
- Replace \`@antv/g2\` to \`echarts\`
- Replace \`ant-design-vue\` to \`iview\`
- Replace \`@antv/s2\` to \`ag-grid\`
- Optimize about page
- Optimize not found page
- Add not authorized page
- Add version badge
- Add not network page
- Support result visual line chart

#### Plugins

---

- Support cratedb
- Support cratedb for http
- Support dameng
- Support clickhouse for http
- Support tdengine for jdbc
- Support impala for jdbc
`;

const releaseNote: ReleaseNote = {
  content: content,
  latest: true,
  time: "2022-11-30",
  version: "1.2.0"
};

export default {
  releaseNote
}
