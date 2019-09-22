#!/bin/sh
docker build -f ./Dockerfile.xfe -t larskj/xfe:v4 ../..

docker push larskj/xfe:v4
# image size 46 MB
