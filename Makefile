.PHONY: help build package deploy
.DEFAULT_GOAL = help

SHELL = bash

help:
	@printf "Usage:\n"
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

database:  # Initialize database and pre-populate with examples.
	cat euphoria-service/src/main/resources/initialize-database.sql | mysql -u root -p

all: install frontend backend database # Alias for `make install frontend backend database`.

deploy:  # Deploy euphoria. Ensure that latest build is in euphoria-frontend/build.
	sudo cp -r euphoria-frontend/build/* /var/www/club.euphoria_recruiting/
	sudo nginx -t
	sudo systemctl restart nginx
	nohup java -jar euphoria-1.0-SNAPSHOT.jar > /dev/null 2>&1 &
