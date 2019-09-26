#!/bin/sh
docker build -f ./Dockerfile.xfe -t larskj/xfe:v11 ../..

docker push larskj/xfe:v11
# image size 46 MB
