#!/bin/sh
docker build -f ./Dockerfile.xfe -t larskj/xfe:v7 ../..

docker push larskj/xfe:v7
# image size 46 MB
