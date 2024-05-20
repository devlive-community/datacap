FROM eclipse-temurin:11-jdk-focal
MAINTAINER qianmoQ "shicheng@devlive.org"

LABEL org.opencontainers.image.description DESCRIPTION

# Add datacap
RUN mkdir -p /opt/app
ADD dist/datacap-release.tar.gz /opt/app/
WORKDIR /opt/app/datacap

# expose our default runtime port
EXPOSE 9096

# run it
ENTRYPOINT ["sh", "./bin/debug.sh"]
