FROM openjdk:17-jdk-alpine
COPY target/Chat_uService-0.0.1-SNAPSHOT.jar energy-ms-chat-us.jar

ENTRYPOINT ["java", "-jar", "energy-ms-chat-us.jar"]
