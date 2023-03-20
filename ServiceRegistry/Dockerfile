FROM openjdk:17

EXPOSE 8761

ADD target/serviceregistry.jar serviceregistry.jar

ENTRYPOINT ["java","-jar","serviceregistry.jar"]
