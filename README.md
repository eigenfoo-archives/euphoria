# Euphoria

A simple recruiting platform, written as a project for ECE366 (Software
Engineering & Large Systems Design) at The Cooper Union. Euphoria's tech stack
consists of a [Spotify Apollo](https://github.com/spotify/apollo) Java service
with a [MySQL](https://www.mysql.com/) database and a
[React.js](https://github.com/facebook/create-react-app) JavaScript web
application. 

Team members:
- [George Ho](https://github.com/eigenfoo)
- [Wendy Ide](https://github.com/wside)
- [Luka Lipovac](https://github.com/lipovac)
- [Ostap Voynarovskiy](https://github.com/ostapstephan)

## Requirements

- JDK 8
- Maven
- MySQL

## Installation and Setup

1. Clone this repository from GitHub

    ```bash
    git clone https://github.com/eigenfoo/euphoria.git
    ```

2. Initialize the database with the provided `initialize-database.sql` script.

    ```bash
    cat euphoria-service/src/main/resources/initialize-database.sql | mysql -u root -p
    ```

3. Download dependencies for, and run, the frontend:

    ```bash
    cd euphoria-frontend
    npm install
    npm start
    ```

   See the [Create React App
   README](https://github.com/eigenfoo/euphoria/blob/master/euphoria-frontend/README.md)
   for more information.

4. Package and run the backend.

    ```bash
    cd euphoria-service
    cat src/main/resources/initialize-database.sql | mysql -u root -p
    mvn package
    java -jar target/euphoria-1.0-SNAPSHOT.jar
    ```

## Documentation

See the [`docs/`
subdirectory](https://github.com/eigenfoo/euphoria/tree/master/docs) for more
documentation.

