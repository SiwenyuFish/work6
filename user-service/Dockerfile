FROM openjdk:21
WORKDIR /app
ADD target/user-service-0.0.1-SNAPSHOT.jar .
ENV SPRING_AMQP_DESERIALIZATION_TRUST_ALL=true
ENV TZ=Asia/Shanghai
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/user-service-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]