# Load Testing Framework

The Test Harness is in two components:

Backend API component that accepts load testing requests from via API endpoint and triggers Gatlin load testing tool with parameters passed into the API call.

Frontend application - UI that allows you to pick parameters and submit load testing request via Web.

## Backend design

Add some content describing the backend incl. data extract from DB, triggerring Gatling, API structure

## Frontend

Add some content describing the frontend component.

# Development Requirements

- Git client
- OpenJDK 21
- Maven
- Docker
- Java Development IDE such as VS Code, Eclipse, IntelliJ IDEA
- Oracle Development Tool such as SQL Developer

	
# Instructions:
	
- Pull code from github:
- Inject the copies of application.properties file for both backend and fronend:
    backend/src/main/resources/application.properties
    frontend/src/main/resources/application.properties
- Start the backend component by running the following cammand when you're in backend directory in the source code: `mvn spring-boot:run`. This will build and start the backend component that will be available on port 9080.
- Start the frontend component by running the following cammand when you're in frontend directory in the source code: `mvn spring-boot:run`. This will build and start the frontend component that will be available on port 9090. It also pops up the application in a new browser window.

## Instructions for docker and docker compose

To be provided
