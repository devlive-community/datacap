---
icon: material/projector-screen-variant-outline
status: new
---

The development environment is mainly divided into two services and a document module. The following is a detailed description of the construction of each service environment.

!!! warning

    Before development, please import the format files of the code into the editor, they are in the `configure` directory

### Server

---

!!! warning

    Unnecessary problems are not avoided, please be sure to read the dependent environment configuration

#### Depends on the environment

| Environment | Version | Required |
|:------------|:--------|:---------|
| `JDK`       | >= 1.8  | Need     |
| `Maven`     | >= 3.5  | Optional |

#### Source code preparation

Fork the code in the code warehouse and clone the code to the local, enter the source code directory

```bash
git clone git@github.com:<GitHubUser>/datacap.git
```

#### Load source code

Open idea to load the project

![img.png](../assets/developer_guide/img.png)

After opening the project, the right menu displays the project directory

![img.png](../assets/developer_guide/img_1.png)

- `configure` Some configurations used by the project
- `dist` Binary file storage path after the project is packaged
- `docs` Project Documentation Source Code
- `plugin` Project plug-in source code
- `server` Project main service source code
- `spi` Project plug-in integration core source code
- `web` Project web front-end source code

#### Service start

Service startup needs to modify the specified configuration file directory

![img.png](../assets/developer_guide/img_2.png)

Add `--spring.config.location=` configuration in `Program arguments`

The content of the configuration file is the current project startup service configuration, and the configuration source code is in `server/src/main/etc/conf`

After the service is configured, it can be started. After the service is started, access `http://localhost:9096/`

### Web

---

!!! warning

    Unnecessary problems are not avoided, please be sure to read the dependent environment configuration

#### Depends on the environment

| Environment | Version  | Required |
|:------------|:---------|:---------|
| `Node`      | >= v16.x | Need     |
| `Npm`       | >= 7.x   | Need     |

`console-fe` The web front-end source code is in this directory

#### Service start

- Go to the source code directory

```bash
cd web/console-fe
```

- Start service

```bash
yarn run dev
```

After the command is executed, the source code will be compiled, and something similar to the following will appear after compilation

```java
...
Use /* eslint-disable */ to ignore all warnings in a file.

App running at:
- Local:   http://localhost:8080/ 
- Network: http://192.168.32.53:8080/

Note that the development build is not optimized.
To create a production build, run yarn build.

No issues found.
```

Access `http://localhost:8080` through a browser to debug the source code, do not use the address returned by `Network`

### Docs

---

!!! warning

    Unnecessary problems are not avoided, please be sure to read the dependent environment configuration

#### Depends on the environment

| Environment | Version  | Required |
|:------------|:---------|:---------|
| `mkdocs`    | >= 1.3.0 | Need     |

`docs/docs` Document source code is in this directory

#### Service start

- Go to the source code directory

```bash
cd docs
```

- Start service

```bash
mkdocs serve --dev-addr=0.0.0.0:8001
```

After the command is executed, the source code will be compiled, and something similar to the following will appear after compilation

```java
...
INFO     -  Documentation built in 1.07 seconds
INFO     -  [13:16:03] Watching paths for changes: 'docs', 'mkdocs.yml'
INFO     -  [13:16:03] Serving on http://0.0.0.0:8001/
INFO     -  [13:16:03] Browser connected: http://0.0.0.0:8001/developer_guide/env.html
```

Access `http://0.0.0.0:8001` through a browser to debug the source code
