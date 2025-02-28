# Load Testing Framework

The Test Harness is in two components:

Backend API component that accepts load testing requests from via API endpoint and triggers Gatling load testing tool with parameters passed into the API call.

Frontend application - UI that allows you to pick parameters and submit load testing request via Web.

## Backend design

Data Extractor component is built using Spring Boot to fetch data from the PLR datastore and create messages based on the types selected by the user.
MavenInvoker collates user inputs, fetches test data and invokes Gatling Simulation.
Gatling.io is used for sending high volume messages. This component would fire message requests to the endpoints chosen by the user.
The REST APIs invoked by the simulation are authenticated via Keycloak using IDIR.

A Gatling report is created which will have 'Run Information Description' about following variables passed from UI:
runType, hasCPN, hasIPC, users, records, pause, environment, spec etc.

## Frontend

Front End Web is built using Vaadin and Spring-boot. This captures form inputs and triggers backend components based on user actions.

# Development Requirements

- Git client
- OpenJDK 21
- Maven
- Docker
- Java Development IDE such as VS Code, Eclipse, IntelliJ IDEA
- Oracle Development Tool such as SQL Developer

	
# Instructions:
	
- Pull code from github:
- Inject the copies of application.properties file for both backend and frontend :
    backend/src/main/resources/application.properties
    frontend/src/main/resources/application.properties
- Start the backend component by running the following command when you're in backend directory in the source code: `mvn spring-boot:run`. This will build and start the backend component that will be available on port 9080.
- Start the frontend component by running the following command when you're in frontend directory in the source code: `mvn spring-boot:run`. This will build and start the frontend component that will be available on port 9090. It also pops up the application in a new browser window.

## Instructions for docker and docker compose

- Pull code from github.
- Download Docker desktop and login with your ID.
- Copy the environment files backend.env and frontend.env file for backend and frontend in /compose folder:
    /compose/backend.env
    /compose/frontend.env
- Start the backend and frontend component by running the following command when you're in /compose directory: 'docker compose up'.
  This will build and start the components. Backend will be available on port 9080 and frontend will be available on port 9090.
- Execute PLR Test Harness in a specific environment (For example, MERGE environment using merge ESB endpoint and merge database) and can fetch the Gatling reports at: 'moh-plr-loadtest/compose/gatling/'
- Templates for environment files are available at compose/backend-template.env and compose/frontend-template.env 

## Run app on local (Docker or Podman)
To start the stack using docker or podman you can use provided cmd files or simply run 'docker compose up' or 'podman compose up' commands from compose directory. 