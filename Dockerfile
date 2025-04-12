# Используем образ с JDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем pom.xml и исходники проекта (если нужно)
COPY pom.xml /app
COPY src /app/src

# Собираем приложение в контейнере
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests

# Копируем собранный JAR файл
COPY target/*.jar app.jar

# Открываем порт для приложения
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
