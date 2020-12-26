# deepsky-manager
DeepSky objects manager application for Advanced Techniques and Tools for Software Development exam, University of Florence, Cyber-Physical Systems of Systems course.

## Package the app without executing unit tests or integration tests
mvn clean package -DskipTests,skipITs

## Run Unit tests
mvn clean test

## Run Mutation tests
mvn clean test -Pmutation-tests

## Run Integration tests
mvn clean verify -Pintegration-tests

### Run Integration tests starting Docker container for MySql
mvn clean verify -Pdocker

# Run Integration tests on a specific test class
mvn -Dit.test=DeepSkyObjectWebControllerIT verify -Pdocker

## Run E2E tests
mvn clean test -Pe2e-tests

# Docker

## Run docker container pom.xml configuration (doesn't leave the container running)
mvn docker:start -Pdocker

## Run docker container using docker-compose.yml configuration
mvn clean package -Pdocker,e2e-tests
docker-compose up && docker-compose rm -fsv

# Coverage
[![Coverage Status](https://coveralls.io/repos/github/morganteg/deepsky-manager/badge.svg?branch=master)](https://coveralls.io/github/morganteg/deepsky-manager?branch=master)

# SonarCloud
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=bugs)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=code_smells)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=coverage)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=ncloc)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=alert_status)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=security_rating)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=sqale_index)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=morganteg_deepsky-manager&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=morganteg_deepsky-manager)