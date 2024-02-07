FROM openjdk:17-alpine
VOLUME /tmp
ADD build/libs/eureka-server-0.0.1-SNAPSHOT.jar /
ENTRYPOINT exec java $JAVA_OPTS -jar eureka-server-0.0.1-SNAPSHOT.jar
