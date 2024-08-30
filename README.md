# Desk_Booking_App

## Overview

This repository currently contains the containerised back-end for an application I built for my 'Software Engineering Principles' university project at Northumbria University.
This was built to address a business need to update my organisation's legacy desk booking application, making it more scalable, flexible, and secure. 
The full project report, including the business case and in-depth analyses of various technologies and frameworks can be found under XXX.
This also outlines the reasons for the decisions made in this project, relationg to the full software development lifecycle:
- Agile methodologies
- Programming languages
- Databases
- Requirements gathering and engineering
- Security considerations
- Architecture
- Systems designs (high level and low level designs)
- Evaluation metrics
- Different test suites (unit, regression, integration, acceptance, performace etc.)
- Compliance and regulation

Implemented:
Spring Boot based REST APIs. 
PostgreSQL database with SQL constraints to enforce business rules. 
Docker Compose for container orchestration. 
A Bruno collection is provided to interact with the REST Endpoints
A unit test suite can be found under src/test/

Still to do:
React front-end.
More tests including end-to-end and integration tests.
Monitoring and logging.
Additional feautures. 


## Usage

**To run:**
docker-compose up --build

**To shut down:**
docker-compose down

