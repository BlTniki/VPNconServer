#!/bin/sh
mkdir BOOT-INF
mkdir BOOT-INF/classes
echo "server.port = ${SERVER_PORT}" >> BOOT-INF/classes/application.properties
echo "server.use-forward-headers=true" >> BOOT-INF/classes/application.properties
echo "server.forward-headers-strategy=native" >> BOOT-INF/classes/application.properties
echo "spring.jpa.hibernate.ddl-auto=update" >> BOOT-INF/classes/application.properties
echo "spring.datasource.url=jdbc:mysql://db:${DB_PORT}/VPNconServer?useSSL=false" >> BOOT-INF/classes/application.properties
echo "spring.datasource.username=${ROOT_NAME}" >> BOOT-INF/classes/application.properties
echo "spring.datasource.password=${ROOT_PASSWORD}" >> BOOT-INF/classes/application.properties
echo "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver" >> BOOT-INF/classes/application.properties
echo "jwt.header=Authorization" >> BOOT-INF/classes/application.properties
echo "jwt.secret=\"${TOKEN_SECRET_KEY}\"" >> BOOT-INF/classes/application.properties
echo "jwt.expiration=604800" >> BOOT-INF/classes/application.properties
echo "cors.allowedOrigins=${CORS_ALLOWED_ORIGINS}" >> BOOT-INF/classes/application.properties
echo "tg.user.password = ${TG_USER_PASSWORD}" >> BOOT-INF/classes/application.properties
echo "accountant.user.password = ${ACCOUNTANT_USER_PASSWORD}" >> BOOT-INF/classes/application.properties
echo "qiwi.secretKey = ${QIWI_SECRET_KEY}" >> BOOT-INF/classes/application.properties
echo "qiwi.publicKey = ${QIWI_PUBLIC_KEY}" >> BOOT-INF/classes/application.properties
jar uf vpncon.jar BOOT-INF/classes/application.properties
exec java -jar vpncon.jar