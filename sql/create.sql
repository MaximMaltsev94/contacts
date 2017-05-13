CREATE TABLE "user" (
	login VARCHAR(15) NOT NULL,
	email VARCHAR(255) NOT NULL,
	need_bdate_notify BOOLEAN NOT NULL,
	profile_picture VARCHAR(255) DEFAULT '/sysImages/default.png',
	password VARCHAR(255) NOT NULL,

	PRIMARY KEY (login),
	UNIQUE (email)
);

CREATE TABLE user_roles (
	login  VARCHAR(15) NOT NULL,
	role_name VARCHAR(15) NOT NULL,

	PRIMARY KEY (login, role_name),
	CONSTRAINT user_roles_user_fk FOREIGN KEY (login) REFERENCES "user"(login) ON DELETE CASCADE

);

CREATE TABLE user_groups (
	id SERIAL NOT NULL,
	group_name VARCHAR(30) NOT NULL,
	login  VARCHAR(15) NOT NULL,

	PRIMARY KEY (id),
	CONSTRAINT user_groups_user_fk FOREIGN KEY (login) REFERENCES "user"(login) ON DELETE CASCADE

);

CREATE TABLE user_reset_tokens (
	token	VARCHAR(255) NOT NULL ,
	login VARCHAR(15) NOT NULL,
	expiry_date TIMESTAMP NOT NULL,

	PRIMARY KEY (token),
	CONSTRAINT user_reset_tokens_user_fk FOREIGN KEY (login) REFERENCES "user"(login) ON DELETE CASCADE

);

CREATE TABLE relationship (
	id SERIAL NOT NULL,
	name VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE country (
	id INT  NOT NULL,
	phone_code SMALLINT  NOT NULL,
	name VARCHAR(50) NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE contact (
	id SERIAL NOT NULL,
	first_name VARCHAR(30) NOT NULL,
	last_name VARCHAR(30) NOT NULL,
	patronymic VARCHAR(30),
	birth_day INT,
	birth_month INT,
	birth_year SMALLINT,
	gender INT NOT NULL,
	citizenship VARCHAR(50),
	id_relationship INT ,
	web_site VARCHAR(255),
	email VARCHAR(255),
	company_name VARCHAR(50),
	profile_picture VARCHAR(255) DEFAULT '/sysImages/default.png',
	id_country INT ,
	id_city INT ,
	street VARCHAR(50),
	postcode VARCHAR(20),
	id_vk INT ,
	id_ok VARCHAR(50),
	id_facebook VARCHAR(50),
	id_instagram VARCHAR(50),
	id_twitter VARCHAR(50),
	id_youtube VARCHAR(50),
	id_linkedin VARCHAR(50),
	id_skype VARCHAR(50),

	login_user VARCHAR(15) NOT NULL,

	PRIMARY KEY (id),
	CONSTRAINT concact_relationship_fk FOREIGN KEY (id_relationship) REFERENCES relationship (id) ON DELETE SET NULL,

	CONSTRAINT concact_country_fk FOREIGN KEY (id_country) REFERENCES country (id) ON DELETE SET NULL,
	CONSTRAINT concact_user_fk FOREIGN KEY (login_user) REFERENCES "user" (login) ON DELETE CASCADE
);

CREATE TABLE contact_groups (
	id_group INT  NOT NULL,
	id_contact INT  NOT NULL,

	PRIMARY KEY (id_group, id_contact),
	CONSTRAINT contact_groups_group_fk FOREIGN KEY (id_group) REFERENCES user_groups(id) ON DELETE CASCADE,
	CONSTRAINT contact_groups_contact_fk FOREIGN KEY (id_contact) REFERENCES contact(id) ON DELETE CASCADE

);

CREATE TABLE phone (
	id SERIAL NOT NULL,
	id_country INT   NOT NULL,
	operator_code SMALLINT  NOT NULL,
	phone_number BIGINT  NOT NULL,
	id_contact INT  NOT NULL,
	type BIT(1) NOT NULL,
	comment VARCHAR(255),
	
	PRIMARY KEY (id),
	CONSTRAINT phone_country_fk FOREIGN KEY (id_country) REFERENCES country(id) ON DELETE CASCADE,
	CONSTRAINT phone_contact_fk FOREIGN KEY (id_contact) REFERENCES contact(id) ON DELETE CASCADE
);

CREATE TABLE attachment (
	id SERIAL NOT NULL,
	file_name VARCHAR(50) NOT NULL,
	file_path VARCHAR(255) NOT NULL,
	id_contact INT  NOT NULL,
	upload_date TIMESTAMP NOT NULL,
	comment VARCHAR(255),
	
	PRIMARY KEY (id),
	CONSTRAINT attachment_contact_fk FOREIGN KEY (id_contact) REFERENCES contact(id) ON DELETE CASCADE
);

