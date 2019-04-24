# Euphoria Recruiting Platform: [euphoria-recruiting.club](http://euphoria-recruiting.club/)

[![Build Status](https://travis-ci.org/eigenfoo/euphoria.svg?branch=master)](https://travis-ci.org/eigenfoo/euphoria)

A simple recruiting platform, written as a project for ECE366 (Software
Engineering & Large Systems Design) at The Cooper Union. Euphoria's tech stack
consists of a [Spotify Apollo](https://github.com/spotify/apollo) Java service
with a [MySQL](https://www.mysql.com/) database and a
[React.js](https://github.com/facebook/create-react-app) JavaScript web
application. 

Team members: [George Ho](https://github.com/eigenfoo), [Wendy Ide](https://github.com/wside), [Luka Lipovac](https://github.com/lipovac) and [Ostap Voynarovskiy](https://github.com/ostapstephan)

## Requirements

- [Oracle JDK 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- Maven
- MySQL
- Node.js and `npm`

```bash
sudo apt install nodejs npm maven mysql-server
```

## Installation and Setup

1. Clone this repository from GitHub

    ```bash
    git clone https://github.com/eigenfoo/euphoria.git
    cd euphoria/
    ```

2. Initialize the database with the provided `initialize-database.sql` script.

    ```bash
    cat euphoria-service/src/main/resources/initialize-database.sql | mysql -u root -p
    ```

3. Initiliaze the example files by manually copying the contents of
   [`docs/exampleFiles`
   subdirectory](https://github.com/eigenfoo/euphoria/tree/master/docs/exampleFiles)
   into `~/.euphoria/`. You will need to manually create the `~/.euphoria`
   directory.

   ```bash
   mkdir ~/.euphoria/
   cp docs/exampleFiles/* ~/.euphoria/
   ```

4. Download dependencies for, and build, the frontend:

    ```bash
    cd euphoria-frontend/
    npm install
    npm run build
    ```

   (See the [Create React App
   README](https://github.com/eigenfoo/euphoria/blob/master/euphoria-frontend/README.md)
   for more information.)

   If serving the frontend on a virtual machine, it may be wiser to build the
   web application locally and then `scp` the built static files to the
   `/var/www/euphoria.ece366.cooper.edu` directory in the virtual machine. You
   may need `sudo` privileges to do this.

   ```bash
   cd build/
   scp -r * root@VM_IP_ADDRESS:/var/www/euphoria.ece366.cooper.edu
   ```

5. If serving the frontend on a virtual machine, overwrite the file
   `/etc/nginx/sites-available/default` with [the `nginx/default`
   file](https://github.com/eigenfoo/euphoria/blob/master/nginx/default). Then
   restart `nginx` with `sudo /etc/init.d/nginx reload`.

   Before restarting `nginx`, you can test your configuration with `sudo nginx
   -t`.

6. Package and run the backend.

    ```bash
    cd ../../euphoria-service
    mvn clean package
    java -jar target/euphoria-1.0-SNAPSHOT.jar
    ```

## Documentation

See the [`docs/`
subdirectory](https://github.com/eigenfoo/euphoria/tree/master/docs) for more
documentation.
