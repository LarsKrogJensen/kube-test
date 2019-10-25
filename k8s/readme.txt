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


docker registry
curl -X GET http://localhost:5000/v2/_catalog
curl -X GET http://localhost:5000/v2/xfe/tags/list

docker-ls tags larskj/xfe -r http://localhost:5000
docker-ls repositories -r http://localhost:5000

# intermedeiate images
docker images -f “dangling=true" -q
clean
docker rmi $(docker images --filter "dangling=true" -q)


tag after build
docker tag larskj/xfe:v11 localhost:5000/xfe:v11
docker push localhost:5000/xfe:v11


config map from file:
k create configmap xbe-configmap --from-file arne.properties --dry-run --output yaml

## explore
local docker registry
local docker registry in minikube failed!!!

port forward
k port-forward svc/xfe1-service 9090:8080

dns lookup inside cluster (exec into pod and execute)
k exec -it xfe1-5c459b5f5-qw66j nslookup xfe1-service.default.svc.cluster.local

rolling updates
OK pod probes   https://medium.com/@dstrimble/kubernetes-horizontal-pod-autoscaling-for-local-development-d211e52c309c
OK autoscaler HPA  https://medium.com/@dstrimble/kubernetes-horizontal-pod-autoscaling-for-local-development-d211e52c309c
service discovery
hazelcast in pod
OK ingress TCP traffic service as NodePort cause ingress does not support tcp/udp

