-- SQL script to create the required database and tables for Examinee Management System
-- Run this with: mysql -u <user> -p < create_database.sql

CREATE DATABASE IF NOT EXISTS `exam_system` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `exam_system`;

-- Users table used by LoginFrame
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(100) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Examinees
CREATE TABLE IF NOT EXISTS `examinee` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `age` INT,
  `gender` VARCHAR(20),
  `email` VARCHAR(255),
  `course` VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Exams
CREATE TABLE IF NOT EXISTS `exam` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `exam_name` VARCHAR(255) NOT NULL,
  `exam_date` DATE,
  `duration` INT,
  `total_marks` INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Assigned exams (mapping examinee -> exam)
CREATE TABLE IF NOT EXISTS `assigned_exam` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `examinee_id` INT NOT NULL,
  `exam_id` INT NOT NULL,
  FOREIGN KEY (`examinee_id`) REFERENCES `examinee`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`exam_id`) REFERENCES `exam`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Results table
CREATE TABLE IF NOT EXISTS `result` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `examinee_id` INT NOT NULL,
  `exam_id` INT NOT NULL,
  `marks` INT,
  FOREIGN KEY (`examinee_id`) REFERENCES `examinee`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`exam_id`) REFERENCES `exam`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert a default admin user (username: admin, password: admin) — change this before production
INSERT IGNORE INTO `users` (`username`, `password`) VALUES ('admin', 'admin');
