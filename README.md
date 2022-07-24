# DOCKER+SPRING PROJECT

###### Project has two microservices: msvc-cursos / msvc-usuarios

They comunicate each other with opengeign. Is possible to make
CRUD with both microservices.

Running the project:

###### MSVC-CURSOS:
_create .env file in ./msvc-cursos/ and add:_

PORT=8002
DB_HOST=postgres14:5432
DB_DATABASE=msvc_cursos
DB_USERNAME=postgres
DB_PASSWORD=
USUARIOS_URL=msvc-usuarios:8001


###### MSVC-USUARIOS:

_create .env file in ./msvc-usuarios/ and add:_

PORT=8001
DB_HOST=mysql8:3306
DB_DATABASE=msvc_usuarios
DB_USERNAME=root
DB_PASSWORD=
CURSOS_URL=msvc-cursos:8002

**Define values of database user and password according
to your local machine values**


RUN in CMD:
***docker-compose up -d***

