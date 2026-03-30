-- 创建数据库
CREATE DATABASE IF NOT EXISTS aihealthdiet CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE aihealthdiet;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     email VARCHAR(100) NOT NULL UNIQUE,
                                     nickname VARCHAR(50),
                                     height DECIMAL(5,2),
                                     weight DECIMAL(5,2),
                                     target_weight DECIMAL(5,2),
                                     daily_calorie_goal INT,
                                     allergies TEXT,
                                     preferred_cuisine VARCHAR(255),
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     INDEX idx_username (username),
                                     INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 饮食记录表
CREATE TABLE IF NOT EXISTS food_logs (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         food_name VARCHAR(100) NOT NULL,
                                         description TEXT,
                                         calories DECIMAL(6,2),
                                         protein DECIMAL(6,2),
                                         carbs DECIMAL(6,2),
                                         fat DECIMAL(6,2),
                                         eat_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         meal_type ENUM('breakfast', 'lunch', 'dinner', 'snack') DEFAULT 'lunch',
                                         input_method ENUM('manual', 'photo', 'voice') DEFAULT 'manual',
                                         photo_url VARCHAR(500),
                                         user_id BIGINT NOT NULL,
                                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                         INDEX idx_user_id (user_id),
                                         INDEX idx_eat_time (eat_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 健康目标表
CREATE TABLE IF NOT EXISTS health_goals (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            goal_type ENUM('weight_loss', 'muscle_gain', 'maintain') NOT NULL,
                                            start_date DATE NOT NULL,
                                            target_date DATE NOT NULL,
                                            initial_weight DECIMAL(5,2),
                                            target_weight DECIMAL(5,2),
                                            weekly_target DECIMAL(4,2),
                                            note TEXT,
                                            user_id BIGINT NOT NULL,
                                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                            INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入测试数据
INSERT INTO users (username, password, email, nickname, height, weight, target_weight, daily_calorie_goal, allergies, preferred_cuisine) VALUES
    ('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBoqwXc2WtZxFO', 'test@example.com', '测试用户', 175.5, 70.2, 65.0, 2000, '花生,海鲜', '中餐,日料');

INSERT INTO food_logs (food_name, description, calories, protein, carbs, fat, eat_time, meal_type, input_method, user_id) VALUES
                                                                                                                              ('燕麦牛奶粥', '早餐燕麦粥', 280.5, 12.0, 40.5, 8.0, '2026-02-28 08:30:00', 'breakfast', 'manual', 1),
                                                                                                                              ('鸡胸肉沙拉', '午餐轻食', 320.0, 35.0, 12.0, 15.0, '2026-02-28 12:15:00', 'lunch', 'manual', 1),
                                                                                                                              ('苹果', '下午加餐', 52.0, 0.3, 14.0, 0.2, '2026-02-28 15:30:00', 'snack', 'manual', 1),
                                                                                                                              ('清蒸鲈鱼', '晚餐', 180.0, 25.0, 0.0, 8.0, '2026-02-28 18:45:00', 'dinner', 'manual', 1);

INSERT INTO health_goals (goal_type, start_date, target_date, initial_weight, target_weight, weekly_target, user_id) VALUES
    ('weight_loss', '2026-02-01', '2026-05-01', 72.5, 65.0, 0.5, 1);