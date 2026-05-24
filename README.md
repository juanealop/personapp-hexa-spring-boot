# personapp-hexa-spring-boot

Plantilla de laboratorio para Arquitectura Limpia (Hexagonal) con Repository y Service.

## Stack

- JDK 11
- Spring Boot
- MariaDB y MongoDB
- Adaptadores de entrada REST y CLI
- Swagger/OpenAPI 3

## Requisitos

- Docker y Docker Compose
- JDK 11
- Maven 3.8+

## Levantar MariaDB y MongoDB con Compose

Este repositorio incluye [docker-compose.yml](docker-compose.yml) con:

- MariaDB en puerto `3307`
- MongoDB en puerto `27017`
- Inicializacion automatica con scripts del directorio [scripts](scripts)

Comandos:

```bash
docker compose up -d
docker compose ps
```

Para reinicializar desde cero (incluyendo datos):

```bash
docker compose down -v
docker compose up -d
```

## Ejecucion de scripts de base de datos

Los scripts se ejecutan automaticamente al primer arranque de cada contenedor:

- MariaDB DDL: [scripts/persona_ddl_maria.sql](scripts/persona_ddl_maria.sql)
- MariaDB DML: [scripts/persona_dml_maria.sql](scripts/persona_dml_maria.sql)
- MongoDB DDL: [scripts/persona_ddl_mongo.js](scripts/persona_ddl_mongo.js)
- MongoDB DML: [scripts/persona_dml_mongo.js](scripts/persona_dml_mongo.js)

## Ejecutar adaptadores de entrada

Son dos `SpringApplication` diferentes:

- REST: [rest-input-adapter/src/main/java/co/edu/javeriana/as/personapp/PersonAppRestApi.java](rest-input-adapter/src/main/java/co/edu/javeriana/as/personapp/PersonAppRestApi.java)
- CLI: [cli-input-adapter/src/main/java/co/edu/javeriana/as/personapp/PersonAppCli.java](cli-input-adapter/src/main/java/co/edu/javeriana/as/personapp/PersonAppCli.java)

### REST

```bash
mvn -pl rest-input-adapter spring-boot:run
```

- API en `http://localhost:3000`
- Swagger UI en `http://localhost:3000/swagger-ui.html`

### CLI

```bash
mvn -pl cli-input-adapter spring-boot:run
```

## Endpoints CRUD de Persona

Base path: `/api/v1/persona`

- `GET /api/v1/persona/{database}` lista personas por base (`MARIA` o `MONGO`)
- `GET /api/v1/persona/{database}/{dni}` consulta una persona
- `POST /api/v1/persona` crea persona
- `PUT /api/v1/persona/{dni}` edita persona
- `DELETE /api/v1/persona/{database}/{dni}` elimina persona

Ejemplo JSON para `POST`/`PUT`:

```json
{
	"dni": "123456700",
	"firstName": "Ana",
	"lastName": "Gomez",
	"age": "25",
	"sex": "F",
	"database": "MARIA"
}
```

## Lombok

Debes tener Lombok configurado en el IDE para evitar errores de compilacion en edicion.

## Nota de uso del repositorio

Se recomienda hacer Fork de este repositorio para trabajar en tu propia copia.
