.PHONY: help install frontend backend storage build all deploy
.DEFAULT_GOAL = help

SHELL = bash

help:
	@echo "Usage:"
	@grep -E '^[a-zA-Z_-]+:.*?# .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?# "}; {printf "\033[1;34mmake %-10s\033[0m%s\n", $$1, $$2}'

install:  # Install Node dependencies with npm.
	( \
	cd euphoria-frontend/; \
	npm install; \
	)

frontend:  # Build React web application with npm.
	( \
	cd euphoria-frontend/; \
	npm run build; \
	)

backend:  # Package Spotify Apollo service with maven.
	( \
	cd euphoria-service/; \
	mvn clean package; \
	)

storage:  # Initialize filesystemm and database and pre-populate with examples. Requires MySQL root password.
	mkdir -p ~/.euphoria
	cp -r docs/example-files/* ~/.euphoria/
	cat euphoria-service/src/main/resources/initialize-database.sql | mysql -u root -p

build: frontend backend  # Alias for `make frontend backend`.

all: install frontend backend storage # Alias for `make install frontend backend storage`.

deploy:  # Deploy euphoria. Requires sudo privileges.
	sudo mkdir -p /var/www/club.euphoria_recruiting/
	sudo rm -r /var/www/club.euphoria_recruiting/*
	sudo cp -r euphoria-frontend/build/* /var/www/club.euphoria_recruiting/
	sudo nginx -t
	sudo systemctl restart nginx
	pkill java
	nohup java -jar euphoria-service/target/euphoria-1.0-SNAPSHOT.jar > /dev/null 2>&1 &

clean:  # Remove Node modules and built web application and service.
	rm -rf euphoria-frontend/build/ euphoria-frontend/node_modules/ euphoria-service/target/
