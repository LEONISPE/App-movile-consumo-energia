Consumo de Servicios del Hogar (Flutter + Spring Boot)
📌 Descripción del Proyecto

Esta aplicación permite visualizar en tiempo real el consumo estimado de los tres servicios básicos de un hogar:

💧 Agua

⚡ Energía

🔥 Gas

El usuario puede ver:

Consumo actual del día

Consumo por hora

Costo total del dia 

Consumo acomulado

Costo generado por cada servicio

⚠️ Nota:
No se usa infraestructura real (medidores inteligentes).
Los cálculos se basan en el promedio de las últimas 12 facturas reales de cada hogar para estimar consumo mensual → diario → por hora.

El sistema está compuesto por:

Backend: API REST desarrollada en Spring Boot 3 + Java 21

Frontend móvil: Aplicación hecha en Flutter, consumiendo los servicios mediante HTTP

Base de datos: MySQL (local o en AWS RDS)

Despliegue real: Backend desplegado en AWS EC2 + RDS

Seguridad: Spring Security con JWT y roles (dueño / miembros del hogar)


🏗️ Arquitectura General
Flutter App  <──────────>  Spring Boot API  <─────────> MySQL
        HTTP                  (JWT Secured)               Local / RDS


Flutter muestra las vistas y consume los endpoints.

Spring Boot procesa reglas, cálculos y seguridad.

MySQL guarda usuarios, roles, hogares, servicios y consumos.



Despliegue real: Backend desplegado en AWS EC2 + RDS
🚀 Tecnologías Utilizadas
Backend (Spring Boot)

Java 21

Spring Boot 3

Spring Data JPA

Spring Web

Spring Validation

Spring Security + JWT

MySQL Driver

Lombok

Dependencias principales

Seguridad basada en JWT

Roles y permisos (dueño / miembro)

API REST para manejo de consumos y hogares

Frontend (Flutter)

Widgets nativos de Flutter

HTTP (para consumir la API)

Animaciones (AnimationController, etc.)

Organización por:

/services → consumo de endpoints
/views → pantallas (Login, Home, Consumo, etc.)


⚙️ Cómo correr el proyecto de forma local
🖥️ 1. Requisitos previos
Backend

Tener instalado Java JDK 21

Tener instalado MySQL local

Tener configurado Maven (opcional si usas Spring Tools o IntelliJ)

Flutter

Tener instalado Flutter SDK

Tener un emulador o un dispositivo conectado

🗄️ 2. Configurar Backend (Spring Boot)
🔧 Paso 1: Crear una base de datos local
CREATE DATABASE appmovile;

🔧 Paso 2: Configurar application.properties

Usa estas variables de entorno o cámbialas directamente:

spring.datasource.url=jdbc:mysql://localhost:3306/appmovile
spring.datasource.username=<TU_USUARIO_LOCAL>
spring.datasource.password=<TU_PASSWORD_LOCAL>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=none
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

api.infra.security.secret=<SECRET_JWT>

server.address=0.0.0.0
server.port=8080

spring.task.scheduling.time-zone=America/Bogota


🔧 Paso 3: Ejecutar el backend

En consola:

mvn clean install
mvn spring-boot:run


O desde tu IDE con Run Application.

Backend corriendo en:

http://localhost:8080

📱 3. Configurar y correr Flutter
🔧 Paso 1: Instalar dependencias

Desde /flutter-app/:

flutter pub get

🔧 Paso 2: Configurar la URL base

En tu archivo de configuración:

const String baseUrl = "http://localhost:8080";


Si corres en emulador Android, usa:
http://10.0.2.2:8080

🔧 Paso 3: Ejecutar la app

Moverte al proyecto Flutter y ejecutar:

flutter run

Ejecutar en navegador (Flutter Web)
flutter run -d chrome

☁️ Cómo desplegar el backend en AWS

Este proyecto fue desplegado usando:

AWS EC2 (instancia para ejecutar el backend)

AWS RDS (base de datos MySQL en la nube)

🔧 1. Crear instancia EC2

Ubuntu 22.04 recomendado

Abrir puerto 8080 en el Security Group

Instalar Java 21

sudo apt update
sudo apt install openjdk-21-jdk

🔧 2. Crear instancia RDS MySQL

Crear base de datos appmovile

Obtener endpoint del RDS

🔧 3. Modificar application.properties

Ejemplo:

spring.datasource.url=jdbc:mysql://<ENDPOINT-RDS>:3306/appmovile
spring.datasource.username=admin
spring.datasource.password=<PASSWORD_RDS>

🔧 4. Empaquetar el backend (JAR)
mvn clean package


Se genera:

target/appmovile-0.0.1-SNAPSHOT.jar

🔧 5. Subir el JAR a tu EC2

Por SCP o WinSCP.

🔧 6. Ejecutar el JAR en EC2
java -jar appmovile-0.0.1-SNAPSHOT.jar

📱 Configurar Flutter para despliegue móvil

Cuando el backend esté en AWS, coloca esta URL:

const String baseUrl = "http://<IP_PUBLICA_EC2>:8080";


Luego generar APK:

flutter build apk


Instalarla en el dispositivo → la app quedará completamente funcional.

📦 Estructura del Proyecto
/backend-springboot
   ├── src/main/java
   ├── src/main/resources
   └── pom.xml

/flutter-app
   ├── lib
   │    ├── views
   │    ├── services
   │    └── utils
   ├── assets
   └── pubspec.yaml

🔐 Seguridad y Roles

El backend maneja JWT con dos roles:

ROLE_OWNER → dueño del hogar (solo él puede autorizar miembros)

ROLE_MEMBER → miembros que solo pueden ver consumos

El login genera un token JWT que Flutter almacena y envía en cada request.

✅ Estado del Proyecto

✔ Backend funcional
✔ Seguridad con JWT
✔ Flutter consumiendo endpoints
✔ Despliegue real en AWS
✔ Cálculo de consumo por hora / día / mes
✔ Aplicación móvil operativa

🙌 Autor

Leo — Backend & Mobile Developer
