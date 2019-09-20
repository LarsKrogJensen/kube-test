 !#/bin/sh
 docker build -f ./Dockerfile -t larskj/kube-test-2:v2 ../..

docker push larskj/kube-test-2:v2
# image size 46 MB
