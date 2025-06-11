
# Seata the deal: no more distributed transaction nightmares across (Spring Boot) microservices

[Example of Apache Seata-managed distributed transactions across multiple Spring Boot microservices](https://medium.com/@biagio.tozzi/seata-the-deal-no-more-distributed-transaction-nightmares-across-spring-boot-microservices-7155312032f5)

![image](https://github.com/user-attachments/assets/2ceffe93-ca6f-4288-80f7-1f4f693fe87b)

## Setup

#### Infra components
Go to *infra* folder:

    docker compose up

This will start:

- MySQL (with the tables needed for Seata Server).
- Seata Server.
- Grafana, Prometheus and Tempo for observability.

#### Microservices
Foe each microservice folder (bff, credit-api and shipping-api) run:

    ./gradlew bootRun

This will start:

- microservice
- the dedicated db (MariaDB for credit-api and PostgreSQL for shipping-api)

#### Observability
- Grafana: http://localhost:3000
  - Spring Boot dashboard: http://localhost:3000/dashboards
  - Prometheus & Tempo: http://localhost:3000/explore

#### OpenAPI
Microservices OpenAPI:
- bff: http://localhost:8080/swagger-ui/index.html
- credit-api: http://localhost:8081/swagger-ui/index.html
- shipping-api: http://localhost:8082/swagger-ui/index.html
