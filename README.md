# Gestão de Vendas

Sistema de microserviços para **gestão de clientes, produtos, pedidos e notificações**, desenvolvido com **Spring Boot**, **Spring Cloud** e **Kafka**, seguindo arquitetura moderna de microserviços.

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.5.5
- Spring Cloud 2025.0.0
- Spring Data JPA
- Spring Kafka
- H2 Database (em memória para testes)
- MySQL (produção)
- JUnit 5 / Mockito (testes)
- Docker / Docker Compose

---

## Estrutura do Projeto

O repositório está organizado como **multi-módulo Maven**:

| Módulo             | Descrição                                               |
|-------------------|--------------------------------------------------------|
| ms-clientes       | Microserviço para gestão de clientes.                 |
| ms-produtos       | Microserviço para gerenciamento de produtos.          |
| ms-pedidos        | Microserviço para processamento de pedidos.           |
| ms-notificacoes   | Microserviço responsável por envio de notificações.   |
| api-gateway       | Ponto de entrada único para todos os microserviços via API Gateway. |
| gestao-vendas     | POM pai agregando todos os módulos.                   |

---

## Microserviços e Endpoints

### ms-clientes
- Banco: H2 (teste), MySQL (dev/prod)
- Endpoints:
    - `GET /api/clientes` → Lista todos os clientes
    - `GET /api/clientes/{id}` → Detalhe de um cliente
    - `POST /api/clientes` → Cria um novo cliente
    - `PUT /api/clientes/{id}` → Atualiza um cliente
    - `DELETE /api/clientes/{id}` → Remove um cliente

### ms-produtos
- Banco: MySQL
- Endpoints:
    - `GET /api/produtos` → Lista todos os produtos
    - `GET /api/produtos/{id}` → Detalhe de um produto
    - `POST /api/produtos` → Cria um novo produto
    - `PUT /api/produtos/{id}` → Atualiza um produto
    - `DELETE /api/produtos/{id}` → Remove um produto

### ms-pedidos
- Banco: MySQL
- Endpoints:
    - `GET /api/pedidos` → Lista todos os pedidos
    - `GET /api/pedidos/{id}` → Detalhe de um pedido
    - `POST /api/pedidos` → Cria um novo pedido

### ms-notificacoes
- Integração com Kafka para envio de notificações assíncronas
- Tópicos Kafka: `clientes`, `pedidos`, `notificacoes`

### api-gateway
- Funcionalidade: Roteamento de requisições para microserviços internos
- Exemplo: `/api/clientes/**` → encaminha para `ms-clientes`

---

## Perfis de Configuração

| Profile | Descrição                        | Porta Padrão |
|---------|---------------------------------|--------------|
| dev     | Desenvolvimento / Local          | 8080         |
| test    | Testes / H2 Database em memória | 8081         |
| prod    | Produção / MySQL                 | 8080         |

Executando com perfil dev:

```bash
mvn clean install -Pdev
mvn spring-boot:run -Pdev
