---
title: Deploying in a Docker container
---

The DataCap project provides the [qianmoq/datacap](https://hub.docker.com/r/qianmoq/datacap) Docker image that includes the DataCap server and a default configuration. The Docker image is published to Docker Hub and can be used with the Docker runtime, among several others.

### Running the container

To run DataCap in Docker, you must have the Docker engine installed on your machine. You can download Docker from the [Docker website](Docker website), or use the packaging system of your operating systems.

Use the docker command to create a container from the [qianmoq/datacap](https://hub.docker.com/r/qianmoq/datacap) image. Assign it the datacap name, to make it easier to reference it later. Run it in the background, and map the default DataCap port, which is `9096`, from inside the container to port `9096` on your workstation.

```bash
docker run -d -p 9909:9096 --name datacap qianmoq/datacap
```

Without specifying the container image tag, it defaults to `latest`, but a number of any released DataCap version can be used, for example `qianmoq/datacap:1.17.0`.

!!! danger

    If it is a new version, H2 database is no longer supported, and you need to hang it in the external MySQL configuration, and the service startup method can be modified to:

    ```bash
    docker run -d -p 9096:9096 -v /root/application.properties:/opt/app/datacap/configure/application.properties --name datacap qianmoq/datacap
    ```

    Assuming your configuration file is in `/root/application.properties`, please specify an absolute path if you need other paths.

Run `docker ps` to see all the containers running in the background.

```bash
-> % docker ps
CONTAINER ID   IMAGE                    COMMAND               CREATED      STATUS          PORTS                    NAMES
2096fba19e2a   datacap:latest           "sh ./bin/debug.sh"   5 days ago   Up 14 seconds   0.0.0.0:9909->9096/tcp   datacap
```

### Cleaning up

You can stop and start the container, using the `docker stop datacap` and `docker start datacap` commands. To fully remove the stopped container, run `docker rm datacap`.
