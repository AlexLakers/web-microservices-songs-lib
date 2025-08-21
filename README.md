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
