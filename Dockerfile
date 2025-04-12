FROM openjdk:17-jdk-slim
WORKDIR /app
COPY prod-vpn-app-jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
