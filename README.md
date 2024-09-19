
INFORMATION AND LIMITATIONS :
-----------------------------------------------------------------------------------------------

#) Watch Dog
I added a capability of watchdog - in case of failure in connectivity with the data base then
a part of handling the exception which will be thrown the watchdog will persist the CRUD action
and the entity itself in a File format in hard drive . Also I will schedule mechanism which will
read those files and parse them and update the data base accordingly .


#) Eventual consistency
In case of failure in the DB for several minutes and entities will be saved into files
then DB will return and client will  run GET user api call before the watchdog will save entities
to DB from files then in this GAP the return info will not be accurate and not updated


#)  SQLite
In order that we will backup all the CRUD operations include Get user we will need to duplicate the data base (SQLite)
and the in case of DB failure we will fetch from backup DB. I will add a job that will sync between those two data bases
every several seconds . the problem is duplication of the data base .


#) AWS  Aurora
Another solution can be choosing AWS  Aurora DB (disadvantage - more expensive)
but it is very fast and has capabilities of backup , always has a capability of run more instances
scale up and scale down .and if it fall then it will be able to recover very quickly


INSTRUCTIONS :
-----------------------------------------------------------------------------------------------

#) Please download the postman collection from the "resources" package
#) Please update the path in class of "WatchdogFileService" - >  consider your OS


HOW TO RUN THE MYSQL DATA BASE CONTAINER :
-----------------------------------------------------------------------------------------------
#) Open terminal on the root of this project 
#) Run the following command:
docker-compose up -d


HOW TO RUN THE DOCKER CONTAINER WHICH RUN THE JAR OF THIS SERVICE :
-----------------------------------------------------------------------------------------------
#) Open the terminal

#) Navigate to the following path :
C:\Dev\Java\home-work\users-managment\src\main\resources

#) Run the following command to create docker image :
docker build -t   user-managment  .

#) Run the following command to run the docker image and create the container:
docker run --name user-managment-container -p 8080:8080 user-managment
~~~~
