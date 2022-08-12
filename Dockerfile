FROM openjdk:11-jre
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} wanted-api.jar
ENTRYPOINT ["java","-jar","/wanted-api.jar"]