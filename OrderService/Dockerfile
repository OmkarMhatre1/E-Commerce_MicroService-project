FROM openjdk:17

EXPOSE 8082

ADD target/orderservice.jar orderservice.jar

ENTRYPOINT ["java", "-jar", "orderservice.jar"]