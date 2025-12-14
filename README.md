# Task Management System
This is a simple Task management system where user can create their tasks, view all tasks as well as track their tasks according to status, update, delete tasks. This system is build using SpringBoot 3.5.8, Java 17.

## Features
- Create tasks
- View all tasks or task by id
- Update task
- Update task's status
- Delete all tasks
- Delete task by id
- View task by status
- Pagination
- Exception Handling
- OpenApi Documentation

## Tech Stack
- Java 17
- Spring 3.5.8
- OpenAPI 2.8.14
- PostgreSQL

## Project Setup
### prerequisites
- Java 17+
- PosgreSQL

### Clone the repository
- git clone https://github.com/Asur-SujalShrestha/Task-Management-System
- cd Task-Management-System

### Configure Database
- Set application.properties:
  * spring.datasource.url=jdbc:postgresql://localhost:5432/Your_Database_Name
  * spring.datasource.username=postgres
  * spring.datasource.password=yourpassword
  * spring.jpa.hibernate.ddl-auto=update
 
### Clean and Run
- mvn clean install
- mvn spring-boot:run

## API Documentation
After running project Swagger UI is available in 
- http://localhost:8081/swagger-ui/index.html

### List of APIs
- POST /api/tasks/add-task – Add a new task
  
- GET /api/tasks/get-all-tasks?pageNumber=1&pageSize=10 – Get all tasks with pagination

- GET /api/tasks/get-task/{taskId} – Get task by ID
 
- PUT /api/tasks/update-task/{taskId} – Update an existing task

- PATCH /api/tasks/update-task-status/{taskId} – Update task status

- DELETE /api/tasks/delete-task/{taskId} – Delete task by ID

- DELETE /api/tasks/delete-all-tasks – Delete all tasks

- GET /api/tasks/get-tasks-by-status?status=PENDING – Get tasks by status

  <img width="1551" height="805" alt="Screenshot 2025-12-14 124823" src="https://github.com/user-attachments/assets/7b2e5bfb-2c79-4823-84dd-66422730a189" />


## Example of Request/Response of API

### Add Task (POST /api/tasks/add-task)

- Make POST http request on "/api/tasks/add-task"
- Input Raw JSON:
  ```json
  {
    "title":"Try from swagger",
    "description":"I am testing my API from Swagger.",
    "dueDate":"2025-12-19",
    "status":"PENDING"
  }

- Response:
    * Status: Status.Created (201)
    * Body:
      ```json
      {
        "id": 8,
        "title": "Try from swagger",
        "description": "I am testing my API from Swagger.",
        "status": "PENDING",
        "createDate": "2025-12-14",
        "dueDate": "2025-12-19"
      }
<img width="1832" height="804" alt="Screenshot 2025-12-14 125000" src="https://github.com/user-attachments/assets/847c8e64-eb8a-4a9f-b5a2-6f759784707c" />
<img width="1750" height="384" alt="Screenshot 2025-12-14 125102" src="https://github.com/user-attachments/assets/f43f304a-070a-47b7-aafa-42c646513140" />


### Get All Tasks (GET /api/tasks/get-all-tasks?pageNumber=1&pageSize=10)
- Make GET request on "/api/tasks/get-all-tasks?pageNumber=1&pageSize=10"
- Response:
  * Status: HttpStatus.OK (200)
  * Body:
    ```json
    [
      {
        "id": 4,
        "title": "Fourth task",
        "description": "This is the fourth task",
        "status": "IN_PROGRESS",
        "createDate": "2025-12-12",
        "dueDate": "2025-12-15"
      },
      {
        "id": 5,
        "title": "Fifth task",
        "description": "This is the fifth task",
        "status": "IN_PROGRESS",
        "createDate": "2025-12-12",
        "dueDate": "2025-12-15"
      },
      {
        "id": 6,
        "title": "Sixth task",
        "description": "This is the sixth task",
        "status": "IN_PROGRESS",
        "createDate": "2025-12-14",
        "dueDate": "2025-12-16"
      }
    ]

  <img width="1746" height="728" alt="image" src="https://github.com/user-attachments/assets/09d1a557-a135-4096-bb74-de853031b2d0" />


### Get task by id
- Make GET request on "/api/tasks/get-task/{taskId}"
- Send taskId as pathvariable
<img width="1823" height="440" alt="image" src="https://github.com/user-attachments/assets/2ee6d5e0-fd53-4949-9d0e-79f4e06f4091" />

  
- Reponse:
  * Response Status: Http.Status.OK (200)
  ```json
  {
  "id": 3,
  "title": "third Updated task",
  "description": "This is the third task",
  "status": "PENDING",
  "createDate": "2025-12-12",
  "dueDate": "2025-12-17"
  }
<img width="1753" height="383" alt="image" src="https://github.com/user-attachments/assets/55276b5b-5f48-4ccc-9654-353a7d47f742" />

### Update Task by id
- Make PUT request on "/api/tasks/update-task/{taskId}"
- Send taskId as path variable
- Send updated task details in request body
<img width="1803" height="656" alt="image" src="https://github.com/user-attachments/assets/8082c9b4-7aa6-44e8-912a-a710ca726f3b" />

- Reponse:
  * Response Status: HttpStatus.OK (200)
  ```json
  {
  "id": 3,
  "title": "Update task",
  "description": "I want to Update",
  "status": "IN_PROGRESS",
  "createDate": "2025-12-12",
  "dueDate": "2025-12-19"
  }

<img width="1754" height="392" alt="Screenshot 2025-12-14 135129" src="https://github.com/user-attachments/assets/20029ddb-e0f1-43bb-b410-887515ece03a" />

### Update task status
- Make PATCH request on "/api/tasks/update-task-status/{taskId}"
- Send taskId as path variable
- Send status as query parameter

<img width="1810" height="529" alt="image" src="https://github.com/user-attachments/assets/20adffd5-cb78-4da6-8a6e-5b489a4c807b" />

- Reponse: 
* Response Status: HttpStatus.OK (200)
  ```json
  {
  "id": 3,
  "title": "Update task",
  "description": "I want to Update",
  "status": "PENDING",
  "createDate": "2025-12-12",
  "dueDate": "2025-12-19"
  }

<img width="1759" height="381" alt="image" src="https://github.com/user-attachments/assets/9ae4d948-e66c-4eab-ac84-a559d3f08ccc" />

### Delete task by id
- Make DELETE request on "/api/tasks/delete-task/{taskId}"
- Send taskId as path variable

  <img width="1804" height="414" alt="image" src="https://github.com/user-attachments/assets/54410a7c-4b2e-4ef8-8b6d-16fd900bd592" />

  - Reponse:
    * Response status: HttpStatus.value (204)
    * Body: No content

<img width="1748" height="311" alt="image" src="https://github.com/user-attachments/assets/18e94995-8516-4ab7-b8fe-2cb6e0cb9af1" />


### Delete All tasks
- Make DELETE Request on "/api/tasks/delete-all-tasks"

- Response:
  * Response status: HttpStatus.NoContent (204)

### Get tasks by status

- Make GET request on "/api/tasks/get-tasks-by-status"
- Send status as a query parameter

  <img width="1790" height="408" alt="image" src="https://github.com/user-attachments/assets/8c746111-a819-4fe7-958f-acf33f638553" />

- Reponse:
  * Response status: HttpStatus.OK (200)
  * Body:
    ```json
    [
      {
        "id": 7,
        "title": "Try from swagger",
        "description": "I am testing my API from Swagger.",
        "status": "PENDING",
        "createDate": "2025-12-14",
        "dueDate": "2025-12-19"
      },
      {
        "id": 8,
        "title": "Try from swagger",
        "description": "I am testing my API from Swagger.",
        "status": "PENDING",
        "createDate": "2025-12-14",
        "dueDate": "2025-12-19"
      }
    ]

<img width="1748" height="555" alt="image" src="https://github.com/user-attachments/assets/9d0ac77e-812c-4f42-bf73-82f8551d78b5" />


## Postman testing collection

### Postman Collection
- Download the collection:
  * Open postman
  * Import and start testing
 
 ## Postman Pictures

### Add task
<img width="1076" height="778" alt="image" src="https://github.com/user-attachments/assets/c03523ed-1f87-4944-a7ef-294654a78903" />

### Get all tasks
<img width="1101" height="816" alt="image" src="https://github.com/user-attachments/assets/fdefbf2b-9520-4f40-9446-20cef63837d4" />

### Get task by id
<img width="1066" height="759" alt="image" src="https://github.com/user-attachments/assets/ba9a516a-e04a-4ecc-aa67-67b823dcae9d" />

### Update task
<img width="1067" height="776" alt="image" src="https://github.com/user-attachments/assets/7925efca-3666-4242-9034-4c489e0adb23" />

### Update task status
<img width="1068" height="747" alt="image" src="https://github.com/user-attachments/assets/a1b4ea1f-136c-4e60-b78a-e5f544ba8e91" />

### Delete Task
<img width="1066" height="637" alt="image" src="https://github.com/user-attachments/assets/198023ea-7e0c-44d9-9df4-e1eb787bec8b" />

### Ger all task by status
<img width="1062" height="815" alt="image" src="https://github.com/user-attachments/assets/6abb6eaf-7f8f-4e31-8e20-ab1d4bdecdaf" />


# END
