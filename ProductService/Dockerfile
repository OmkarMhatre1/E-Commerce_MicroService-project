FROM openjdk:17

EXPOSE 8080

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} prodcutservice.jar

ENTRYPOINT ["java","-jar","/prodcutservice.jar"]