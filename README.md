# UpworkAlert

UpworkAlert is a job monitoring and analysis tool designed to help freelancers track and evaluate jobs on Upwork. It periodically fetches jobs based on custom criteria, provides email notifications, and allows for AI-assisted analysis of job descriptions.

## Features

- **Automated Job Fetching**: Regularly polls Upwork (via Apify or directly) for new jobs matching your specific criteria.
- **Criteria-based Filtering**: Filter jobs by hourly rate, fixed price, category, location, and keywords.
- **Email Notifications**: Get notified instantly when a relevant job is found (optional).
- **AI Chat Integration**: Interact with an AI (OpenAI) to analyze job details, ask questions about requirements, or draft proposals.
- **Modern Web Dashboard**: A responsive Angular-based frontend to view and manage job listings.

## Tech Stack

### Backend
- **Java 25**
- **Spring Boot 4**
- **MySQL**
- **Flyway**

### Frontend
- **Angular 21**
- **Bootstrap 5**

## Getting Started

### Prerequisites
- Java 25 JDK
- Node.js & npm (for frontend)
- MySQL Database
- External API keys:
  - **Apify Token**: Required for job scraping.
  - **OpenAI API Key**: Required for the AI chat feature.
  - **Upwork API Credentials** (optional, depending on client configuration).

### Configuration

1. Create a MySQL database named `upwork_alert`.
2. Configure your environment in `src/main/resources/application.yml`:
   - Update `spring.datasource` credentials.
   - Set `apify.token`.
   - Set `openai.api-key` and `openai.vector-store-id`.
   - (Optional) Configure `spring.mail` if email notifications are enabled.
   - Adjust `search.criteria` to match your target jobs.

### Running the Application

#### Backend
```bash
./mvnw spring-boot:run
```

#### Frontend
```bash
cd frontend
npm install
npm start
```
The dashboard will be available at `http://localhost:4200`.

## Project Structure

- `src/main/java/com/tm/upwork`: Backend source code.
  - `domain/alert`: Scheduled tasks for job monitoring.
  - `domain/chat`: AI chat integration logic.
  - `domain/job`: Job management and API clients (Apify/Upwork).
- `src/main/resources`: Application configuration and database migrations.
- `frontend`: Angular application.
  - `src/app/features`: Feature-based frontend modules (job list, chat).
