DataCap is a software for data transformation, integration and visualization.

#### System Requirements

---

!!! warning

    The binary package of the software is compiled and tested based on the following systems. It has not been tested on other versions, and it is theoretically supported.

    If there is an unsupported system, use the source code compilation method to actively compile the binary file.

| System | Version   |
|--------|-----------|
| JDK    | `>=11`    |
| MySQL  | `>=5.6.x` |

#### Binary install

---

!!! note

    Download the binary software package of the corresponding system from the following address for installation.

- [Download Release](/download.html)

##### Download package

Run the following command after downloading the binary to your local

```bash
tar -xvzf datacap-<VERSION>-release.tar.gz
```

`VERSION` After referring to the downloaded binary file version

##### Configuration software

!!! warning

    If MySQL is not configured for the first installation, the h2 built-in database will be used by default.

For the first installation of the software, you need to import the sql scripts in the `schema/datacap.sql` file to the MySQL server. Note that the scripts that need to be imported are matched according to the downloaded software package

After importing the `SQL` script, modify the `configure/application.properties` configuration file to modify the configuration information of the MySQL server

If MySQL, Remove configuration

```bash
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:datacap
spring.datasource.username=h2
spring.datasource.password=h2
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

Add configuration

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/datacap?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&useSSL=false&useOldAliasMetadataBehavior=true&jdbcCompliantTruncation=false&sessionVariables=sql_mode='STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION,PIPES_AS_CONCAT'
spring.datasource.username=root
spring.datasource.password=12345678
```

!!! warning

    If you need to modify the log configuration, just modify the `configure/logback.xml` configuration file

##### Start service

DataCap service startup is very simple, execute the following script

```bash
./bin/startup.sh
```

##### Stop service

Stop the service and execute the following script

```bash
./bin/shutdown.sh
```

!!! note

    If you want to debug the system, you can use `./bin/debug.sh` to start the service, but it will stop when you close the window

#### Source installation

---

!!! warning

    To manually compile and install DataCap, you need to perform the following steps.

    The system needs to install `JDK`

- Clone the source code to this machine

```bash
git clone https://github.com/EdurtIO/datacap.git
```

- Compile and build the application

```bash
./mvnw clean install package -DskipTests
```

!!! warning

    After compiling, the `datacap-release.tar.gz` package will be generated in the `dist` directory.

    Use the relevant packages to install it.

!!! note

    If you do not want to install to the local software directory, you can use the following documents to start the development mode for software use.

[Developer](../../developer_guide/env.md)
