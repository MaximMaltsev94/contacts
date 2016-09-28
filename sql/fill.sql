START TRANSACTION ;
INSERT INTO `contacts_maltsev`.`relationship` (`name`) VALUES ('В отношениях');
INSERT INTO `contacts_maltsev`.`relationship` (`name`) VALUES ('Свободен');
INSERT INTO `contacts_maltsev`.`relationship` (`name`) VALUES ('В активном поиске');
INSERT INTO `contacts_maltsev`.`relationship` (`name`) VALUES ('В браке');
COMMIT;

START TRANSACTION ;
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) VALUES('Беларусь', 375);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) VALUES('Россия', 7);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) VALUES('Украина', 380);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) VALUES('США', 1);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) VALUES('Польша', 48);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) VALUES('Германия', 49);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) VALUES('Франция', 33);
COMMIT;

START TRANSACTION ;
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Минск', 1);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Витебск', 1);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Брест', 1);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Гродно', 1);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Гомель', 1);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Могилев', 1);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Москва', 2);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Санкт-Петербург', 2);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Уфа', 2);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Пермь', 2);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Киев', 3);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Одесса', 3);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Нью Йорк', 4);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Вашингтон', 4);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Лос Анджелес', 4);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Бостон', 4);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Варшава', 5);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Берлин', 6);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Мюнхен', 6);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Дортмунд', 6);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Гамбург', 6);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) VALUES('Париж', 7);
COMMIT;

START TRANSACTION ;
INSERT INTO `contacts_maltsev`.`contact` (`first_name`, `last_name`, `gender`, `company_name`, `street`) VALUES('Мальцев', 'Максим', 1, 'ПГУ', 'ул. Мариненко д.1А, кв.59');
INSERT INTO `contacts_maltsev`.`contact` (`first_name`, `last_name`, `gender`, `company_name`, `street`) VALUES('Иванов', 'Иван', 1, 'БГУ', 'ул. Ленина д.25, кв.33');
INSERT INTO `contacts_maltsev`.`contact` (`first_name`, `last_name`, `gender`, `company_name`, `street`) VALUES('Петров', 'Петр', 1, 'БГУИР', 'ул П. Бровки д.3, кв45');
COMMIT ;

START TRANSACTION ;
INSERT INTO `contacts_maltsev`.`phone` (`id_country`, `operator_code`, `phone_number`, `id_contact`, `type`, `comment`) VALUES (1, 33, 9047222, 3, 1, 'Очень хороший номер телефона от очень хорошего человека');
INSERT INTO `contacts_maltsev`.`phone` (`id_country`, `operator_code`, `phone_number`, `id_contact`, `type`, `comment`) VALUES (2, 17, 9047222, 3, 0, 'Очень плохой номер телефона от очень плохого человека человека');
COMMIT;

START TRANSACTION ;
INSERT INTO `contacts_maltsev`.`attachment` (`file_name`, `file_path`, `id_contact`, `upload_date`, `comment`) VALUES ('файл 1', '/contact/?action=document&name=file0023.zip', 3, NOW(), 'Персональные данные');
INSERT INTO `contacts_maltsev`.`attachment` (`file_name`, `file_path`, `id_contact`, `upload_date`, `comment`) VALUES ('файл второй', '/contact/?action=document&name=file0024.zip', 3, NOW(), 'Общественные данные');
COMMIT ;