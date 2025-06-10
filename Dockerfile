
FROM maven:3.8.6-jdk-17 AS build
WORKDIR /build

# Копируем исходники
COPY pom.xml .
COPY src ./src

# Собираем проект
RUN mvn clean package

# Stage 2: Запуск приложения
FROM openjdk:17-jdk-slim
WORKDIR /app

# Копируем собранный JAR из stage 1
COPY --from=build /build/target/*.jar app.jar

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]