FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/config-server-0.0.1-SNAPSHOT.jar /
ENTRYPOINT exec java $JAVA_OPTS -jar config-server-0.0.1-SNAPSHOT.jar
