DROP DATABASE IF EXISTS `contacts`;

CREATE DATABASE `contacts` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

CREATE TABLE `contacts`.`user` (
	`login` NVARCHAR(15) NOT NULL,
	`email` NVARCHAR(255) NOT NULL,
	`need_bdate_notify` BIT NOT NULL,
	`profile_picture` NVARCHAR(255) DEFAULT '/sysImages/default.png',
	`password` NVARCHAR(255) NOT NULL,

	PRIMARY KEY (`login`),
	UNIQUE (`email`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts`.`user_roles` (
	`login`  NVARCHAR(15) NOT NULL,
	`role_name` NVARCHAR(15) NOT NULL,

	PRIMARY KEY (`login`, `role_name`),
	CONSTRAINT `user_roles_user_fk` FOREIGN KEY (`login`) REFERENCES `contacts`.`user`(`login`) ON DELETE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts`.`user_groups` (
	`id` INT UNSIGNED AUTO_INCREMENT NOT NULL,
	`group_name` NVARCHAR(30) NOT NULL,
	`login`  NVARCHAR(15) NOT NULL,

	PRIMARY KEY (`id`),
	CONSTRAINT `user_groups_user_fk` FOREIGN KEY (`login`) REFERENCES `contacts`.`user`(`login`) ON DELETE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts`.`user_reset_tokens` (
	`token`	NVARCHAR(255) NOT NULL ,
	`login` NVARCHAR(15) NOT NULL,
	`expiry_date` DATETIME NOT NULL,

	PRIMARY KEY (`token`),
	CONSTRAINT `user_reset_tokens_user_fk` FOREIGN KEY (`login`) REFERENCES `contacts`.`user`(`login`) ON DELETE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts`.`relationship` (
	`id` TINYINT UNSIGNED AUTO_INCREMENT NOT NULL,
	`name` NVARCHAR(20) NOT NULL,
	PRIMARY KEY (`id`)		
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts`.`country` (
	`id` INT UNSIGNED NOT NULL,
	`phone_code` SMALLINT UNSIGNED NOT NULL,
	`name` NVARCHAR(50) NOT NULL,
	PRIMARY KEY (`id`)		
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `contacts`.`contact` (
	`id` INT UNSIGNED AUTO_INCREMENT NOT NULL,
	`first_name` NVARCHAR(30) NOT NULL,
	`last_name` NVARCHAR(30),
	`patronymic` NVARCHAR(30),
	`birth_day` TINYINT,
	`birth_month` TINYINT,
	`birth_year` SMALLINT,
	`gender` TINYINT NOT NULL,
	`citizenship` NVARCHAR(50),
	`id_relationship` TINYINT UNSIGNED,
	`web_site` NVARCHAR(255),
	`email` NVARCHAR(255),
	`company_name` NVARCHAR(50),
	`profile_picture` NVARCHAR(255) DEFAULT '/sysImages/default.png',
	`id_country` INT UNSIGNED,
	`id_city` INT UNSIGNED,
	`street` NVARCHAR(50),
	`postcode` NVARCHAR(20),
	`id_vk` INT UNSIGNED,
	`id_ok` NVARCHAR(50),
	`id_facebook` NVARCHAR(50),
	`id_instagram` NVARCHAR(50),
	`id_twitter` NVARCHAR(50),
	`id_youtube` NVARCHAR(50),
	`id_linkedin` NVARCHAR(50),
	`id_skype` NVARCHAR(50),

	`login_user` NVARCHAR(15) NOT NULL,
	
	PRIMARY KEY (`id`),
	CONSTRAINT `concact_relationship_fk` FOREIGN KEY (`id_relationship`) REFERENCES `contacts`.`relationship` (`id`) ON DELETE SET NULL,
	
	CONSTRAINT `concact_country_fk` FOREIGN KEY (`id_country`) REFERENCES `contacts`.`country` (`id`) ON DELETE SET NULL,
	CONSTRAINT `concact_user_fk` FOREIGN KEY (`login_user`) REFERENCES `contacts`.`user` (`login`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts`.`contact_groups` (
	`id_group` INT UNSIGNED NOT NULL,
	`id_contact` INT UNSIGNED NOT NULL,

	PRIMARY KEY (`id_group`, `id_contact`),
	CONSTRAINT `contact_groups_group_fk` FOREIGN KEY (`id_group`) REFERENCES `contacts`.`user_groups`(`id`) ON DELETE CASCADE,
	CONSTRAINT `contact_groups_contact_fk` FOREIGN KEY (`id_contact`) REFERENCES `contacts`.`contact`(`id`) ON DELETE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts`.`phone` (
	`id` INT UNSIGNED AUTO_INCREMENT NOT NULL,
	`id_country` INT UNSIGNED  NOT NULL,
	`operator_code` SMALLINT UNSIGNED NOT NULL,
	`phone_number` BIGINT UNSIGNED NOT NULL,
	`id_contact` INT UNSIGNED NOT NULL,
	`type` BIT(1) NOT NULL,
	`comment` NVARCHAR(255),
	
	PRIMARY KEY (`id`),
	CONSTRAINT `phone_country_fk` FOREIGN KEY (`id_country`) REFERENCES `contacts`.`country`(`id`) ON DELETE CASCADE,
	CONSTRAINT `phone_contact_fk` FOREIGN KEY (`id_contact`) REFERENCES `contacts`.`contact`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts`.`attachment` (
	`id` INT UNSIGNED AUTO_INCREMENT NOT NULL,
	`file_name` NVARCHAR(50) NOT NULL,
	`file_path` NVARCHAR(255) NOT NULL,
	`id_contact` INT UNSIGNED NOT NULL,
	`upload_date` DATETIME NOT NULL,
	`comment` NVARCHAR(255),
	
	PRIMARY KEY (`id`),
	CONSTRAINT `attachment_contact_fk` FOREIGN KEY (`id_contact`) REFERENCES `contacts`.`contact`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

