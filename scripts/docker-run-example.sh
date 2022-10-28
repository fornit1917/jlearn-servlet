docker run \
 -e JDBC_DATABASE_URL='jdbc:postgresql://localhost:5432/bookt' \
 -e JDBC_DATABASE_USERNAME='bookt' \
 -e JDBC_DATABASE_PASSWORD='bookt12345' \
 -e PORT=5555 \
 --network host \
 -d \
 fornit1917/bookt:latest