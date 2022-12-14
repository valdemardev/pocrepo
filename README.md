# Stack Collaborator

A ideia desse **CRUD** é mostrar algumas stacks básicas que compõem um microserviço, tais como Logs, Tracing, programação reativa(sem bloqueio), cofre de segredos e service discovery. Outra caracteristicas que fazem parte do microserviço, como api gateway, mais itens de observabilidades, malha de serviços, monitoramento, entre outras, serão discutidas e avaliadas em outros momentos e em outros microserviços. 

## Importante
Para subir a stack abaixo localmente, tenha instalado o **docker** em sua máquna de desenvolvimento.


## Consul

Para subir o consul execute o seguinte comando:

`docker run -d --name=consul    -e CONSUL_BIND_INTERFACE=eth0    -p 8500:8500    consul`

Depois entre em [http://localhost:8500/](http://localhost:8500/) -> click na aba Key/Value -> clique em create e cadastre a pasta config(ex: config/) e dentro de config crie a chave  **collaborator** com o seguinte conteúdo:

`mongouri=mongodb://${mongo-user}:${mongo-passwd}@localhost:27117`

`database=destiny`

## MongoDB
Subindo o MongoDB

`docker run -ti --rm --network bridge -p 27117:27017 -d --name mongodb \
      -e MONGO_INITDB_ROOT_USERNAME=admin \
      -e MONGO_INITDB_ROOT_PASSWORD=admin \
      mongo`
      
Caso não fucionar o usuario e senha definido por parametro execute os passos abaixo:

`#1 docker exec -it mongodb /bin/bash`    

`#2 mongosh`    

`#3 use admin`

`#4 db.auth("admin", passwordPrompt())`  //digite a senha do usuario admin         

## Vault
Subindo o vault

`docker run --rm --cap-add=IPC_LOCK -e VAULT_ADDR=http://localhost:8200 -p 8200:8200 -d --name=dev-vault vault:1.6.0`

Depois execute:

`docker logs dev-vault`// pegue o valor de **Root Token** você vai precisar mais a frente

Execute os passos:

`#1 docker exec -it dev-vault sh`

`#2 export VAULT_TOKEN=Root Token`

`#3 vault kv put secret/myapps/vault-quickstart/config mongo-user=seuUsuarioMongo mongo-passwd=suaSenhaMongo`

`#4 cat <<EOF | vault policy write vault-quickstart-policy -
path "secret/data/myapps/vault-quickstart/*" {
  capabilities = ["read"]
}
EOF`

Nesse exemplo estou utilizando o tipo de autenticação Approle, mas podemos mudar isso mais pra frente:

`#5 vault auth enable approle`

`#6 vault write auth/approle/role/myapprole policies=vault-quickstart-policy`

`#7 vault read auth/approle/role/myapprole/role-id`// guarde o valor do campo **role_id**

`#8 vault write -f auth/approle/role/myapprole/secret-id`// guarde o valor do campo **secret_id**

## Configurando o Logs
Instale o docker-compose, uma vez que o docker-compose já esteja instalado execute o seguinte comando abaixo dentro da pasta onde esta o arquivo docker-compose.yml. No nosso exemplo, eu deixei esse arquivo pronto na pasta do projeto em **/destiny_ms_collaborator/src/main/docker/docker-compose.yml**

`docker-compose up -d` 

Depois dos serviços ativos entre em [http://localhost:9000/](http://localhost:9000/) usuario **admin** e senha **admin**. Você está na dentro do gryalog. Agora clique na aba System -> Input -> no campo Select Input digite **GELF UDP** -> selecione o node, de um titulo e clique em salvar. Pronto, o graylog já está pronto para receber seus logs, depois clique em Show received messages e depois que você terminar de configurar essa stack e subir seu microserviço você irá começar a ver os logs da sua aplicação chegando no graylog.

## Configurando OpenTrancing

Subindo o serviço:

`docker run -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 14268:14268 jaegertracing/all-in-one:latest`

Depois acesse [http://localhost:16686/](http://localhost:16686/) e você vera a interface do **Jaegger** e pode começar ver os trancing dos endpoints.

## Agora vamos rodar

 1. Para rodar em modo dev execute:
 
 `mvn compile quarkus:dev`

 2. Para rodar em modo JVM execute :
 
 `mvn clean package`

   Depois 

 `java -jar target/quarkus-app/quarkus-run.jar`

 3. para rodar em modo nativo de máqui usando o GraalVM Mandrel(Aqui você não precisa ter o GraalVM instalado e configurado na sua máquina local, logo você precisa de um sabor de container, seja ele, docker, podman, ContainerD, Mesos, etc, no nosso exemplo estamos usando docker) e do java 11 com o Java_Home devidamente configurado, execute o seguinte comando:
 
 `mvn package -Pnative -Dquarkus.native.container-build=true`

   Depois 

 `./target/destiny-collaborator-1.0.0-SNAPSHOT-runner`

O microserviço vai subir na casa dos milisegundos e aí você pode voltar para a interface do graylog e ver seus logs chegando. 

## HealthCheck

Através da URI [http://localhost:8080/q/health-ui/](http://localhost:8080/q/health-ui/) a gente pode ver a saúde da aplicação e dos serviços que ela se conecta, no nosso caso o serviço de banco de dados do mongoDB.

## Variáveis de Ambiente

Lembra que falei para guardar o secret_id e o role_id? Então para que nada fique explicito no código, umas das boas práticas é usar váriaveis de ambiente.

Veja:

**Vault**

`export ROLE_ID=seuRoleID`

`export SECRET_ID=seuSecretID`

`export VAULT_URL=http://localhost:8200`

`export KV_PATH=myapps/vault-quickstart/config`

**Consul**

`export CONSUL_URL=localhost:8500`
`export CONSUL_VALUE_KEY=config/collaborator`

**GELF**

`export GELF_URL=localhost`

`export GELF_PORT=12201`

**Jaeger**

`export JAEGER_SERVICE_NAME=collaborator-tracing`
