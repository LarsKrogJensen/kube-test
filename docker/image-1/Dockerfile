#FROM openjdk:11-jre-slim
FROM adoptopenjdk/openjdk12-openj9:alpine-slim

RUN mkdir /opt/app
COPY xfe/target/xfe-1.0.0-SNAPSHOT-fat.jar /opt/app/xfe.jar
EXPOSE 8887
EXPOSE 8888
EXPOSE 8889
CMD ["java", "-XX:MaxRAMPercentage=75", "-jar", "/opt/app/xfe.jar"]
