# Euphoria

[![Build Status](https://travis-ci.org/eigenfoo/euphoria.svg?branch=master)](https://travis-ci.org/eigenfoo/euphoria)

> Our web application is served at
> [199.98.27.117:8000](http://199.98.27.117:8000/). We eventually intend to set
> up the [euphoria-recruiting.club](http://euphoria-recruiting.club/) domain
> name (hence the project structure).

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

Simply

```bash
git clone https://github.com/eigenfoo/euphoria.git
cd euphoria/
make all
```

This will:

1. Install `euphoria`'s dependencies
2. Build the frontend web application and backend service
3. Initialize the filesytem and database, and pre-populate it with examples.

See the [installation and setup
documentation](https://github.com/eigenfoo/euphoria/blob/master/docs/installation-setup.md)
for more detailed instructions on how to manually install and setup `euphoria`.

## Documentation

See the [`docs/`
subdirectory](https://github.com/eigenfoo/euphoria/tree/master/docs) for more
documentation.
