FROM openjdk:21
WORKDIR /app
ADD target/transaction-service-0.0.1-SNAPSHOT.jar .
ENV SPRING_AMQP_DESERIALIZATION_TRUST_ALL=true
ENV TZ=Asia/Shanghai
ENTRYPOINT ["java","-jar","/app/transaction-service-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]