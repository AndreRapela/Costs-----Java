# Costs API

API desenvolvida com **Spring Boot 3**, **Java 21** e **MySQL** para gerenciamento de orçamentos, com autenticação JWT e controle de acessos via Spring Security.

---

## 🚀 Tecnologias utilizadas

- **Java 21**
- **Spring Boot 3**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
  - Spring Security
- **MySQL**
- **Flyway** (migrações de banco de dados)
- **Lombok**
- **JWT (JSON Web Token)** via [java-jwt](https://github.com/auth0/java-jwt)

---

## ⚙️ Configuração do Projeto

### Pré-requisitos

- **Java 21**
- **Maven 3.9+**
- **MySQL 8+**

### Configuração do Banco de Dados

No arquivo `application.properties` ou `application.yml`, configure o acesso ao banco MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/costsdb
spring.datasource.username=root
spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

spring.flyway.enabled=true
```

> As migrações de banco são gerenciadas pelo **Flyway**.

### Rodando a aplicação

```bash
# Build do projeto
mvn clean install

# Rodando a aplicação
mvn spring-boot:run
```

A API estará disponível em:  
👉 `http://localhost:8080`

---

## 🔑 Autenticação

- A autenticação é baseada em **JWT**.
- O usuário deve realizar login (endpoint de autenticação) para receber o token.
- O token deve ser enviado no **header Authorization** em cada requisição protegida:

```http
Authorization: Bearer <seu_token_aqui>
```

---

## 📌 Endpoints principais

### Orçamentos (`/orcamentos`)

- `POST /orcamentos` → Criar um novo orçamento
- `GET /orcamentos/{id}` → Buscar orçamento por ID
- `PUT /orcamentos/{id}` → Atualizar orçamento
- `DELETE /orcamentos/{id}` → Remover orçamento

### Autenticação

- `POST /login` → Realizar login e obter JWT

---

## 🛠️ Testes

O projeto utiliza **Spring Boot Starter Test** e **Spring Security Test** para testes unitários e de integração.

Rodar testes:

```bash
mvn test
```

---

## 🏗️ Arquitetura (C4 Model)

A arquitetura do sistema foi documentada utilizando o modelo **C4 (Context, Container, Components)**.

### Nível 1 – Contexto do Sistema
Mostra como o sistema **Costs** se relaciona com os usuários.

![C4 Nível 1 - Contexto](./img/System%20context%20-%20Costs%20N1.drawio.png)

---

### Nível 2 – Containers
Mostra os principais containers: SPA (Angular), API (Spring Boot) e Banco de Dados (MySQL).

![C4 Nível 2 - Containers](./img/Container%20Costs%20-%20C4%20N2.drawio.png)

---

### Nível 3 – Componentes
Mostra os principais componentes da API, incluindo controllers, repositórios, autenticação e handlers de exceções.

![C4 Nível 3 - Componentes](./img/Components%20Costs%20-%20C4%20N3.drawio.png)

---

## 📖 Referências

- O modelo de documentação em C4 foi inspirado no vídeo do canal **Rodrigo Branas**:  
  [Arquitetura de Software com C4 Model](https://www.youtube.com/watch?v=iWkXd0RJ2FA&t=2966s)

---

📌 **Autor**: André Rapela  
