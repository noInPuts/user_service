# User Service
This service handles the user information such as name, email, address. 
To access a user, the request must include its JWT token cookie. 
This service verifies the JWT token by itself, by having identical keys with the Auth Service. 

## Documentation 
This repository contains UML diagrams in the docs/diagram folder.
Diagrams included are:
* Sequence Diagram

## Tech Stack
* Spring boot