import {ReleaseNote} from "./ReleaseNote";

const content = `
> The current release involves several major updates.

#### General

---

- Fixed restart script
- Supports monitor process
- Do not modify the default system SQL template
- Fixed plugin template by name
- Support user login log
- Refactoring plug-in configuration extraction mode

#### Experimental

---

- Support data source manager
- Add client cli

#### Web

---

- Plug-in ICONS are displayed based on the plug-in type
- Optimize editor auto prompt
- Support watermark
- Templates are not supported for adding data sources
- Fixed footer link

#### Plugins

---

- Support snowflake for jdbc
- Support ydb for jdbc

#### Docs

---

- Refactor some docs

#### Redis (Native)

---

- Fixed command multiple parameters
`;

const releaseNote: ReleaseNote = {
  content: content,
  latest: true,
  time: "2023-01-31",
  version: "1.4.0"
};

export default {
  releaseNote
}
