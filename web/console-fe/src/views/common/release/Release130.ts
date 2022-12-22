import {ReleaseNote} from "./ReleaseNote";

const content = `
> The current release involves several major updates.

#### General

---

- Support change username
- Support custom sql template
- Support plugin function
- Add restart script

#### Web

---

- Optimize the presentation of the data source list
- Add data source description and prompt
- Support query history id order
- Support quote query history

#### Plugins

---

- Support oceanbase for jdbc
- Support redis for native
- Support neo4j for jdbc
- Support iotdb for jdbc

#### Redis

- Support auth for native
`;

const releaseNote: ReleaseNote = {
  content: content,
  latest: true,
  time: "2022-12-16",
  version: "1.3.0"
};

export default {
  releaseNote
}
