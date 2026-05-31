CREATE TABLE search_criteria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    min_hourly_rate INT,
    min_fixed_price INT,
    category_ids VARCHAR(1024),
    locations VARCHAR(1024),
    search_expression VARCHAR(1024)
);

INSERT INTO search_criteria (min_hourly_rate, min_fixed_price, category_ids, locations, search_expression)
VALUES (35, 100, '531770282580668418', 'Europe,Asia', '(Java OR Spring OR Angular)');
