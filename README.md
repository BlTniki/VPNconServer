# vpnconserver
## About RU
Это Spring boot REST приложение служит обслуживанию и координации клиентов и [VPN сервисов]. Оно ==Бла бла бла==
## About EN
This Spring boot REST application serves to service and coordinate clients and [VPN services].

## Initialization RU
Все команды выполняются в корне проекта.
### Базовая инициализация
Клонируете проект.
Создаёте `application.properties` в `src\main\resources`
Пример `application.properties`:
```properties
server.port =  
  
spring.jpa.hibernate.ddl-auto=update  
spring.datasource.url=jdbc:mysql://url/to/db/VPNconServer
spring.datasource.username=
spring.datasource.password=  
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver  
  
jwt.header=Authorization  
jwt.secret="secretThatLongEnough"  
jwt.expiration=
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
И запускаем его:
```
docker run -it -e SERVER_PORT= -p exPort:SERVER_PORT -e DB_PORT= -e ROOT_NAME= -e ROOT_PASSWORD= -e TOKEN_SECRET_KEY= vpnconserver
```
Так же пример `docker compose`:
```yml
---
services:
  db:
    image: mysql
    command: --default-authentication-plugin=mysql_native_password
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: ""
      MYSQL_DATABASE: ""
    ports:
      - 
    networks:
      - springmysql-net
      
  vpncon:
    depends_on:
      - db
    image: bitniki/vpnconserver
    restart: unless-stopped
    environment:
      SERVER_PORT: 
      DB_PORT: 
      ROOT_NAME: 
      ROOT_PASSWORD: 
      TOKEN_SECRET_KEY:
    ports:
      - :SERVER_PORT
    networks:
      - springmysql-net
      
networks:
  springmysql-net:
```

Вы можете не компилировать образ самостоятельно, а взять его [отсюда](https://hub.docker.com/r/bitniki/vpnconserver) 

## Initialization EN
All commands are executed at the root of the project.
### Basic initialization
Clone the project.
Create `application.properties` in `src\main\resources`
Example of `application.properties`:
```properties
server.port =  
  
spring.jpa.hibernate.ddl-auto=update  
spring.datasource.url=jdbc:mysql://url/to/db/VPNconServer
spring.datasource.username=
spring.datasource.password=  
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver  
  
jwt.header=Authorization  
jwt.secret="secretThatLongEnough"  
jwt.expiration=
```
__mysql__, __java__ and __gradle__ must be installed.
In __mysql__ it is required to create a database with the name `VPNconServer`
Run the script `gradlew` (`./gradlew` in Linux).

### Building a Docker container
__java__ and __gradle__ must be installed.
Clone the project.
Compile the project:
```bash
./gradlew build -x test
```
`-x test` is used to ignore the tests, as they will give an error due to an un-configured connection to the database and the absence of `application.properties` (which we do not need to configure).
Next, we collect the image:
```bash
docker build -t vpnconserver .
```
And launch it:
```
docker ru -it -e SERVER_PORT= -p exPort:SERVER_PORT -e DB_PORT= -e ROOT_NAME= -e ROOT_PASSWORD= -e TOKEN_SECRET_KEY= vpnconserver
```
Also an example of `docker compose`:
```yml
---
services:
  db:
    image: mysql
    command: --default-authentication-plugin=mysql_native_password
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: ""
      MYSQL_DATABASE: ""
    ports:
      - 
    networks:
      - springmysql-net
      
  vpncon:
    depends_on:
      - db
    image: bitniki/vpnconserver
    restart: unless-stopped
    environment:
      SERVER_PORT: 
      DB_PORT: 
      ROOT_NAME: 
      ROOT_PASSWORD: 
      TOKEN_SECRET_KEY:
    ports:
      - :SERVER_PORT
    networks:
      - springmysql-net
      
networks:
  springmysql-net:
```

You can not compile the image yourself, but take it [from here](https://hub.docker.com/r/bitniki/vpnconserver )
