# deepsky-manager
DeepSky objects manager application for Advanced Techniques and Tools for Software Development exam, University of Florence, Cyber-Physical Systems of Systems course.

## Run docker container
mvn docker:start -Pdocker

## Run Unit tests
mvn clean test

## Run Mutation tests
mvn clean test -Pmutation-tests

## Run Integration tests
mvn clean verify -Pintegration-tests
mvn verify -Pdocker
mvn -Dit.test=DeepSkyObjectWebControllerIT verify -Pdocker

## Run E2E tests
mvn clean test -Pe2e-tests

# Docker
docker-compose up --remove-orphans

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