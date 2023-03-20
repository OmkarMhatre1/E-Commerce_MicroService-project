FROM openjdk:17

EXPOSE 8081

ADD target/paymentservice.jar paymentservice.jar

ENTRYPOINT ["java", "-jar", "paymentservice.jar"]