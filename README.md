# 3SOAT-tech-challenge
Repositório do Tech Challenge da turma 3SOAT/2023 - Pós Tech FIAP

Projeto desenvolvido com as seguintes tecnologias:

- Java 17
- Gradle
- Framework Spring (boot, data, mvc)
- JPA/Hibernate
- Banco de dados Postgres, com migrations utilizando Flyway
- Junit com MockMVC / Jupiter
- APIs Rest
- Swagger
- Spring Actuator
- Docker
  
Para executar o projeto, siga os seguintes passos:
- Realize o clone do repositório e dentro da IDE desejada;
- Instalar o Docker CLI na máquina, ou Docker Desktop caso utilize Windows
- Executar o build do projeto
- Pelo terminal, Navegue até a raiz da pasta onde foi clonado o projeto, e rodar os seguinte comando: `docker-compose up -d`
- Após verificar que os containeres estão em execução, rode a aplicação na IDE, e assim que ela subir com sucesso, acesso a URI `http://localhost:8080/swagger-ui/index.html#/`
