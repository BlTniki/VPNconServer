#!/bin/sh
echo "server.port = ${SERVER_PORT}" >> src/main/resources/application.properties
echo "spring.jpa.hibernate.ddl-auto=update" >> src/main/resources/application.properties
echo "spring.datasource.url=jdbc:mysql://db:${DB_PORT}/VPNconServer?useSSL=false" >> src/main/resources/application.properties
echo "spring.datasource.username=${ROOT_NAME}" >> src/main/resources/application.properties
echo "spring.datasource.password=${ROOT_PASSWORD}" >> src/main/resources/application.properties
echo "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver" >> src/main/resources/application.properties
echo "jwt.header=Authorization" >> src/main/resources/application.properties
echo "jwt.secret=\"${TOKEN_SECRET_KEY}\"" >> src/main/resources/application.properties
echo "jwt.expiration=604800" >> src/main/resources/application.properties
chmod 777 -R .
./gradlew build -x test
exec java -jar build/libs/VPNconServer*SNAPSHOT.jar