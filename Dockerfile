FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B package -DskipTests

FROM tomcat:9.0-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/ROOT \
    /usr/local/tomcat/webapps/ROOT.war
COPY --from=build /app/target/campuscycle.war /usr/local/tomcat/webapps/ROOT.war

# Render's default web-service port is 10000. PORT can be overridden in the service settings.
ENV PORT=10000
EXPOSE 10000

CMD ["sh", "-c", "sed -i -e \"s/port=\\\"8005\\\"/port=\\\"-1\\\"/\" -e \"s/port=\\\"8080\\\"/port=\\\"${PORT}\\\"/\" /usr/local/tomcat/conf/server.xml && catalina.sh run"]
