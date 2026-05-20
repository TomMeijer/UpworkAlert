CREATE TABLE job (
    id INT PRIMARY KEY AUTO_INCREMENT,
    upwork_id VARCHAR(255) UNIQUE,
    title VARCHAR(255),
    description TEXT,
    type VARCHAR(50),
    hourly_rate_min DOUBLE,
    hourly_rate_max DOUBLE,
    fixed_price DOUBLE,
    client_country VARCHAR(255),
    required_skills TEXT,
    url VARCHAR(255),
    published_on VARCHAR(255),
    experience_level VARCHAR(255),
    payment_verified BOOLEAN,
    client_rating DOUBLE,
    client_total_spent DOUBLE
);
