CREATE DATABASE IF NOT EXISTS assignment5;
USE assignment5;

CREATE TABLE IF NOT EXISTS bus_pass (
  pass_id VARCHAR(20) PRIMARY KEY,
  register_no VARCHAR(20) NOT NULL,
  student_name VARCHAR(100) NOT NULL,
  route_no VARCHAR(20) NOT NULL,
  boarding_stop VARCHAR(100) NOT NULL,
  validity_date DATE NOT NULL,
  amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0)
);

CREATE TABLE IF NOT EXISTS student (
  student_id INT PRIMARY KEY,
  student_name VARCHAR(100) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS wallet (
  wallet_id INT PRIMARY KEY,
  owner_name VARCHAR(80) NOT NULL,
  balance DECIMAL(12,2) NOT NULL CHECK (balance >= 0)
);

CREATE TABLE IF NOT EXISTS app_user (
  username VARCHAR(60) PRIMARY KEY,
  password_text VARCHAR(100) NOT NULL
);

INSERT IGNORE INTO wallet(wallet_id, owner_name, balance) VALUES
(1, 'Hostel Wallet', 2000.00),
(2, 'Canteen Wallet', 1000.00);

INSERT IGNORE INTO app_user(username, password_text) VALUES
('student1', 'pass123');
