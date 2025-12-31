
#  Architecture Microservices 

## Objectif
Créer une application basée sur une architecture microservices qui permet de gérer les factures contenant des produits et appartenant à un client.

---


## Architecture du Projet

<img width="239" height="364" alt="image" src="https://github.com/user-attachments/assets/931344dc-6c14-4d97-a4b3-6ebfd44b6967" />

Le projet se compose de plusieurs microservices interconnectés :



## 1. Microservice Customer-Service

### Description
Service responsable de la gestion des clients.

### Structure du Projet

<img width="361" height="375" alt="image" src="https://github.com/user-attachments/assets/01504d1e-0093-4c68-b4a1-4addd3bca39a" />

### Entités Principales

#### Customer
- Entité représentant un client
- Attributs : id, nom, email, etc.

#### CustomerProjection
- Projection pour la récupération optimisée des données client

### Fonctionnalités
-  CRUD complet sur les clients
-  REST Repository avec Spring Data REST
-  Configuration personnalisée via `RestRepositoryConfig`
-  Paramètres de configuration externalisés (`CustomerConfigParams`)

---

## 2. Microservice Inventory-Service

### Description
Service responsable de la gestion des produits.

### Structure du Projet

<img width="340" height="334" alt="image" src="https://github.com/user-attachments/assets/8d0856ec-932f-4e9d-96e2-3c4ae36e543c" />


### Entités Principales

#### Product
- Entité représentant un produit
- Attributs : id, nom, prix, quantité, etc.

### Fonctionnalités
- Gestion complète des produits
-  REST Repository pour l'exposition des APIs
-  Configuration du repository REST

---

## 3. Gateway Service (Spring Cloud Gateway)

### Description
Point d'entrée unique pour tous les microservices. Gère le routage des requêtes vers les services appropriés.



### Fonctionnalités
-  **Configuration statique** : Routes définies dans le fichier de configuration
-  **Configuration dynamique** : Routes découvertes automatiquement via Eureka
-  Load balancing
-  Filtres de sécurité et de logging

### Configuration des Routes

#### Routes Statiques (application.yml)
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: customer-service
          uri: lb://CUSTOMER-SERVICE
          predicates:
            - Path=/customers/**
        - id: inventory-service
          uri: lb://INVENTORY-SERVICE
          predicates:
            - Path=/products/**
```

#### Routes Dynamiques
Découverte automatique via Eureka Discovery Service.

---

## 4. Discovery Service (Eureka Server)

### Description
Service d'annuaire permettant l'enregistrement et la découverte des microservices.


### Fonctionnalités
-  Enregistrement automatique des microservices
-  Health check des services
-  Load balancing côté client
-  Dashboard web pour visualiser les services

### Configuration
```yaml
spring.application.name=discovery-service
server.port=8761
eureka.client.fetch-registry=false
eureka.client.register-with-eureka=false
```

---

## 5. Billing Service (avec OpenFeign)

### Description
Service de facturation qui agrège les données des clients et des produits pour générer des factures.

### Structure du Projet

<img width="356" height="519" alt="image" src="https://github.com/user-attachments/assets/20b4e115-9c15-425d-81d2-868ccae93b00" />


### Entités

#### Bill
- Entité représentant une facture
- Relations avec Customer et ProductItem

#### ProductItem
- Ligne de facture contenant un produit et sa quantité

### Clients Feign

#### CustomerRestClient
```java
@FeignClient(name = "customer-service")
public interface CustomerRestClient {
    @GetMapping("/customers/{id}")
    Customer getCustomerById(@PathVariable Long id);
    @GetMapping("/customers")
    PagedModel<Customer> getAllCustomers();
}
```

#### ProductRestClient
```java
@FeignClient(name = "inventory-service")
public interface ProductRestClient {

    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable String id);
    @GetMapping("/products")
   PagedModel<Product>  getAllProducts();
}
```

### Fonctionnalités
-  Création de factures
-  Association client-produits
-  Calcul automatique du montant total
-  Communication inter-services via OpenFeign

---

## 6. Config Service (Spring Cloud Config)

### Description
Service centralisé de gestion des configurations pour tous les microservices.

### Structure
```
config-service/
└── src/main/java/
    └── ConfigServiceApplication
```
config-repo
<img width="343" height="263" alt="image" src="https://github.com/user-attachments/assets/6b7bbabf-4f96-434f-aa66-2ff864907c0a" />


### Fonctionnalités
-  Configuration centralisée
-  Support multi-environnements (dev, prod, test)
-  Rafraîchissement dynamique des configurations
-  Versioning des configurations via Git

### Configuration
```yaml
spring.application.name=config-service
server.port=9999
spring.cloud.config.server.git.uri=https://github.com/El-Fijaoui-Nissrine/ConfigurationRepo
```

---

## Architecture Technique

### Technologies Utilisées

| Technologie | Usage |
|------------|-------|
| **Spring Boot** | Framework principal |
| **Spring Cloud Gateway** | API Gateway |
| **Eureka Discovery** | Service Registry |
| **OpenFeign** | Communication inter-services |
| **Spring Cloud Config** | Configuration centralisée |
| **Spring Data JPA** | Accès aux données |
| **Spring Data REST** | Exposition REST automatique |
| **H2/MySQL** | Base de données |



## Configuration des Services

### Configuration Eureka (pour chaque service)
```yaml
spring.cloud.discovery.enabled=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eyreka.instance.prefer-ip-address=true
management.endpoints.web.exposure.include=*
```

### Ports Utilisés

| Service | Port |
|---------|------|
| Discovery Service | 8761 |
| Gateway Service | 8888 |
| Config Service | 9999 |
| Customer Service | 8081 |
| Inventory Service | 8082 |
| Billing Service | 8083 |

---

## Démarrage de l'Application

### Ordre de Démarrage

1. **Config Service** 
2. **Discovery Service** (Eureka)
3. **Gateway Service**
4. **Customer Service**
5. **Inventory Service**
6. **Billing Service**


## Conclusion

Ce TP a permis de mettre en place une architecture microservices complète avec :

-  Séparation des responsabilités (Customer, Inventory, Billing)
-  Communication inter-services via OpenFeign
-  Découverte de services avec Eureka
-  Routage intelligent via Spring Cloud Gateway
-  Configuration centralisée avec Config Service
-  Scalabilité et résilience de l'architecture

---
