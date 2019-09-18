FROM azul/zulu-openjdk-alpine:11 as packager

RUN { \
        java --version ; \
        echo "jlink version:" && \
        jlink --version ; \
    }

ENV JAVA_MINIMAL=/opt/jre

# build modules distribution
RUN jlink \
    --verbose \
    --add-modules \
        java.base,java.management,java.logging \
    --compress 2 \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --output "$JAVA_MINIMAL"

FROM alpine

ENV JAVA_MINIMAL=/opt/jre
ENV PATH="$PATH:$JAVA_MINIMAL/bin"

COPY --from=packager "$JAVA_MINIMAL" "$JAVA_MINIMAL"

COPY target/kube-test-1.0.0-SNAPSHOT-fat.jar /opt/app/kube-test.jar
EXPOSE 8888
CMD ["java", "-jar", "/opt/app/kube-test.jar"]

