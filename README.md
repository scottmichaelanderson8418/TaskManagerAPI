
# TaskManagerAPI - README

## Step #01
1. Using Spring Tools Suite (or your preferred IDE), open TaskManagerAPI.
2. Run the TaskManagerAPI as a "Spring Boot App".
3. Use the Postman API Platform and import the following .json files into Postman. The files are located in the TaskManagerAPI directory.
  1. TaskManagerAPI.postman_collection.json
  2. TaskManagerAPI-USERS.postman_collection.json

## Step #02

1. Register your user at "http://localhost:8080/register/user".
2. Enter username, password, and role.
  - Note: the roles variable must be in the following format with no spaces:
    - "USER,ADMIN"
    - "ADMIN,USER"
    - "USER"
    - "ADMIN"

## Step #03
1. Login your user at "http://localhost:8080/login" 
2. You will be authenticated and you will be authorized to access the endpoints accessible with your specified role.


## For Users with "ADMIN"  
1. Only users with "ADMIN" role can access the following Endpoints
  - WHICH USER IS LOGGED IN --> "http://localhost:8080/admin/activeuser"
  - GET ALL USERS --> "http://localhost:8080/admin/getallusers"
  - GET USER BY ID --> "http://localhost:8080/admin/user/1"

