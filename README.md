## About RU
Это Spring boot REST приложение служит обслуживанию создания туннелей до серверов с помощью [VPNconHost](https://github.com/BlTniki/vpnconhost). В качестве клиента выступает [vpncon-tg-bot](https://github.com/BlTniki/vpncon-tg-client/tree/main).

Проект представляет собой приложение для создания VPN-туннелей с использованием технологии WireGuard, а также предоставляет RESTful серверную часть на Java и клиентскую часть в виде телеграм-бота.

В рамках проекта я реализовал создание VPN-туннелей с использованием WireGuard. Для хранения данных я выбрал реляционную базу данных MySQL.

Для обеспечения безопасности и аутентификации пользователей, я реализовал механизм авторизации с использованием JWT-токенов.

Для удобного управления VPN-туннелями, я создал клиентскую часть в виде телеграм-бота, который позволяет пользователям управлять своими VPN-соединениями через популярный мессенджер Telegram.

Для развертывания приложения на сервере я использовал Docker и Nginx.

Дополнительно, я организовал распределение модулей приложения на различные серверы, где основной модуль является Spring-приложением на Java, а модуль создания туннелей реализован на Python с использованием фреймворка Flask.


## Initialization RU
Все команды выполняются в корне проекта.
### Базовая инициализация
Клонируете проект.
Создаёте `application.properties` в `src\main\resources`.
Пример `application.properties`:
```properties
server.url = # Server domain
server.port =  
  
spring.jpa.hibernate.ddl-auto=update  
spring.datasource.url=jdbc:mysql://url/to/db/VPNconServer
spring.datasource.username=
spring.datasource.password=  
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver  
  
jwt.header=Authorization  
jwt.secret="secretThatLongEnough"  
jwt.expiration=

cors.allowedOrigins= # Might be null

tg.user.password = # Password for VPNcon Bot 
accountant.user.password =  
qiwi.secretKey = # P2P Qiwi secret key
qiwi.publicKey = # P2P Qiwi public key
```
Должен быть установлен __mysql__, __java__ и __gradle__.
В __mysql__ требуется создать базу с названием `VPNconServer`
Запускаете скрипт `gradlew` (`./gradlew` в Linux).

### Сборка Docker контейнера
Должен быть установлен __java__ и __gradle__.
Клонируете проект.
Скомпилируйте проект:
```bash
./gradlew build -x test
```
`-x test` используется для игнорирования тестов, так как они дадут ошибку из-за не настроенного подключения к базе данных и отсутствия `application.properties` (что нам и необязательно настраивать).
Далее собираем образ:
```bash
docker build -t vpnconserver .
```

Вы можете не компилировать проект самостоятельно, а взять его [отсюда](https://hub.docker.com/r/bitniki/vpnconserver)
