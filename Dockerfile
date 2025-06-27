# Usa JDK 17 sobre Alpine Linux
FROM eclipse-temurin:17-jdk-alpine

# Crea un directorio de trabajo
WORKDIR /app

# Copia el JAR compilado
COPY target/*.jar app.jar

# Expone el puerto que tu microservicio usa (ajusta el puerto si no es 8081)
EXPOSE 8087

# Arranca Spring Boot con el profile docker
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]