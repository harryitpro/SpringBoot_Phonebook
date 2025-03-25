# Project Structure:
phonebook/   
├── src/  
│   ├── main/  
│   │   ├── java/   
│   │   │   └── com/harryitpro/phonebook/    
│   │   │       ├── PhonebookApplication.java  (Main class)    
│   │   │       ├── model/   
│   │   │       │   └── Contact.java          (POJO)    
│   │   │       └── controller/   
│   │   │           └── PhonebookController.java (REST Controller)    
│   │   └── resources/    
│   │       └── application.properties        (Configuration)  
│   └── test/                                 (Tests, ignored here)  
├── pom.xml                                   (Maven config)  
└── .gitignore  


##  Test the API
Use curl, Postman, or a browser:

#### Get All Contacts
1. curl http://localhost:8080/api/contacts  
   Response:  
   [  
   {"id": 1, "name": "Alice", "phoneNumber": "123-456-7890"},  
   {"id": 2, "name": "Bob", "phoneNumber": "098-765-4321"}  
   ]  
2. #### Get Contact by ID
   curl http://localhost:8080/api/contacts/1  
   Response:  
   {"id": 1, "name": "Alice", "phoneNumber": "123-456-7890"}  
3. #### Add a New Contact  
   curl -X POST -H "Content-Type: application/json" -d '{"name":"Charlie","phoneNumber":"555-555-5555"}' http://localhost:8080/api/contacts   
   {"id": 3, "name": "Charlie", "phoneNumber": "555-555-5555"}  
4. #### Update a Contact  
   curl -X PUT -H "Content-Type: application/json" -d  
   Response:  
   {"id": 1, "name": "Alice Updated", "phoneNumber": "111-222-3333"}  
5. Delete a Contact  
   curl -X DELETE http://localhost:8080/api/contacts/1  
   Response: (204 No Content, no body)  

Explanation  
Spring Boot: Provides an embedded Tomcat server and simplifies configuration.  
Contact POJO: Represents a phonebook entry with id, name, and phoneNumber.  
PhonebookController: Handles REST endpoints:  
GET /api/contacts: List all contacts.  
GET /api/contacts/{id}: Get a specific contact.  
POST /api/contacts: Add a new contact.  
PUT /api/contacts/{id}: Update an existing contact.  
DELETE /api/contacts/{id}: Remove a contact.  
In-memory Storage: Uses an ArrayList instead of a database for simplicity.  

Enhancements (Optional)  
Add a Database: Use Spring Data JPA with H2 or MySQL.  
Add dependency: spring-boot-starter-data-jpa and h2 (or another DB).  
Annotate Contact with @Entity and @Id.  
Validation: Add @NotNull and @Valid for input validation (requires spring-boot-starter-validation).  
Frontend: Create a simple HTML/Thymeleaf page to interact with the API.  
Error Handling: Use @ControllerAdvice for centralized exception handling.  
This is a basic RESTful phonebook app to get you started.  
