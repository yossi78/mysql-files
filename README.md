
INFORMATION AND LIMITATIONS:
---------------------------------------------------------------------------------------------------------


# WATCH-DOG:
I have implemented a watchdog mechanism to handle database connectivity issues.
If a failure occurs, the watchdog will capture the exception and persist the CRUD action 
and the entity in a file on the hard drive. Additionally, a scheduled task will read these files,
parse their contents, and update the database accordingly.

# EVENTUAL CONSISTENCY :
If there is a database failure lasting several minutes and entities are saved to files during this time,
there could be a discrepancy when the client makes a GET user API call. In this gap, 
the returned information might be outdated or inaccurate until the watchdog updates the database 
with the data from the files.

# SQLite:
To ensure backup for all CRUD operations, including GET user, we will duplicate the database using SQLite.
In the event of a database failure, we will retrieve data from the backup database. 
I will implement a job to synchronize the two databases every few seconds. 
However, the challenge here is managing the duplication of the database.

# AWS  AURORA:
An alternative solution is to use AWS Aurora DB, which, 
while more expensive, offers fast performance and built-in backup capabilities. 
It supports scaling up and down with ease, and if a failure occurs, it can recover quickly.


# INSTRUCTIONS:
---------------------------------------------------------------------------------------------------------
#) Please download the Postman collection from the "resources" package.
#) Update the path in the "WatchdogFileService" class according to your operating system.



3 HOW TO RUN THE MYSQL DATABASE CONTAINER:
---------------------------------------------------------------------------------------------------------

#) Open a terminal at the root of the project

#) Run the following command:
    docker-compose up -d



---------------------------------------------------------------------------------------------------------
