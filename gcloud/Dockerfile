FROM  google/cloud-sdk:alpine
MAINTAINER "Yamazaki Norihito"

WORKDIR /root/

WORKDIR /google_api/

COPY key.json key.json

RUN apk update  \
    && apk upgrade  \
    && gcloud components install kubectl \
    && apk add docker --no-cache

ENTRYPOINT bash