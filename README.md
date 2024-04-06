# **JWT Spring Boot Security**
-----
## **Application Details**
- This application utilizes JSON Web Token (JWT)-based authentication to secure endpoints. Member obtains a JWT token upon successful register/login which are then used to access endpoints. 
- Under the "Configuration/SecurityConfiguration" it can be seen that "/login/**" and "/register/**" endpoints don't need authentication while the "/admin/**" is only accessible to 'ADMIN' roles.
- A custom filter was utilized in order to authenticate incoming request using JWT tokens. The filter is responsible for intercepting incoming requests, extracting JWT tokens from the "Authorization" header, validate them, and set up authentication.
- Password encryption was utilized in order to securely store passwords in a database. MySQL was used for this project.
-----
## **Technology Used**
- Spring Boot
- Postman
- MySQL
- Java
- PopSQL
-----
## **Examples Images**
###### Below I have included images of requests and their results.
- Image 1: MEMBER registration.
    - <img src="https://github.com/Andi-Cast/FirstSpringBootApplication/blob/main/ExampleScreenshots/GET_Example.png" height="auto" width="75%" >
- Image 2: MEMBER login.
    - <img src="https://github.com/Andi-Cast/FirstSpringBootApplication/blob/main/ExampleScreenshots/POST_Example.png" height="auto" width="75%" > 
- Image 3: DEMO endpoint.
    - <img src="https://github.com/Andi-Cast/FirstSpringBootApplication/blob/main/ExampleScreenshots/DELETE_Example.png" height="auto" width="75%" >
- Image 4: ADMIN only endpoint access denied by MEMBER.
    - <img src="https://github.com/Andi-Cast/FirstSpringBootApplication/blob/main/ExampleScreenshots/PUT_Example.png" height="auto" width="75%" >
- Image 5 & Image 6: Logout and demonstration how access is denied due to token being logged out.
  - <img src="https://github.com/Andi-Cast/FirstSpringBootApplication/blob/main/ExampleScreenshots/PUT_GET_Example.png" height="auto" width="75%" >
- Image 6: ADMIN registration.
  - <img src="https://github.co
- Image 8: ADMIN only endpoint.
  - <img src="https://github.co
    -----

