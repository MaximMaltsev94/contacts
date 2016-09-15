DROP DATABASE IF EXISTS `contacts`;

CREATE DATABASE `contacts` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

--citizenship
--relationship
--contact phones
--attached files

CREATE TABLE `contacts`.`citizenship` (
	`id` TINYINT UNSIGNED AUTO_INCREMENT  NOT NULL,
	`name` NVARCHAR(30) NOT NULL
	
	PRIMARY KEY (`id`)		
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contacts`.`relationship` (
	`id` TINYINT UNSIGNED AUTO_INCREMENT NOT NULL,
	`name` NVARCHAR(30) NOT NULL
	
	PRIMARY KEY (`id`)		
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contacts`.`country` (
	`id` TINYINT UNSIGNED AUTO_INCREMENT NOT NULL,
	`phone_code` SMALLINT UNSIGNED NOT NULL,
	`name` NVARCHAR(30) NOT NULL
	
	PRIMARY KEY (`id`)		
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contacts`.`region` (
	`id` TINYINT UNSIGNED AUTO_INCREMENT NOT NULL,
	`name` NVARCHAR(30) NOT NULL,
	`id_country` TINYINT UNSIGNED NOT NULL,
	
	PRIMARY KEY (`id`),
	CONSTRAINT `region_country_fk` FOREIGN KEY (`id_country`) REFERENCES `contacts`.`country`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contacts`.`city` (
	`id` SMALLINT UNSIGNED AUTO_INCREMENT NOT NULL,
	`name` NVARCHAR(30) NOT NULL,
	`id_region` TINYINT UNSIGNED NOT NULL,
	
	PRIMARY KEY (`id`),
	CONSTRAINT `city_region_fk` FOREIGN KEY (`id_region`) REFERENCES `contacts`.`region`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contacts`.`contact` (
	`id` INT UNSIGNED AUTO_INCREMENT NOT NULL,
	`first_name` NVARCHAR(30) NOT NULL,
	`last_name` NVARCHAR(30) NOT NULL,
	`patronymic` NVARCHAR(30) NOT NULL,
	`birth_date` DATE NOT NULL,
	`gender` BIT(1) NOT NULL,
	`id_citizenship` TINYINT UNSIGNED,
	`id_relationship` TINYINT UNSIGNED,
	`web_site` NVARCHAR(255) NOT NULL,
	`email` NVARCHAR(255) NOT NULL,
	`company_name` NVARCHAR(50),
	`profile_picture` NVARCHAR(255) NOT NULL,
	
	`id_country` TINYINT UNSIGNED,
	`id_city` SMALLINT UNSIGNED,
	`street` NVARCHAR(50),
	`postcode` NVARCHAR(20),
	
	PRIMARY KEY (`id`),
	CONSTRAINT `concact_citizenship_fk` FOREIGN KEY (`id_citizenship`) REFERENCES `contacts`.`citizenship` (`id`) ON DELETE SET NULL
	CONSTRAINT `concact_relationship_fk` FOREIGN KEY (`id_relationship`) REFERENCES `contacts`.`relationship` (`id`) ON DELETE SET NULL
	
	CONSTRAINT `concact_country_fk` FOREIGN KEY (`id_country`) REFERENCES `contacts`.`country` (`id`) ON DELETE SET NULL
	CONSTRAINT `concact_city_fk` FOREIGN KEY (`id_city`) REFERENCES `contacts`.`city` (`id`) ON DELETE SET NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contacts`.`phone` (
	`id` INT UNSIGNED AUTO_INCREMENT NOT NULL,
	`id_country` TINYINT UNSIGNED  NOT NULL,
	`operator_code` SMALLINT UNSIGNED NOT NULL,
	`phone_number` BIGINT UNSIGNED NOT NULL,
	`id_contact` INT UNSIGNED NOT NULL,
	`type` BIT(1) NOT NULL,
	`comment` NVARCHAR(255) NOT NULL,
	
	PRIMARY KEY (`id`),
	CONSTRAINT `phone_country_fk` FOREIGN KEY (`id_country`) REFERENCES `contacts`.`country`(`id`) ON DELETE CASCADE
	CONSTRAINT `phone_contact_fk` FOREIGN KEY (`id_contact`) REFERENCES `contacts`.`contact`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contacts`.`attachment` (
	`id` INT UNSIGNED AUTO_INCREMENT NOT NULL,
	`file_name` NVARCHAR(50) NOT NULL,
	`file_path` NVARCHAR(255) NOT NULL,
	`id_contact` INT UNSIGNED NOT NULL,
	`upload_date` DATETIME NOT NULL,S
	`comment` NVARCHAR(255) NOT NULL,
	
	PRIMARY KEY (`id`),
	CONSTRAINT `phone_country_fk` FOREIGN KEY (`id_country`) REFERENCES `contacts`.`country`(`id`) ON DELETE CASCADE
	CONSTRAINT `attachment_contact_fk` FOREIGN KEY (`id_contact`) REFERENCES `contacts`.`contact`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

