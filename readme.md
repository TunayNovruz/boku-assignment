Make sure you have Java JDK, Maven or Gradle, and Spring Boot installed.
Clone the repository:
```
git clone https://github.com/TunayNovruz/boku-assignment.git
cd payment-integration
```
Build the project:
```
mvn clean install
```
Configure the application properties:
Open src/main/resources/application.properties and update any relevant configuration settings such as server port, credentials, and URLs.
Run the Spring Boot application:
```
mvn spring-boot:run
```
Access the application at http://localhost:8080 in your web browser.

Possible improvements:
* implement retry strategy rather than adding responses to queue again.
* Store failed/success/waiting transactions in db
* implement dead letter queue for failed transactions
