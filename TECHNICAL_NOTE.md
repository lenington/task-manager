# Nota técnica

## Decisões

- A aplicação foi mantida em Spring Boot 2.7 e Java 8 para respeitar a base existente e evitar uma migração desnecessária.
- O front-end foi implementado como arquivos estáticos servidos pelo próprio Spring Boot. Isso reduz dependências e facilita a avaliação com um único comando.
- O banco padrão é H2 em memória com Flyway. Assim, o avaliador consegue executar o projeto sem configurar MySQL. O perfil `mysql` permanece disponível.
- A API usa DTOs para entrada e saída, validação e tratamento de exceções.
- Os logs ficam no serviço, onde encontra-se o CRUD.

## Trade-offs

- O front-end não usa framework JavaScript. Para este escopo, HTML/CSS/JS puro entrega o fluxo completo com menos instalação e menor risco operacional.
- O H2 não substitui todos os comportamentos do MySQL, mas é adequado para execução local e testes rápidos. Para produção, o perfil MySQL deve ser usado e validado.
- A listagem não possui paginação. Para grande volume de tarefas, paginação e ordenação seriam necessárias.

## Melhorias futuras

- Adicionar paginação, ordenação e busca textual.
- Adicionar autenticação e autorização de usuários.
- Padronizar respostas de validação com timestamp e código de erro.
- Criar pipeline CI com `mvn test`.
- Adicionar testes mais robustos (end-to-end do front-end).
- Diagnóstico mais eficaz para produção.

## Análise de incidente

### Cenário

Um erro recorrente aparece nos logs durante a inicialização da aplicação:

```text
Flyway migration failed: Found non-empty schema(s) but no schema history table.
Ou:
Validate failed: relation/table tasks not found.
```

Também poderia aparecer após uma mudança de pasta:

```text
Flyway failed to initialize: Unable to resolve location classpath:db/migration.
```

### Hipótese principal

A aplicação estava configurada para buscar migrations em `classpath:db/migration`, mas a migration inicial estava em `db/migrations`. Com isso, o Flyway não encontrava o script, o Hibernate tentava validar a tabela `tasks` por causa de `ddl-auto=validate`, e a inicialização falhava porque a tabela não existia.

### Impacto

- API indisponível após deploy ou restart.
- Frontend sem conseguir ter acesso as tarefas.
- Logs repetidos de falha de conexão ou validação.

### Correção aplicada

- A migration foi movida para `src/main/resources/db/migration/V1__create_tasks_table.sql`.
- O banco padrão foi alterado para H2 em memória, facilitando reprodução e validação local.
- O perfil MySQL foi isolado em `application-mysql.properties` ou `application-mysql55.properties`.
- Foram adicionados testes de integração para validar a CRUD.

### Prevenção

- Rodar `mvn test` em CI antes de qualquer merge.
- Manter `spring.jpa.hibernate.ddl-auto=validate` para detectar divergencias entre o banco e a entity.
- Evitar mudanças manuais em paths de migrations sem teste de inicialização da aplicação.
- Monitorar logs de startup e criar alerta para falhas.
- Documentar claramente o banco usado em cada ambiente.
