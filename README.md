# Demo Park API

API REST desenvolvida em **Java 17** com **Spring Boot 3**, voltada para o gerenciamento de um estacionamento. O projeto utiliza **JWT** para autenticação, **Spring Security** para autorização baseada em perfis, **Spring Data JPA** para persistência, **Bean Validation** para validação de entradas e **OpenAPI/Swagger** para documentação da API.

## Tecnologias

- Java 17
- Spring Boot 3.4.4
- Spring Web
- Spring Security
- Spring Data JPA
- Spring Validation
- SpringDoc OpenAPI / Swagger UI
- JWT (JSON Web Token)
- Hibernate
- MySQL
- H2 Database
- Lombok
- Maven
- WebTestClient para testes de integração

## Funcionalidades

- Cadastro e autenticação de usuários
- Emissão de token JWT
- Cadastro e consulta de clientes
- Cadastro e consulta de vagas de estacionamento
- Check-in e check-out de veículos
- Consulta paginada de registros de estacionamento
- Geração de relatórios
- Controle de acesso por perfil de usuário

## Perfis de acesso

A aplicação trabalha com controle de acesso por role:

- **ADMIN**: acesso às operações administrativas
- **CUSTOMER**: acesso às funcionalidades do cliente

## Autenticação

A API usa autenticação via **Bearer Token**.

Fluxo básico:

1. Crie um usuário em `POST /api/v1/users`
2. Autentique em `POST /api/v1/auth`
3. Use o token retornado no header:

```http
Authorization: Bearer SEU_TOKEN_AQUI
```

## Principais endpoints

> A documentação interativa fica disponível via Swagger.

### Autenticação
- `POST /api/v1/auth`

### Usuários
- `POST /api/v1/users`
- `GET /api/v1/users/{id}`
- `GET /api/v1/users`
- `PUT /api/v1/users/{id}/password`

### Clientes
- `POST /api/v1/customers`
- `GET /api/v1/customers/{id}`
- `GET /api/v1/customers`
- `GET /api/v1/customers/details`

### Vagas
- `POST /api/v1/parking-spot`
- `GET /api/v1/parking-spot/{code}`

### Estacionamento
- `POST /api/v1/parking/checkIn`
- `POST /api/v1/parking/checkOut`
- `GET /api/v1/parking`
- `GET /api/v1/parking/{receipt}`
- `GET /api/v1/parking/report`

## Exemplos de requests e responses

### 1) Autenticação

**Request**

```bash
curl -X POST http://localhost:8080/api/v1/auth \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jose@gmail.com",
    "password": "123456"
  }'
```

**Response 200**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response 400**

```json
{
  "status": 400,
  "message": "Invalid credentials",
  "path": "/api/v1/auth",
  "method": "POST"
}
```

### 2) Criar usuário

**Request**

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ana@gmail.com",
    "password": "123456"
  }'
```

**Response 201**

```json
{
  "id": 101,
  "username": "ana@gmail.com",
  "role": "ROLE_CUSTOMER"
}
```

**Response 409**

```json
{
  "status": 409,
  "message": "User already exists",
  "path": "/api/v1/users",
  "method": "POST"
}
```

### 3) Consultar usuário por ID

**Request**

```bash
curl -X GET http://localhost:8080/api/v1/users/100 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Response 200**

```json
{
  "id": 100,
  "username": "jose@gmail.com",
  "role": "ROLE_ADMIN"
}
```

**Response 404**

```json
{
  "status": 404,
  "message": "User not found",
  "path": "/api/v1/users/0",
  "method": "GET"
}
```

### 4) Criar vaga de estacionamento

**Request**

```bash
curl -X POST http://localhost:8080/api/v1/parking-spot \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "A-10",
    "status": "FREE"
  }'
```

**Response 201**

Headers:

```http
Location: http://localhost:8080/api/v1/parking-spot/A-10
```

**Response 409**

```json
{
  "status": 409,
  "message": "Parking Spot with code A-01 has already been registered.",
  "path": "/api/v1/parking-spot",
  "method": "POST"
}
```

### 5) Criar cliente

**Request**

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "60639545033",
    "name": "Maria Silva"
  }'
```

**Response 200**

```json
{
  "id": 203,
  "cpf": "60639545033",
  "name": "Maria Silva"
}
```

### 6) Check-in no estacionamento

**Request**

```bash
curl -X POST http://localhost:8080/api/v1/parking/checkIn \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "carModel": "Vectra 2.0",
    "carBrand": "Chevrolet",
    "licensePlateNumber": "ABC-0000",
    "carColor": "WHITE",
    "customerCpf": "60639545033"
  }'
```

**Response 201**

```json
{
  "receipt": "20250507-131100",
  "entryDate": "2025-05-07T13:11:00",
  "parkingSpot": {
    "code": "A-01"
  },
  "customer": {
    "cpf": "60639545033"
  }
}
```

**Response 404**

```json
{
  "status": 404,
  "message": "Cpf or vacancy not found",
  "path": "/api/v1/parking/checkIn",
  "method": "POST"
}
```

### 7) Consultar check-in por receipt

**Request**

```bash
curl -X GET http://localhost:8080/api/v1/parking/checkIn/20250507-131100 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Response 200**

```json
{
  "receipt": "20250507-131100",
  "licensePlateNumber": "ABC-0000",
  "carModel": "Vectra 2.0",
  "carBrand": "Chevrolet",
  "carColor": "WHITE"
}
```

### 8) Check-out

**Request**

```bash
curl -X POST http://localhost:8080/api/v1/parking/checkOut \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "receipt": "20250507-131100"
  }'
```

**Response 200**

```json
{
  "receipt": "20250507-131100",
  "invoice": 25.0,
  "exitDate": "2025-05-07T15:45:00"
}
```

## Documentação da API

Após subir a aplicação, acesse:

- Swagger UI: `http://localhost:8080/docs-park.html`
- OpenAPI JSON: `http://localhost:8080/docs-park`

## Requisitos

- Java 17+
- Maven 3.8+
- MySQL

## Configuração do banco

Por padrão, o projeto está configurado para usar MySQL local com as credenciais abaixo em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost/demo_park_api?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
spring.datasource.username=developer
spring.datasource.password=1234
```

Se necessário, ajuste as credenciais e a URL do banco antes de executar o projeto.

## Como executar

### 1. Clonar o repositório

```bash
git clone https://github.com/BrunoPressi/demo-park-api.git
cd demo-park-api
```

### 2. Configurar o banco MySQL

Crie o banco `demo_park_api` e ajuste as credenciais, se necessário.

### 3. Executar a aplicação

```bash
mvn spring-boot:run
```

Ou gerar o JAR:

```bash
mvn clean package
java -jar target/demo-park-api-0.0.1-SNAPSHOT.jar
```

## Testes

Para executar os testes de integração:

```bash
mvn test
```

## Observações

- A aplicação usa JWT com expiração de 30 minutos.
- Endpoints protegidos exigem token válido no header `Authorization`.
- A documentação Swagger está liberada sem autenticação.

## Estrutura geral

- `src/main/java/com/compass/demo_park_api/config` — configurações gerais e segurança
- `src/main/java/com/compass/demo_park_api/jwt` — utilitários e filtros JWT
- `src/main/java/com/compass/demo_park_api/web/controller` — controladores REST
- `src/main/java/com/compass/demo_park_api/web/dto` — objetos de entrada e saída
- `src/main/java/com/compass/demo_park_api/service` — regras de negócio
- `src/main/java/com/compass/demo_park_api/entity` — entidades JPA

## Licença

Este projeto não possui licença definida no repositório.
