# Используем базовый образ с JDK 11
FROM openjdk:21

# Устанавливаем метаданные о приложении
LABEL maintainer="demo"
# Копируем файл JAR в контейнер
COPY target/*.jar app.jar

# Порт, на котором работает Spring Boot
EXPOSE 9000

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "/app.jar"]

