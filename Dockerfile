FROM amazoncorretto:21

RUN mkdir /app
COPY web-backend.jar /app

ENTRYPOINT ["java", "-jar", "/app/web-backend.jar"]
