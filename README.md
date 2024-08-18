# GitHub Repository and Branch Info Service

This project is a Spring Boot application that provides a REST API to fetch information about GitHub repositories and their branches. The service leverages `WebClient` for non-blocking requests to the GitHub API and uses Resilience4j for implementing fault tolerance patterns like Circuit Breaker, Retry, and Rate Limiter.

## Features

- **Fetch Repositories**: Retrieve non-fork repositories for a specified GitHub user.
- **Fetch Branches**: Retrieve branches of a specified repository.
- **Fault Tolerance**: Implements Circuit Breaker, Retry, and Rate Limiter patterns using Resilience4j.

## Project Structure

- **`config/Resilience4jConfig.java`**: Configuration for Resilience4j, including Circuit Breaker, Retry, and Rate Limiter.
- **`controllers/SearchReposController.java`**: REST controller that defines endpoints to fetch repositories and branches.
- **`dto/`**: Data Transfer Objects (DTOs) representing GitHub repository and branch data.
- **`service/SearchReposService.java`**: Interface defining methods for fetching repositories and branches.
- **`service/SearchReposServiceImpl.java`**: Implementation of the service interface using `WebClient` to interact with the GitHub API.

## Getting Started

### Prerequisites

- Java 17 or later
- Maven 3.8+ or Gradle 7+

### Building the Project

1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/your-repo-name.git
    cd your-repo-name
    ```

2. Build the project using Maven or Gradle:
    ```bash
    ./mvnw clean install
    # or
    ./gradlew build
    ```
The application will start on the default port 8080.

## API Endpoints

### Get Non-Fork Repositories

- Endpoint: /api/users/{username}/repos
- Method: GET
- Description: Fetches non-fork repositories for the specified GitHub user.
- Response:
```
[
  {
    "name": "repo-name",
    "ownerLogin": "username",
    "branches": [
      {
        "name": "branch-name",
        "commitSha": "commit-sha"
      }
    ]
  }
]
```
### Get Repository Branches
- Endpoint: /api/users/{username}/repos/{repoName}/branches
- Method: GET
- Description: Fetches branches for the specified repository of a GitHub user.
- Response:
```
[
  {
    "name": "branch-name",
    "commitSha": "commit-sha"
  }
]

```
### Resilience4j Configuration
The application uses Resilience4j to manage fault tolerance with the following configurations:

### Circuit Breaker:
Failure rate threshold: 50%
Wait duration in open state: 10 seconds
Sliding window size: 10
Retry:
Maximum attempts: 3
Wait duration: 500 milliseconds
Rate Limiter:
Limit for period: 10 requests
Refresh period: 1 second
These configurations are defined in Resilience4jConfig.java and applied to the GitHub API interactions in SearchReposServiceImpl.java.

### Error Handling
Rate Limit Exceeded: Returns HTTP 429 with a warning message when the GitHub API rate limit is exceeded.
User Not Found: Returns HTTP 404 if the specified user or repository is not found.
Contributing
Contributions are welcome! Please follow these steps:

### Fork the repository.
Create a new feature branch (git checkout -b feature/your-feature).
Commit your changes (git commit -m 'Add some feature').
Push to the branch (git push origin feature/your-feature).
Create a new Pull Request.

### Contact
For any inquiries, please reach out to daniel.shelest1@gmail.com
