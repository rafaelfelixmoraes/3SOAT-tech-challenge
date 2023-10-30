# 3SOAT-tech-challenge
Repositório do Tech Challenge do grupo G8 da turma 3SOAT/2023 - Pós Tech FIAP

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
- SonarQube
  
Para executar o projeto, siga os seguintes passos:
- Realize o clone do repositório e dentro da IDE desejada;
- Instalar o Docker CLI na máquina, ou Docker Desktop caso utilize Windows
- Executar o build do projeto
- Pelo terminal, Navegue até a raiz da pasta onde foi clonado o projeto, e rodar os seguinte comando: `docker-compose up -d`
- Após verificar que os containeres estão em execução, acesse a URI `http://localhost:8080/swagger-ui/index.html#/`
- Caso deseje rodar pela IDE, é preciso parar a execução do container da aplicação. Após isso rode a aplicação na IDE, e assim que ela subir com sucesso, acesse a mesma URI acima.

## Integração MP - Mercado Pago
Documentação base: https://www.mercadopago.com.br/developers/pt/docs/qr-code/pre-requisites

O sistema possui uma integração prévia com as apis do Mercado Pago, para autenticação e geração do qr com o link de pagamento a partir do pedido. Porém, a integração ainda não está finalizada, pois será construida na fase 2, uma API para receber o retorno da confirmação de pagamento, enviada pelo MP. No momento a atualização do status do pagamento / pedido é estática no momento do checkout.

## SonarQube
O SonarQube é uma ferramenta autogerenciada e automática de revisão estática de código que ajuda sistematicamente a fornecer um código limpo

O Sonar ja é executado automaticamente junto com os demais containeres do projeto.

Para rodar a análise estática do código, basta seguir os passos a seguir:
- Certifique-se que o container do microserviço esteja parado
- Pelo terminal, execute o seguinte comando dentro da pasta raiz do projeto: `./gradlew clean build`
- Certifique-se que o container do SonarQube, acessando o endereço: `http://localhost:9000/projects`
- Na tela de login, use as credenciais: `login: admin, password: admin`
- Na tela seguinte, será necessário fornecer novas credenciais
- Após a etapa de autenticação, siga essas instruções para criação do projeto: https://docs.sonarsource.com/sonarqube/latest/try-out-sonarqube/#analyzing-a-project
- Após criar o projeto, na tela principal, navegue até a opção de execução da análise local
- Em seguida, crie um token para uso no próximo passo
- No terminal, execute o seguinte comando: `./gradlew sonar "-Dsonar.token=[token_criado]"`
- Após a conclusão do build, você poderá conferir os resultados da análise na página do projeto criado
- O Quality Gate utilizado com base nas métricas é o padrão da própria ferramenta, mas é possível criar outros conforme necessidade
