# ItemDetailPage

This is an application that shows a e-commerce site. Up to now, the application shows
a products list, that is prepopulated at running the application.

The stack used for this application is the following:
- React at the front
- Node version 18
- Spring Boot at the back
- Java 21
- PostgreSQL 15 as a database

I used this stack because I'm familiarized with it, it is a quite common stack
as far I could see.

Both parts of this application are dockerized, frontend and backend have their
corresponding `Dockerfile`. Each `Dockerfile` is divided into two stages, for
building and serving each part. You can see at the root a `docker-compose.yml`
for running the whole application. I feel that this is practical, given that
you can build & run the application using Docker Compose.