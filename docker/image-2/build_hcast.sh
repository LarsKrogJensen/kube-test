#!/bin/bash
TAG=v1
#eval "$(minikube docker-env)"
docker build -f ./Dockerfile.hcast -t larskj/hcast:$TAG ../..
#docker tag larskj/xfe:$TAG localhost:5000/larskj/xfe:$TAG
#docker push localhost:5000/larskj/xfe:$TAG

# easier way is to talk to minikube docker host and build image local to mk
# eval $(minikube docker-env)
#docker push larskj/xfe:v11
# image size 46 MB
