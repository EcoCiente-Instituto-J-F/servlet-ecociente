# Etapa 1: compila o projeto e gera o WAR
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q package -DskipTests

# Etapa 2: roda o WAR no Tomcat
FROM tomcat:11.0-jdk25
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
ENV JAVA_OPTS="-Xmx300m"
EXPOSE 8080