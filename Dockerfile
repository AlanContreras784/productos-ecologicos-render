# ============================================================
# ETAPA 1: Compilación de la aplicación
# ============================================================

FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copiamos los archivos necesarios para Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Damos permisos de ejecución al Maven Wrapper
RUN chmod +x mvnw

# Descargamos dependencias y compilamos
RUN ./mvnw clean package -DskipTests


# ============================================================
# ETAPA 2: Ejecución de Spring Boot
# ============================================================

FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos el JAR generado durante la etapa de compilación
COPY --from=build /app/target/productos-ecologicos-0.0.1-SNAPSHOT.jar app.jar

# Render proporciona el puerto mediante la variable PORT
EXPOSE 8080

# Iniciamos la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]