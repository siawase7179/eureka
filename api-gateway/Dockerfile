FROM openjdk:17-alpine
VOLUME /tmp
ADD build/libs/api-gateway-0.0.1-SNAPSHOT.jar /
ENTRYPOINT exec java $JAVA_OPTS -jar api-gateway-0.0.1-SNAPSHOT.jar 
