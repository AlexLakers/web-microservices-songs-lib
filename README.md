# web-microservices-songs-lib
This app has a microservice structure using 'Kafka','Redis' and allows you to use and manage songs library. 'Keycloak' and OAUTH2.0 provides security this app.

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/40.png?raw=true)

## Logging and monitoring system
I used (ELK) stack for vizualization logs of services.
'Elasticsearch'- analitic sistem which creates indexes for logs from logstash.
'Logstash'- some conveyor to transfer or filter logs to elasticsearch.
'Kibana'-system of vizualization.
'Zipkin'-for tracing transactions.
And I want to a cuple of words about 'Prometheus' and 'Grafana'
'Graphana'-system for getting metrics from our app and vizualization.
'Prometheus'-system for getting and saving metrics using time series.

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/35.png?raw=true) 
## About this app
This app is based on microservice architecture and I should write about it in general and particular. 
If you want to use this app then you will be userful for you to understand general idea.
Firstly, you need to finish 'deployment stage' using docker-compose(or kubernate).
All the services must start in the correct order(see 'web-microservices-songs-lib/docker/compose.yaml').
After it you can go to localhost:8089/dashboard.

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/34.png?raw=true)

You will see some dashboard all the services. And now you can navigation menu to use a specific service in list('Author-service','Song-service'..)
But you should remember that my app is protected using OIDC protoclo fore authentication and OAUTH2.0 for authorization.
As a authorization-service for generating ,checking all the tokens: access_tokens,token_id.. I choosed keycloak.
You must create user,client,realm and roles in 'keycloak'(see 'web-microservices-songs-lib/docker/compose.yaml').
If you click to 'login using keycloak' then you must enter you credentails and after it you can click a specific service.

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/33.png?raw=true)


For example, we can show all the author by entered criteria.

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/38.png?raw=true)


Also we can change sone available author (update,delete)

If you click to 'logout' then occurs invalidate currect session in the app and in the keycloak too.

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/39.png?raw=true)


Ok, we need to return to our main idea. for example, you finish with setting keycloak 
 I would like to describe an every single service.

### Author-service
This service contains funtions for interaction with  alone database 'songs_lib_auhtor'. I need to tell you that this app
contains more operations then CRUD. For example, you can use dynamic filter and pagination by input criteria from user.
This app does not have 'MVC' part but it have REST API for remote calls from other services using http-cleints(Feign,RestTemplate,WebClient).
As I sad early this service uses database 'Postgres' for permanent storage. But it is not all, it uses NoSql storage 'Redis' as a caching system.  

### Song-Service
This service contains funtions for interaction with  alone database 'songs_lib_song'. It allows some user to find any song in 
abstract library using dynamic filter(name,author and so on). We can get specific page with specific size if we want.
This service interacts with 'author-service' using REST-clients.

### Message-broker
As a message broker was choosed Kafka 3.5. In additional I used zookeeper as an orchestration system for kafka.
Kakfa used for comminication process between 'Author-service' as a consumer and 'Song-Service' as a producer.
Song-service notifies 'Auhtor-service' if some data was changed. It occurs using kafka and Spring-Cloud-Stream.
Some words about it: If some data are changed in 'Song-service' then the publisher sends message to kafka-server
using Suplier Spring-Cloud-Stream. After it 'Author-service' gets a specific message from kafka-server and do corresponding
actions with Redis-storage.

### Eureka-Server
This service is discovery service for clients(microservices).

### Gateway-server.
This service is api-gateway server. It used as a single point this app for routing and filtering.

### Config-server
This service is config-server to store config  of this app separately from app.
In here I used git repo for it 'song-lib-config' 

https://github.com/AlexLakers/songs-lib-config.git

### Frontend-client
This service is dashboard for interaction with available services. Now you can use 'Author-service' and 'Song-service'.
This service uses MVC pattern and contains .html pages for visualization all the necessary information by entered input data.
Also this app uses for authorization and authentication processes. We can get token_id and access token from Keycloak
using this app as a 'oauth-client'.

### Security System
I use spring-security-starter to provide secirity in this app. In additional I install authorization server 'Keycloak' to
manage process authentication and authorization. We must create our own realm in Keycloak with full set roles,users and applications.
Also we can use standard enpoints which provides Keylocak server to get token or to check token and so on.
So, what about intercation different components during this process. When some user of this app click on start page or specific service
in dashboard page then he need to finish authentication process using keycloak login-form.After it this app gets authorization code
and sends request to keycloak to get access token and token_id. Ok, we have tokens.Then we can go to our services with it.
'Author-service' and 'Song-service' as resourse-servers and to interact with them we need to have token.After it token will check
using Keycloak.

### Building system
AS a building system I used Maven. Also I used Docker to manage images and containers.
In particular I used Dockerfile maven plugin to create images for all the microservices by Dockerfiles.
What about containers, I choosed Docker-compose to describe and create services(containers) using compose.yaml.

> - You can build .jar files for all the services using one command in parent project 'web-microservices-songs-lib/':
```
./mvnw clean package
```
> - After it we must make Dockerfile for each service. It located in '/web-microservices-songs-lib/XXX-service/Dockerfile':
> - And we can build image manually using docker build command. Or we can use dockerfile plugin '/web-microservices-songs-lib'
```
./mvnw dockerfile:build
```
> - After it we nust run all our services 'web-microservices-songs-lib':
```
docker compose -f docker/compose.yaml up
```


  
 
This app can be used as a base for product data managment system(PDM). You need also to use it as a Hierarchical Data Storage with authentication and authorization functions.
As a client of this app you can use web intarface with html pages for registartion a new user, for log in,for specifications or details data managment.
Possible, you will want to use data from this service in your app then you need to pay attention to REST-API for getting data formatted .JSON between different system.

## More datails 
This app has ***MVC*** part:
The main page of this app allows you go to any page using header buttons.
![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/5.png?raw=true)

For example, if you are not authenticated user then you can click on 'Registartion' page and finish registartions operation.
Similary, you can go to login page,logout, profile of current user and so on.
![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/1.png?raw=true)

After authentication you can add new parent node(Specification) to database using special form.
You can also find available specifications using specific params for search.For example, you can 
set field and direction of sort. What about pagination then it is available for you too.
of course you can update or delete your specifications. Similary , you can manage set details in parent specification.
![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/6.png?raw=true)

As I sad early you can save new specification or detail:
![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/11.png?raw=true)

Also if you have role 'ADMIN' then you have access to ***Admin page***(***see navigation menu):
![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/10.png?raw=true)


You can also update,create and delete user for this app:
![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/9.png?raw=true)

As I sad early you can use ***REST-API*** too. More information about REST endpoints 'src/main/resources/api-docs.json'.

```json
/api/v1/specifications": {
      "get": {
        "tags": [
          "rest-specification-controller"
        ],
        "operationId": "findAll_1",
        "parameters": [
          {
            "name": "specificationSearchDto",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/SpecificationSearchDto"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageSpecificationDto"
                }
              }
            }
          }
        }
      }
.....
```

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/32.png?raw=true)
