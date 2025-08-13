FROM amazoncorretto:21

RUN mkdir /app
COPY build/libs/web-backend-1.0.0.jar /app

ENTRYPOINT ["java", "-jar", "/app/web-backend-1.0.0.jar"]