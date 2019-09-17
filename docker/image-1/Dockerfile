FROM openjdk:11-jre-slim
RUN mkdir /opt/app
COPY target/kube-test-1.0.0-SNAPSHOT-fat.jar /opt/app/kube-test.jar
EXPOSE 8888
CMD ["java", "-jar", "/opt/app/kube-test.jar"]
