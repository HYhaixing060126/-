CREATE DATABASE IF NOT EXISTS gym_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gym_system;

CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL, real_name VARCHAR(50) NOT NULL, role VARCHAR(20) DEFAULT '管理员',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS member (
  id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50) NOT NULL, phone VARCHAR(30) NOT NULL,
  card_type VARCHAR(20) NOT NULL, expire_date DATE, status VARCHAR(20) DEFAULT '正常',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS gym_class (
  id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50) NOT NULL, coach VARCHAR(50),
  class_time VARCHAR(30), capacity INT DEFAULT 20, current_count INT DEFAULT 0
);
CREATE TABLE IF NOT EXISTS checkin (
  id BIGINT PRIMARY KEY AUTO_INCREMENT, member_id BIGINT NOT NULL, checkin_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_checkin_member FOREIGN KEY (member_id) REFERENCES member(id)
);
INSERT IGNORE INTO sys_user(id,username,password,real_name,role) VALUES (1,'admin','123456','管理员','超级管理员');
INSERT IGNORE INTO member(id,name,phone,card_type,expire_date,status) VALUES
 (1,'张小雨','13800001024','年卡','2025-06-24','正常'),(2,'李明远','13900002251','季卡','2026-07-02','即将到期');
INSERT IGNORE INTO gym_class(id,name,coach,class_time,capacity,current_count) VALUES
 (1,'瑜伽基础','李教练','09:00',20,12),(2,'力量训练','王教练','11:30',15,8),(3,'动感单车','张教练','14:00',20,18);
