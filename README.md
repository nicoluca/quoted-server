# Quoted (Server)
A Spring Boot server-side quote management application.

## Description
This is a simple quote management server-side application. It allows you to create, edit, delete and view quotes and their respective sources and persist them in a database. You can also review random quotes and search quotes and sources by keyword.

This server-side application provides REST APIs to be accessed by a client application. A JavaFX client-side application can be found [here](https://github.com/nicoluca/quoted-client).

## Features
- APIs and endpoints to create, edit, delete and view quotes
- Persist quotes and sources in a database

## User Manual
Please refer to the [Client-side User Manual](https://github.com/nicoluca/quoted-client/blob/master/UserManual.md).

## Requirements
- Java 20
- Maven to build the project including dependencies

## Dependencies
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Dev Tools
- Lombok
- Postgres
- (Spring Boot Test)

## Setup
1. Setup a local postgresql server. On MacOS, you can e.g. do that using [Homebrew](https://brew.sh) with the commands `brew install postgresql` and then `brew services start postgresql@14`. (Default user will be your local user name without a password.) See [here](https://www.postgresql.org/download/) for examples on how to do that on other platforms.
2. Setup a database named according to the application.properties file (default: `quote_db_server`) and a user with read and write permissions to that database.
3. Remove the suffix .example from src/main/resources/application.properties.example and replace USER and PASSWORD accordingly for both database and spring security.
4. (For testing, also remove the suffix .example from src/test/resources/application-test.properties.example and replace USER and PASSWORD accordingly for both database and spring security.)
5. To run the application, run `mvn spring-boot:run` in the project root directory.

## Notes
- Basic Auth is used for authentication.
- The application is designed to use a local postgresql database.
- Unit tests are designed to use a local postgresql database, too.