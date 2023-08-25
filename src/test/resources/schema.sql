CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  nickname CHAR(10) NOT NULL UNIQUE,
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS user_role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  role VARCHAR(50) CHECK (role IN ('ROLE_ADMIN', 'ROLE_USER')),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS posts (
  post_id BIGINT PRIMARY KEY,
  views BIGINT DEFAULT 0,
  title VARCHAR(50) NOT NULL,
  content VARCHAR(5000) NOT NULL,
  created_at TIMESTAMP DEFAULT NOW(),
  user_id BIGINT NOT NULL,
  nickname VARCHAR(10) NOT NULL
);