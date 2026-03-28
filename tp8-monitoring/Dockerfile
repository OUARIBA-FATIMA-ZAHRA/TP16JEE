FROM openjdk:17-alpine
WORKDIR /app
COPY target/tp8-monitoring-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]