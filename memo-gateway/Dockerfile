FROM openjdk:21
WORKDIR /app
ADD target/memo-gateway-0.0.1-SNAPSHOT.jar .
ENV SPRING_AMQP_DESERIALIZATION_TRUST_ALL=true
ENV TZ=Asia/Shanghai
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/memo-gateway-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]