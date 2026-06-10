# Task Manager

Aplicação full stack, focada em backend, em Spring Boot para gerenciamento de tarefas, criada para o teste técnico de desenvolvimento e análise de incidentes.

## Funcionalidades

- Criar, listar, editar e remover tarefas.
- Filtrar tarefas por status e prioridade.
- Validações no front-end e no back-end.
- Persistência com JPA, Flyway e H2 (padrão, em memória) / MySQL
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

| Recurso | URL |
|---------|-----|
| Frontend | http://localhost:8081 |
| Swagger UI | http://localhost:8081/swagger-ui/index.html |
| H2 Console | http://localhost:8081/h2-console |

Dados do H2 Console:

- JDBC URL: `jdbc:h2:mem:taskmanager`
- User: `sa`
- Password: *(vazio)*
> O banco H2 em memória é criado automaticamente ao iniciar. Nenhuma configuração adicional é necessária.
> 
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

- **Unitários** (`TaskServiceTest`): regras do service com repositório mockado — busca por id, criação e exceção para id inexistente.
- **Integração** (`TaskControllerIntegrationTest`): fluxo CRUD completo via MockMvc, validação de campos obrigatórios e rejeição de enum inválido.

## Endpoints da API

Base URL: `/api/tasks`

### Listar tarefas

```http
GET /api/tasks
GET /api/tasks?status=TODO&priority=HIGH
```

Parâmetros opcionais:

| Parâmetro | Valores aceitos |
|-----------|----------------|
| `status` | `TODO`, `IN_PROGRESS`, `DONE` |
| `priority` | `LOW`, `MEDIUM`, `HIGH` |

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

```
src/
├── main/
│   ├── java/com/taskmanager/
│   │   ├── config/            # Configuração de CORS
│   │   ├── controller/        # TaskController
│   │   ├── dto/               # TaskRequestDTO, TaskResponseDTO
│   │   ├── exception/         # GlobalExceptionHandler, ErrorResponse
│   │   ├── model/             # Task (entity) + enums
│   │   ├── repository/        # TaskRepository
│   │   ├── service/           # TaskService (regras + logs)
│   │   └── TaskManagerApplication.java
│   └── resources/
│       ├── db/migration/      # V1__create_tasks_table.sql
│       ├── static/            # Frontend estático (HTML/CSS/JS)
│       └── application.properties
└── test/
    └── java/com/taskmanager/
        ├── controller/        # TaskControllerIntegrationTest
        └── service/           # TaskServiceTest
```

## Decisões técnicas e análise de incidente

Consulte o arquivo [TECHNICAL_NOTE.md](./TECHNICAL_NOTE.md).

## Para o Frontend utilizando o Angular

Consulte o repositório [task-manager-front](https://github.com/lenington/task-manager-front)
