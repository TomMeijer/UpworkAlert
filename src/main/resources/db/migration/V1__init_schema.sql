CREATE TABLE chat (
    id INT PRIMARY KEY AUTO_INCREMENT,
    external_id VARCHAR(255) NOT NULL
);

CREATE TABLE chat_message (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role VARCHAR(50) NOT NULL,
    time DATETIME NOT NULL,
    content TEXT NOT NULL,
    chat_id INT NOT NULL,
    CONSTRAINT fk_chat_message_chat FOREIGN KEY (chat_id) REFERENCES chat (id) ON DELETE CASCADE
);

CREATE TABLE job (
    id INT PRIMARY KEY AUTO_INCREMENT,
    upwork_id VARCHAR(255) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    type VARCHAR(50),
    hourly_rate_min DOUBLE,
    hourly_rate_max DOUBLE,
    fixed_price DOUBLE,
    client_country VARCHAR(255),
    required_skills TEXT,
    url VARCHAR(255) NOT NULL,
    published_on DATETIME NOT NULL,
    experience_level VARCHAR(255),
    payment_verified BOOLEAN,
    client_rating DOUBLE,
    client_total_spent DOUBLE,
    chat_id INT,
    CONSTRAINT fk_job_chat FOREIGN KEY (chat_id) REFERENCES chat (id) ON DELETE SET NULL
);
