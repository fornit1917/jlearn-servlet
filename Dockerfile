FROM maven:3.8.6-eclipse-temurin-11-alpine as build
WORKDIR /app
COPY . /app
RUN mvn -s settings-heroku.xml -DskipTests clean dependency:list install

FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=build /app/target /app/target
EXPOSE 8080
ENTRYPOINT java -jar /app/target/dependency/webapp-runner.jar --port $PORT /app/target/jlearn-servlet-1.0-SNAPSHOT.war