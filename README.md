## About RU
Это Spring boot REST приложение служит обслуживанию создания туннелей до серверов с помощью [VPNconHost](https://github.com/BlTniki/vpnconhost). В качестве клиента выступает [vpncon-tg-bot](https://github.com/BlTniki/vpncon-tg-client/tree/main).

Проект представляет собой приложение для создания VPN-туннелей с использованием технологии WireGuard, а также предоставляет RESTful серверную часть на Java и клиентскую часть в виде телеграм-бота. Также проект поддерживает оплату через сервис пожертвований Yoomoney.

В рамках проекта я реализовал создание VPN-туннелей с использованием WireGuard.

Для обеспечения безопасности и аутентификации пользователей, я реализовал механизм авторизации с использованием JWT-токенов.

Для удобного управления VPN-туннелями, я создал клиентскую часть в виде телеграм-бота, который позволяет пользователям управлять своими VPN-соединениями через популярный мессенджер Telegram.



### Базовая инициализация
Создаёте `application.yml` в `src\main\resources`.

Пример `application.yml`:
```yml
server:
  url: http://...
  port: ...
  use-forward-headers: true
  forward-headers-strategy: native

spring:
  jpa:
    hibernate:
      ddl-auto: validate
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQL10Dialect
  datasource:
    url: jdbc:postgresql://...
    username: ...
    password: ...
    driver-class-name: org.postgresql.Driver
  flyway:
    enabled: true
    url: jdbc:postgresql://...
    user: ...
    password: ...
    locations: classpath:db/migration

jwt:
  header: Authorization
  secret: "..."
  expiration: ...

cors:
  allowedOrigins: ...

tg:
  user:
    password: ...
accountant:
  user:
    password: ...

logging:
  level:
    org.springframework: INFO
  file:
    path: ./logs

payment:
  uuidEncryption:
    secretKey: ...

yoomoney:
  secretKey: ...
  account: ...
  successUrl: ...
```
Собираете проект при помощи **maven**

Вы можете не компилировать проект самостоятельно, а взять его [отсюда](https://hub.docker.com/r/bitniki/vpnconserver)
