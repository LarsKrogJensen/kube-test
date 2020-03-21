FROM azul/zulu-openjdk-alpine:11 as packager

RUN { \
        java --version ; \
        echo "jlink version:" && \
        $JAVA_HOME/bin/jlink --version ; \
    }

ENV JAVA_MINIMAL=/opt/jre

# build modules distribution
RUN $JAVA_HOME/bin/jlink \
    --verbose \
    --add-modules \
        java.base,java.management,java.logging,java.xml,java.naming,jdk.naming.dns,java.transaction.xa,jdk.unsupported \
    --compress 2 \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --output "$JAVA_MINIMAL"

FROM alpine

ENV JAVA_MINIMAL=/opt/jre
ENV PATH="$PATH:$JAVA_MINIMAL/bin"

COPY --from=packager "$JAVA_MINIMAL" "$JAVA_MINIMAL"

COPY target/hcast-1.0.0-SNAPSHOT-fat.jar /opt/app/hcast.jar
EXPOSE 8887
EXPOSE 8888
EXPOSE 8889

CMD ["java", "-XX:MaxRAMPercentage=75", "-jar", "/opt/app/hcast.jar", "--add-modules", "java.se", "--add-exports", "java.base/jdk.internal.ref=ALL-UNNAMED", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "--add-opens",  "java.base/java.nio=ALL-UNNAMED", "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens", "java.management/sun.management=ALL-UNNAMED", "--add-opens", "jdk.management/com.sun.management.internal=ALL-UNNAMED"]
