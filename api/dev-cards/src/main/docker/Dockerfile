# Utiliza una imagen base oficial de Java JDK
FROM openjdk:22-jdk

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR a la imagen Docker
COPY dev-cards-0.0.1-SNAPSHOT.jar /app/dev-cards.jar

# Expon el puerto en el que tu aplicación escucha (cámbialo según sea necesario)
EXPOSE 8080

# Comando para ejecutar tu aplicación
ENTRYPOINT ["java", "-jar", "/app/dev-cards.jar"]