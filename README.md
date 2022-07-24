# DOCKER+SPRING PROJECT

Better to fork this project to your own github account and then clone it
locally. 
If you clone this repo directly to your local machine,
and want to push changes, do it in a new branch.

### Project has two microservices: msvc-cursos / msvc-usuarios

They comunicate each other with openFeign. Is possible to make
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
to your local machine values, and in docker-compose.yaml
change enviroment variables from mysql and postgres. (password, ports, etc) **

Probably you'll need to install PGADMIN or similiar for POSTGRES DB
and WORKBENCH or similar for MYSQL DB.



- RUN in CMD:
***docker-compose up -d***

# TESTING

NOTE 1 of 2: Is possible to delete and modify "cursos" and
"usuarios" but don't waste time on that, just check
that the proyect is deployed correctly in your
local machine.


Once all containers are runnning, add data to the 
database.

## For msvc-usuarios:
###### Add users(change values):
**http://localhost:8001**

- POST


          {
          "nombre": "Franco",
          "email": "franco@bov222",
          "password": "1234"
          }

## For msvc-cursos:

###### Add "cursos", change "nombre" value

http://localhost:8002
- POST

        {
        "nombre": "Spring"
        }

    
###### Add users to specific "curso"
- PUT
http://localhost:8002/asignar-usuario/{curso-id}
(change curso-id value in url)


          {
          "id": 3,
          "nombre": "Franco",
          "email": "franco@bov222",
          "password": "1234"
          }

###### See specific "curso" details with students added
- GET
http://localhost:8002/{curso-id}
(change curso-id value in url)

###### Create users from msvc-cursos 
- POST
http://localhost:8002/crear-usuario/1
(Creates user that saves in msvc-usuarios
and msvc-cursos)


          {        
          "nombre": "Jane",
          "email": "jane@did123123",
          "password": "12345"
          }





Note 2 of 2: the project is not complete, some exceptions
handling is missing, the goal of this project is to test docker. 