DROP DATABASE IF EXISTS `contacts_maltsev`;

CREATE DATABASE `contacts_maltsev` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

CREATE TABLE `contacts_maltsev`.`relationship` (
	`id` TINYINT UNSIGNED AUTO_INCREMENT NOT NULL,
	`name` NVARCHAR(20) NOT NULL,
	PRIMARY KEY (`id`)		
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts_maltsev`.`country` (
	`id` TINYINT UNSIGNED AUTO_INCREMENT NOT NULL,
	`phone_code` SMALLINT UNSIGNED NOT NULL,
	`name` NVARCHAR(30) NOT NULL,
	PRIMARY KEY (`id`)		
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts_maltsev`.`city` (
	`id` SMALLINT UNSIGNED AUTO_INCREMENT NOT NULL,
	`name` NVARCHAR(30) NOT NULL,
	`id_country` TINYINT UNSIGNED NOT NULL,
	
	PRIMARY KEY (`id`),
	CONSTRAINT `city_county_fk` FOREIGN KEY (`id_country`) REFERENCES `contacts_maltsev`.`country`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts_maltsev`.`contact` (
	`id` INT UNSIGNED AUTO_INCREMENT NOT NULL,
	`first_name` NVARCHAR(30) NOT NULL,
	`last_name` NVARCHAR(30) NOT NULL,
	`patronymic` NVARCHAR(30),
	`birth_date` DATE,
	`gender` BIT(1) NOT NULL,
	`citizenship` NVARCHAR(50),
	`id_relationship` TINYINT UNSIGNED,
	`web_site` NVARCHAR(255),
	`email` NVARCHAR(255),
	`company_name` NVARCHAR(50),
	`profile_picture` NVARCHAR(255) DEFAULT '/profileImages/default.png',
	
	`id_country` TINYINT UNSIGNED,
	`id_city` SMALLINT UNSIGNED,
	`street` NVARCHAR(50),
	`postcode` NVARCHAR(20),
	
	PRIMARY KEY (`id`),
	CONSTRAINT `concact_relationship_fk` FOREIGN KEY (`id_relationship`) REFERENCES `contacts_maltsev`.`relationship` (`id`) ON DELETE SET NULL,
	
	CONSTRAINT `concact_country_fk` FOREIGN KEY (`id_country`) REFERENCES `contacts_maltsev`.`country` (`id`) ON DELETE SET NULL,
	CONSTRAINT `concact_city_fk` FOREIGN KEY (`id_city`) REFERENCES `contacts_maltsev`.`city` (`id`) ON DELETE SET NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts_maltsev`.`phone` (
	`id` INT UNSIGNED AUTO_INCREMENT NOT NULL,
	`id_country` TINYINT UNSIGNED  NOT NULL,
	`operator_code` SMALLINT UNSIGNED NOT NULL,
	`phone_number` BIGINT UNSIGNED NOT NULL,
	`id_contact` INT UNSIGNED NOT NULL,
	`type` BIT(1) NOT NULL,
	`comment` NVARCHAR(255) NOT NULL,
	
	PRIMARY KEY (`id`),
	CONSTRAINT `phone_country_fk` FOREIGN KEY (`id_country`) REFERENCES `contacts_maltsev`.`country`(`id`) ON DELETE CASCADE,
	CONSTRAINT `phone_contact_fk` FOREIGN KEY (`id_contact`) REFERENCES `contacts_maltsev`.`contact`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts_maltsev`.`attachment` (
	`id` INT UNSIGNED AUTO_INCREMENT NOT NULL,
	`file_name` NVARCHAR(50) NOT NULL,
	`file_path` NVARCHAR(255) NOT NULL,
	`id_contact` INT UNSIGNED NOT NULL,
	`upload_date` DATETIME NOT NULL,
	`comment` NVARCHAR(255) NOT NULL,
	
	PRIMARY KEY (`id`),
	CONSTRAINT `attachment_contact_fk` FOREIGN KEY (`id_contact`) REFERENCES `contacts_maltsev`.`contact`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

