# ReservationService

### Description
The Reservation Service is a Spring Boot application that provides RESTful APIs for managing restaurant reservations. It includes functionalities for creating, updating, canceling, and retrieving reservations.

### Prerequisites
- Java 17
- Maven
- An IDE (e.g., IntelliJ IDEA)

### Starting the Application
- make sure that the restaurant service is up and runing before this steps for the integration test
- mvn clean package
- run the application
- The application will start on http://localhost:8081.

### Make sure to start the RestaurantService application before running the tests.
You can find the RestaurantService application at this url: [http://....](https://github.com/abakkari/restaurant-service/tree/main)

### Postman collection
You can use postman collection "Reservation micro service.postman_collection.json" to test the application

### Swagger UI Documentation
The Swagger UI documentation is available at http://localhost:8081/swagger-ui.html.

### Test scenario using postman collection ( run test scenario for Restaurant before this test to create restaurnt and tables before creating a reservation )
- Create customer
- Create reservation
- Create a seconde reservation
- Get reservations per cutomer id
- Cancel a reservation
