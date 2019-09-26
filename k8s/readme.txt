login to minikube
minikube status to get ip
ssh docker@ip pwd tcuser


local docker images
https://stackoverflow.com/questions/42564058/how-to-use-local-docker-images-with-minikube
eval $(minikube docker-env)

build image and mk will see it use never as pullPolicy


tag to push locally
docker tag larskj/xfe:v3 localhost:5000/larskj/xfe:v3
docker push localhost:5000/larskj/xfe:v3


java mem on container
https://medium.com/adorsys/jvm-memory-settings-in-a-container-environment-64b0840e1d9e
java -XX:+PrintFlagsFinal -version | grep -Ei "maxheapsize|maxram"


## explore
local docker registry
local docker registry in minikube failed!!!

rolling updates
OK pod probes   https://medium.com/@dstrimble/kubernetes-horizontal-pod-autoscaling-for-local-development-d211e52c309c
OK autoscaler HPA  https://medium.com/@dstrimble/kubernetes-horizontal-pod-autoscaling-for-local-development-d211e52c309c
service discovery
hazelcast in pod
OK ingress TCP traffic service as NodePort cause ingress does not support tcp/udp

