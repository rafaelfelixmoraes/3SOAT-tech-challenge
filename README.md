# 3SOAT-tech-challenge
Repositório do Tech Challenge do grupo G8 da turma 3SOAT/2023 - Pós Tech FIAP

Projeto desenvolvido com as seguintes tecnologias:

- Java 17
- Gradle
- Framework Spring (boot, data, mvc, security)
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

## Documentação do Projeto
O projeto possui um diretório que concentra a documentação, bem como desenhos da arquitetura e outros arquivos pertinentes. Basta acessar o diretório `./docs`

Nesse link https://www.notion.so/Documenta-o-do-Sistema-de-Autoatendimento-de-Fast-Food-DDD-e523aebb91d44a0a85d511d2fd459763, é possível visualizar a documentação completa do projeto, contendo o descritivo do DDD, bem como os diagramas e desenhos da arquitetura na Azure e AWS

O vídeo com a apresentação da arquitetura do projeto no ambiente da Azure está disponível nesse link: https://vimeo.com/905022966?share=copy

## Fluxo das APIs e Collections
O projeto possui diversas APIs para realização e gerenciamento de pedidos, produtos e clientes. O sistema também possui APIs para cadastro e autenticação de usuários.

Nesse momento, todas as APIs estão abertas e não possuem autenticação, para agilizar alguma validação se necessário. Nas próximas fases, iremos evoluir essa parte.

As collections do Postman com todas as APIs do projeto estão disponíveis na pasta `./docs/postman` na raiz do projeto. Basta importar os arquivos no seu postman conforme desejado

Obs.: As collections possuem 2 variáveis de ambiente com a url base local do serviço. Algumas APIs ainda estão com a variável `baseUrl` apontadando para `http://localhost:8080`, outras possuem a variável `baseUrlK8s` apontando para `http://localhost:31808`. Para validações usando o ambiente do Kubernetes, deve-se usar a variável `baseUrlK8s`

## Kubernetes
O projeto possui integração com a arquitetura Kubernetes, e os arquivos dos manifestos (.yml) estão localizados na pasta `./k8s` na raiz do projeto. 

Para a implementação e testes do projetos, foi utilizado a ferramenta `kubectl` que é integrado ao Docker Desktop no ambiente Windows. Para instalação, consulte aqui: https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/. Para utilização, confira essa documentação: https://kubernetes.io/pt-br/docs/reference/kubectl/

Para execução do projeto utilizando a arquitetura do Kubernetes localmente, siga os passos a seguir:
- Realizar o clone do projeto
- Após verificar que o `kubectl` está operacional, via prompt navegue até o diretório `./k8s` na raiz do projeto
- Para verificar se o cluster local está operacional, execute o comando `kubectl cluster-info`. O resultado deve ser algo parecido com isso:
  ```
  Kubernetes control plane is running at https://kubernetes.docker.internal:6443
  CoreDNS is running at https://kubernetes.docker.internal:6443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy

  To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
  ```
- Dentro do diretório `./k8s`, Para subir os pods com os serviços, execute os seguintes comandos, na ordem:
  - `kubectl apply -f .\redis-tech-challenge.yaml` - Cache Redis
  - `kubectl apply -f .\postgres-tech-challenge.yaml` - Postgres DB
  - `kubectl apply -f .\application-tech-challenge.yaml` - Microserviço Java
- Com exceção dos serviços e cachê e DB, o serviço Java possui Probes configuradas para validação e verificação da saúde do pod e do microserviço. O pod em questão ficará 100% disponível em cerca 4,5 minutos após a execução do comando, podendo variar dependendo de ambiente.
- Para verificar o status dos pods, basta executar o comando `kubectl get pods`. O retorno deve ser algo parecido com isso:
  ````
  NAME                                   READY   STATUS    RESTARTS   AGE
  postgres-566d84c765-2grvl              1/1     Running   0          6m22s
  redis-bd5b6d555-xg6pc                  1/1     Running   0          6m36s
  tech-challenge-java-64df984894-s5pdr   1/1     Running   0          5m10s
  ````
- Após a completa inicialização dos serviços, a aplicação estará disponível localmente pela URL `http://localhost:31808/swagger-ui/index.html#/`
- O microserviço possui integração com a ferramenta Spring Actuator, que fornece ferramentas para monitoramento da aplicação. Para obter um resumo da saúde da aplicação, basta acessar a URL `http://localhost:31808/actuator/health`
- Após as validações desejadas, para finalizar a execução dos pods, basta executar os comandos a seguir em ordem:
  - `kubectl delete deployment tech-challenge-java` - Microserviço Java
  - `kubectl delete deployment redis` - Cache Redis
  - `kubectl delete deployment postgres` - Postgres DB


## Integração MP - Mercado Pago
Documentação base: https://www.mercadopago.com.br/developers/pt/docs/qr-code/pre-requisites

O sistema possui uma integração prévia com as apis do Mercado Pago, para autenticação e geração do qr com o link de pagamento a partir do pedido. Porém, a integração ainda não está finalizada, pois será concluída na fase 3, uma API para receber o retorno da confirmação de pagamento, enviada pelo MP. No momento a atualização do status do pagamento / pedido é estática no momento do checkout.

## SonarQube
O SonarQube é uma ferramenta autogerenciada e automática de revisão estática de código que ajuda sistematicamente a fornecer um código limpo

O Sonar ja é executado automaticamente com os demais containeres do projeto.

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

## Pipeline
O pipeline desse projeto utiliza Github Actions, com integração com Azure, onde a pipeline realiza o deploy dos manifestos .yamls diretamente no cluster Kubernetes na Azure através da intregração com Azure CLI e Azure EntraID (AD)

A pipeline do Actions está configurada da seguinte forma:
- Para PRs: Executa a validação e build do script terraform, até a etapa "terraform plan"
- Commits na Main: A etapa da implantação via "terraform apply" ocorre somente quando há um evento de Push na branch "Main"
- A cada execução da pipeline, ele gera automaticamente um comentário no PR com o status da execução de cada passo
