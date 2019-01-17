# UserMicroservice
Creation of a User Microservice in Java

This test has been developped using Spring Boot.
Spring MVC has been used to provide a REST API, allowing customers to Create, Read, Update and Delete users

Running the application will start a Tomcat server on port **8585**.

The REST API can be used as described below : 

_**Create a User**_

POST : **http://localhost:8585/user/create**
accepting a JSON body such as  

{  "firstname":"Thierry",
    "lastname":"Henry",
    "email":"thierry@gmail.com",
    "nickname":"King Henry",
    "country":"France",
    "password":"henry"
}

All fields must be given in order to create the user.

**Response**

Return a 200 with the user created if the operation is successful

Return a 400 if something is missing in the input

Return a 409 if user with this email is already existing

_**Update a User**_

POST : **http://localhost:8585/user/update**
accepting a JSON body such as  

{  "firstname":"Thierry",
    "lastname":"Henry",
    "email":"thierry@gmail.com",
    "nickname":"King Henry",
    "country":"England",
    "password":"henry"
}


All fields must be given in order to create the user.
The password and the email cannot change.
If the password provided is invalid, the user won't be updated

**Response**

Return a 200 with the user updated if the operation is successful

Return a 400 if something is missing in the input

Return a 401 if the password is invalid

Return a 404 if the user is not found

_**Get a specific User**_

GET : **http://localhost:8585/user/{{email}}**

The email must be given in the URL

**Response**

Return a 200 with the user if the operation is successful

Return a 404 if the user is not found


_**Get all Users**_

GET : **http://localhost:8585/user/getAll**

**Response**

Return a 200 with a list of users if the operation is successful

_**Get a list filtered Users**_

GET : **http://localhost:8585/user/getUsers?criteria=country&value=France**

The criteria name and the value of the criteria must be given as query parameters

**Response**

Return a 200 with a list of users if the operation is successful

Return a 400 if criteria and value are not provided

_**Delete a specific User**_

DELETE : **http://localhost:8585/user/{{email}}**

The email must be given in the URL

**Response**

Return a 200 with true if the user has been deleted

Return a 404 if the user is not found


# How to run/test the application

_**Test the application**_

The application has been build with Gradle 4.

Unit Test have been implemented around the REST controller and the business layer.

To run the Unit Test : **gradle test** , you can run it with --info to have more details about the tests run.

To build the application : **gradle build**, it will generate a jar in the **build/libs** folder


_**Run the application**_

To run the application : **java -jar build/libs/microservice-0.0.1-SNAPSHOT.jar**

It will set up a Tomcat running on port 8585.



_**How to consume the API**_

I've created some shell scripts that will run some curl commands to create, update, get and delete users.

You can find them in the **user_scripts** folder

To run these scripts just use the "sh" command, e.g **sh create_user.sh**

To test the getUsers query, I would suggest to use a browser :

e.g **http://localhost:8585/user/getUsers?criteria=country&value=France**


# Technical decisions

My assumptions :

 - A user cannot be created if any information is missing or a user with the same email exists
 - A user cannot be updated if the password is invalid

I basically considered the email address as an identifier for the user, which is why the REST API accepts email in the URL for getting and deleting user.
With more time, I would have created a simple userID, I believe it's following more closely REST principles.

Regarding the notification to eventual others microservices, I believe that implementing the message bus is more relevant than sending another HTTP request to another API.

Sending a message on a bus gives the responsability to other microservice to retrieve the information. They just have to listen on interrested topics and retrieve the message.

I think it's better, because in case of a REST API communication, the user microservice would have to know all others microservices interrested in it in order to send a request.

Here, the user microservice just send a message on the bus and doesn't have to know who is interrested by this message.

Because of time, I've only implemented a blocking queue, but I would have used Kafka or Rabbit otherwise.
**In a ideal world**, I would not have implemented a simple cache, but a simple SQL DB.

Taking scalability into consideration, the cache could not be used, we need a one DB per user microservice with replication of data.
And I would put a load balancer in front of the services.

