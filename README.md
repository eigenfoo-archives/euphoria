# Euphoria

A simple recruiting platform web application, written for ECE366 at The Cooper Union.

Team members:
- [George Ho](https://github.com/eigenfoo)
- [Wendy Ide](https://github.com/wside)
- [Luka Lipovac](https://github.com/lipovac)
- [Ostap Voynarovskiy](https://github.com/ostapstephan)

## Requirements

- JDK 8
- MySQL
- Maven

## Installation and Setup

1. Clone this repository from GitHub

```bash
git clone https://github.com/eigenfoo/euphoria.git
```

2. Initialize the database with the provided `initialize-database.sql` script.

```bash
cat euphoria-service/src/main/resources/initialize-database.sql | mysql -u root -p
```

3. Download dependencies for the frontend:

```bash
cd euphoria-frontend
npm install
npm start
```

4. Test, compile and package the backend:

```bash
TODO
```
