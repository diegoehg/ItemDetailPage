# How to Run This Project

## Prerequisites
- Docker
- Docker compose

## Project Structure
This repo contains two directories, frontend & backend, and each directory has
its own `Dockerfile`. Each `Dockerfile` is divided into two stages, for
building and for serving the application.

At the root of this repo, you can see a `docker-compose.yml` file that takes
each image and serves it, along with a Postgresql database. A volume `postgres_data`
was declared for storing the database data.

## How to Run It
You can run it  and build it like this:
```
docker compose up --detach --build
```

This is what you are going to see:
- Backend app has a `DataInitialization` config that will populate the database with some predetermined data.
- Front is served at `http://localhost:80`
- Back is served at `http://localhost:8080`

You can shut down the application by running the following command:
```
docker compose down --volumes
```
Flag `--volumes` or `-v` is for removing the volumes associated with the database. 