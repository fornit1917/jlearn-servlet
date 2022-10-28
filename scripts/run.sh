JDBC_DATABASE_URL='jdbc:postgresql://localhost:5432/bookt' \
JDBC_DATABASE_USERNAME='bookt' \
JDBC_DATABASE_PASSWORD='bookt12345' \
java -jar target/dependency/webapp-runner.jar --port 8080 target/*.war
