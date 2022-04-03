# https://hub.docker.com/_/node
FROM node:current-alpine3.14

RUN apk update && apk upgrade
RUN apk add bash

RUN npm install expo-cli --global

RUN expo --version

WORKDIR /src

EXPOSE 19002
EXPOSE 19000
EXPOSE 19001
