FROM java:8
MAINTAINER qianmoQ "shicheng@ttxit.com"

# Add datacap
RUN mkdir -p /opt/app
ADD dist/datacap-release.tar.gz /opt/app/
WORKDIR /opt/app/datacap
CMD sh ./bin/startup.sh

EXPOSE 9096
