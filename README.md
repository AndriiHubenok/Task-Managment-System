# Task Management System

A simple and efficient REST API for task management, developed in Java using Spring Boot. The system supports user registration, authentication (Basic Auth & JWT), task creation, assignee management, and commenting.

## 🚀 Key Features

* **Authentication & Authorization:**
    * New user account registration.
    * **Basic Auth** support for obtaining tokens.
    * **JWT** support for accessing protected resources.
* **Task Management:**
    * Create, view, and update task statuses.
    * Filter tasks by author and assignee.
    * Sort by creation date (newest first).
    * Assign users to specific tasks.
* **Comments:**
    * Add comments to tasks.
    * View the list of comments for a specific task.

## 🛠 Tech Stack

* **Java 21** 
* **Spring Boot 4.0.0** (Web, Security, Data JPA, Validation, OAuth2 Resource Server)
* **Database:** H2 (In-memory)
* **Build Tool:** Gradle

## ⚙️ Setup and Execution

The application will be available at: `http://localhost:8080`

### Database (H2 Console)
* **URL:** `http://localhost:8080/h2-console`
* **JDBC URL:** `jdbc:h2:file:../tms_db`
* **User:** `sa`
* **Password:** `sa`

---

## 📚 API Documentation

**Important:** All requests to `/api/tasks` and `/api/comments` require an authorization header (except for account creation and token retrieval):
`Authorization: Bearer <your_jwt_token>`

### 🔐 Authentication

| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/accounts` | Register a new user | `{ "email": "user@example.com", "password": "pass" }` |
| `POST` | `/api/auth/token` | Get JWT token (Basic Auth) | _(empty)_ |

---

### 📋 Tasks

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/tasks` | Get all tasks. <br>Parameters: `?author=email` or `?assignee=email`. |
| `POST` | `/api/tasks` | Create a new task. |
| `PUT` | `/api/tasks/{id}/status` | Update status (`CREATED`, `IN_PROGRESS`, `COMPLETED`). |
| `PUT` | `/api/tasks/{id}/assign` | Assign a user to the task. |

#### 📝 Task Creation Example (Body)
**POST** `/api/tasks`
```json
{
  "title": "Fix auth bug",
  "description": "Users cannot log in with '@' symbol in password"
}
