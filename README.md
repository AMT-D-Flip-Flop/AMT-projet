# DFlipFlop
![GitHub](https://img.shields.io/github/license/AMT-D-Flip-Flop/AMT-projet)
![GitHub repo size](https://img.shields.io/github/repo-size/AMT-D-Flip-Flop/AMT-projet)
![GitHub issues](https://img.shields.io/github/issues/AMT-D-Flip-Flop/AMT-projet)
![GitHub pull requests](https://img.shields.io/github/issues-pr/AMT-D-Flip-Flop/AMT-projet)
[![CICD Pipeline](https://github.com/AMT-D-Flip-Flop/AMT-projet/actions/workflows/CICD.yml/badge.svg?branch=main)](https://github.com/AMT-D-Flip-Flop/AMT-projet/actions/workflows/CICD.yml)



DFlipFLop est une application e-commerce web qui offre à ses clients une large gamme de produits informatique.

## Prérequis
**Développement**
- MySQL 5.7+ ou MariaDB 10.2+
- Java 8
- Maven

**Déploiement**
- Ubuntu 20.04

## Installation
- Cloner le repo

    ```bash
    git clone https://github.com/AMT-D-Flip-Flop/AMT-projet
    ```

- Créer une base de données ainsi qu'un utilisateur avec les droits sur celle-ci
  - Le dossier *Setup* met à disposition un docker compose permettant d'automatiser cette partie.

  
- Modifier le fichier *src/main/application.properties* avec la base de données et les identifiants de l'utilisateur


- Lancer le projet:
    ```bash
    ./mvnw spring-boot:run
    ```



- L'application est ensuite accessible à l'adresse http://localhost:8080

## Déploiement

Pour déployer l'application, copiez le script *Setup/server-setup.sh* sur le serveur et suivez la procédure d'installation.

Sur la machine de build, créer un fichier **settings.xml** dans le dossier **.m2** de votre home directory qui contient les informations de connexion à votre serveur Tomcat
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>TomcatServer</id>
      <username></username>
      <password></password>
    </server>
  </servers>
</settings>
```
Remplissez la partie username et password avec vos identifiants Tomcat.

Modifiez aussi le fichier pom.xml avec l'adresse de votre serveur (partie build).

Démarrez le déploiement:
``
 ./mvnw tomcat7:deploy
``

## Contribuer
Rendez vous sur la partie [collaboration](https://github.com/AMT-D-Flip-Flop/AMT-projet/wiki/Collaboration) du wiki pour vous renseigner sur le workflow pour contribuer au projet.

N'oubliez pas de créer des tests en fonction des nouvelles fonctionnalités ajoutées.

## Licence
[MIT](https://choosealicense.com/licenses/mit/)
