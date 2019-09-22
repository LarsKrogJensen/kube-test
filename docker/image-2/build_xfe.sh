#!/bin/sh
docker build -f ./Dockerfile.xfe -t larskj/xfe:v8 ../..

docker push larskj/xfe:v8
# image size 46 MB
