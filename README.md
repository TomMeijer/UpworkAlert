# Upwork Alert

Upwork Alert is a Spring Boot application that periodically monitors Upwork for new job postings based on custom search criteria and sends email notifications when matching jobs are found.

## Features

- **Automated Job Search**: Periodically fetches job postings from Upwork using the GraphQL API.
- **Customizable Criteria**: Filter jobs by hourly rate, fixed price, categories, locations, and keywords.
- **Email Notifications**: Sends detailed emails for new jobs, including title, description, and link.
- **Deduplication**: Ensures that the same job is not notified multiple times (using a local in-memory cache).

## Prerequisites

- **Java 21** or higher.
- **Maven 3.6+**.
- **Upwork API Credentials**: You need a `client_id` and `client_secret` from the Upwork Developer Portal.
- **SMTP Server**: An email account to send notifications (e.g., Gmail, SendGrid).

## Configuration

Configure the application by editing `src/main/resources/application.properties`.

### Upwork API Settings

```properties
upwork.api.client-id=YOUR_CLIENT_ID
upwork.api.client-secret=YOUR_CLIENT_SECRET
```

### Search Criteria

Adjust these values to match your job preferences:

```properties
search.criteria.min-hourly-rate=35
search.criteria.min-fixed-price=100
search.criteria.category-ids=531770282580668418
search.criteria.locations="Europe","Asia","Oceania"
search.criteria.searchExpression=(Java OR Spring OR Angular)
```

### Email Settings

Configure your SMTP server for sending alerts:

```properties
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=YOUR_PASSWORD
spring.mail.from=your-email@example.com
spring.mail.to=notification-recipient@example.com
```

## Getting Started

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd UpworkAlert
   ```

2. **Configure the application**: Update `src/main/resources/application.properties` with your credentials and criteria.

3. **Build the project**:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

The application will start and check for new jobs every 15 minutes by default.

## Project Structure

- `src/main/java/com/tm/upwork/domain/alert`: Contains the scheduled task logic.
- `src/main/java/com/tm/upwork/domain/job`: Handles job fetching, parsing, and query building.
- `src/main/java/com/tm/upwork/email`: Service for sending email notifications.
- `src/main/java/com/tm/upwork/config`: Configuration beans for Upwork API.
