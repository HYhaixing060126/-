CREATE DATABASE IF NOT EXISTS gym_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gym_system;

CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL, real_name VARCHAR(50) NOT NULL, role VARCHAR(20) DEFAULT '管理员',
  member_id BIGINT NULL, balance DECIMAL(10,2) DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE sys_user ADD COLUMN IF NOT EXISTS member_id BIGINT NULL;
ALTER TABLE sys_user ADD COLUMN IF NOT EXISTS balance DECIMAL(10,2) DEFAULT 0.00;
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
INSERT IGNORE INTO sys_user(id,username,password,real_name,role,balance) VALUES (1,'admin','123456','管理员','超级管理员',0);
INSERT IGNORE INTO sys_user(id,username,password,real_name,role,balance) VALUES (2,'member','123456','普通会员','普通用户',0);
INSERT IGNORE INTO sys_user(id,username,password,real_name,role,balance) VALUES (3,'vip','123456','尊贵会员','VIP会员',0);
INSERT IGNORE INTO member(id,name,phone,card_type,expire_date,status) VALUES
 (1,'张小雨','13800001024','年卡','2025-06-24','正常'),(2,'李明远','13900002251','季卡','2026-07-02','即将到期');
INSERT IGNORE INTO gym_class(id,name,coach,class_time,capacity,current_count) VALUES
 (1,'瑜伽基础','李教练','09:00',20,12),(2,'力量训练','王教练','11:30',15,8),(3,'动感单车','张教练','14:00',20,18);
UPDATE sys_user SET real_name='管理员',role='超级管理员' WHERE username='admin';
UPDATE sys_user SET real_name='普通会员',role='普通用户' WHERE username='member';
UPDATE sys_user SET real_name='尊贵会员',role='VIP会员' WHERE username='vip';
UPDATE member SET name='张小雨',phone='13800001024',card_type='年卡' WHERE id=1;
UPDATE member SET name='李明远',phone='13900002251',card_type='季卡' WHERE id=2;
UPDATE member SET status='正常',expire_date='2025-06-24' WHERE id=1;
UPDATE member SET status='即将到期',expire_date='2026-07-02' WHERE id=2;
UPDATE gym_class SET name='瑜伽基础',coach='李教练' WHERE id=1;
UPDATE gym_class SET name='力量训练',coach='王教练' WHERE id=2;
UPDATE gym_class SET name='动感单车',coach='张教练' WHERE id=3;
