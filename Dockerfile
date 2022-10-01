FROM java:8
MAINTAINER qianmoQ "shicheng@ttxit.com"

# Add datacap
RUN mkdir -p /opt/app
COPY dist/datacap-release.tar.gz /opt/app/datacap-release.tar.gz
RUN tar -xvzf /opt/app/datacap-release.tar.gz -C /opt/app/
WORKDIR /opt/app/datacap
CMD sh ./bin/startup.sh

EXPOSE 9096
