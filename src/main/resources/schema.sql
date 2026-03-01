CREATE DATABASE IF NOT EXISTS aihealthdiet DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE aihealthdiet;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    age INT,
    height DOUBLE,
    weight DOUBLE,
    gender VARCHAR(10),
    medical_history VARCHAR(255),
    allergy_info VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS food_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    food_image_url VARCHAR(255),
    food_description VARCHAR(255),
    timestamp DATETIME,
    nutrition_result VARCHAR(500),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS health_goal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    goal_type VARCHAR(50),
    goal_detail VARCHAR(255),
    achieved BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES user(id)
);