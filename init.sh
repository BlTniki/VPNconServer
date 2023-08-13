#!/bin/sh
mkdir BOOT-INF
mkdir BOOT-INF/classes
echo "
server:
  url: ${SERVER_URL}
  port: ${SERVER_PORT}
  use-forward-headers: true
  forward-headers-strategy: native

spring:
  jpa:
    hibernate:
      ddl-auto: validate
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQL10Dialect
  datasource:
    url: jdbc:postgresql://db:${DB_PORT}/VPNconServer?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: ${ROOT_NAME}
    password: ${ROOT_PASSWORD}
    driver-class-name: org.postgresql.Driver
  flyway:
    enabled: true
    url: jdbc:postgresql://db:${DB_PORT}/VPNconServer?useUnicode=true&characterEncoding=utf-8&useSSL=false
    user: ${ROOT_NAME}
    password: ${ROOT_PASSWORD}
    locations: classpath:db/migration

jwt:
  header: Authorization
  secret: \"${TOKEN_SECRET_KEY}\"
  expiration: 604800

cors:
  allowedOrigins: ${CORS_ALLOWED_ORIGINS}

tg:
  user:
    password: ${TG_USER_PASSWORD}
accountant:
  user:
    password: ${ACCOUNTANT_USER_PASSWORD}

logging:
  level:
    org.springframework: INFO
  file:
    path: ./logs

payment:
  uuidEncryption:
    secretKey: ${UUID_SECRET_KEY}

yoomoney:
  secretKey: ${YOO_SECRET_KEY}
  account: ${YOO_ACCOUNT}
  successUrl: ${YOO_SUCCESS_URL}
" > BOOT-INF/classes/application.yml
jar uf vpncon.jar BOOT-INF/classes/application.yml
exec java -jar vpncon.jar