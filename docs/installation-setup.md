# Installation and Setup

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
   [`docs/example-files`
   subdirectory](https://github.com/eigenfoo/euphoria/tree/master/docs/example-files)
   into `~/.euphoria/`. You will need to manually create the `~/.euphoria`
   directory.

   ```bash
   mkdir ~/.euphoria/
   cp -r docs/example-files/* ~/.euphoria/
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
   `/var/www/club.euphoria_recruiting` directory in the virtual machine. You may
   need `sudo` privileges to do this.

   ```bash
   cd build/
   scp -r * root@VM_IP_ADDRESS:/var/www/club.euphoria_recruiting
   ```

5. If serving the frontend on a virtual machine, overwrite the file
   `/etc/nginx/sites-available/default` with [the `nginx/default`
   file](https://github.com/eigenfoo/euphoria/blob/master/nginx/default). Then
   restart `nginx` with `sudo systemctl restart nginx`.

   Before restarting `nginx`, you can test your configuration with `sudo nginx
   -t`.

6. Package and run the backend.

    ```bash
    cd ../../euphoria-service
    mvn clean package
    java -jar target/euphoria-1.0-SNAPSHOT.jar
    ```
