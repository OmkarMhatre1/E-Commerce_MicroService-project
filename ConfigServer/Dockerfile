FROM openjdk:17

EXPOSE 9296

ADD target/configserver.jar configserver.jar

ENTRYPOINT ["java","-jar","configserver.jar"]
