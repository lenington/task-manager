# Task Manager

Aplicação full stack, focada em backend, em Spring Boot para gerenciamento de tarefas, criada para o teste técnico de desenvolvimento e análise de incidentes.

## Funcionalidades

- Criar, listar, editar e remover tarefas.
- Filtrar tarefas por status e prioridade.
- Validações no front-end e no back-end.
- Persistência com JPA, Flyway e banco H2 em memória por padrão.
- Logs mínimos para criação, busca, atualização e remoção.
- Documentação automática da API com Swagger UI.
- Testes unitários e de integração.

## Como executar

Pré-requisitos:

- Java 8 ou superior.
- Maven 3.6 ou superior.

Execute:

```bash
mvn spring-boot:run
```

Acesse:

- Front-end: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- H2 Console: `http://localhost:8081/h2-console`

Dados do H2 Console:

- JDBC URL: `jdbc:h2:mem:taskmanager`
- User: `sa`
- Password: vazio

## Executar com MySQL

Crie ou disponibilize uma instância MySQL local e ajuste `src/main/resources/application-mysql.properties` se necessário.

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Para MySQL 5.5 legado, use o perfil `mysql55`. Ele desativa o Flyway porque a versão usada no projeto não suporta MySQL 5.5:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql55
```

## Testes

```bash
mvn test
```

Os testes cobrem:

- Regras principais do `TaskService`.
- Fluxo de API com criação, filtro, atualização, remoção e busca inexistente.
- Validações obrigatórias do payload.

## Endpoints da API

Base URL: `/api/tasks`

### Listar tarefas

```http
GET /api/tasks
GET /api/tasks?status=TODO&priority=HIGH
```

Parâmetros opcionais:

- `status`: `TODO`, `IN_PROGRESS`, `DONE`
- `priority`: `LOW`, `MEDIUM`, `HIGH`

### Buscar por id

```http
GET /api/tasks/{id}
```

Resposta `404` quando a tarefa não existe.

### Criar tarefa

```http
POST /api/tasks
Content-Type: application/json

{
  "title": "Analisar logs do incidente",
  "description": "Consolidar hipótese e ação corretiva",
  "status": "TODO",
  "priority": "HIGH"
}
```

Resposta `201 Created`.

### Atualizar tarefa

```http
PUT /api/tasks/{id}
Content-Type: application/json

{
  "title": "Analisar logs do incidente",
  "description": "Correção validada em homologação",
  "status": "DONE",
  "priority": "MEDIUM"
}
```

### Remover tarefa

```http
DELETE /api/tasks/{id}
```

Resposta `204 No Content`.

## Modelo de tarefa

```json
{
  "id": 1,
  "title": "Analisar logs do incidente",
  "description": "Consolidar hipótese e ação corretiva",
  "status": "TODO",
  "priority": "HIGH",
  "createdAt": "2026-06-09T10:00:00",
  "updatedAt": "2026-06-09T10:00:00"
}
```

## Estrutura

- `src/main/java/com/taskmanager`: API, services, model (entity), DTOs e tratamento de exceções.
- `src/main/resources/db/migration`: migrations.
- `src/main/resources/static`: frontend estático.
- `src/test/java/com/taskmanager`: testes unitários e de integração.
- `TECHNICAL_NOTE.md`: decisões técnicas, 'trade-offs', melhorias futuras e análise de incidente.
