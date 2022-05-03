# bank-service-advance
## Supported Operations
- Put amount into account
- Withdraw amount from account
- Transfer amount between accounts
## Postman Tests
Postman request sample is located at /test/resources/bank_service.postman_collection.json
## How to build
mvnw clean package
## How to run
java -jar service.bank-0.0.1-SNAPSHOT.jar
## Default Configuration
- Service is run on 8080 port : http://localhost:8080
- 2 Users are configured for tests : user/pass and user2/pass
- H2 console is available on http://localhost:8080/h2-console. Cred : admin/admin
