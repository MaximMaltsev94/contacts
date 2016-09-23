START TRANSACTION ;
INSERT INTO `contacts_maltsev`.`relationship` (`name`) values ('В отношениях');
INSERT INTO `contacts_maltsev`.`relationship` (`name`) values ('Свободен');
INSERT INTO `contacts_maltsev`.`relationship` (`name`) values ('В активном поиске');
INSERT INTO `contacts_maltsev`.`relationship` (`name`) values ('В браке');
COMMIT;

START TRANSACTION ;
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) values('Беларусь', 375);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) values('Россия', 7);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) values('Украина', 380);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) values('США', 1);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) values('Польша', 48);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) values('Германия', 49);
INSERT INTO `contacts_maltsev`.`country` (`name`, `phone_code`) values('Франция', 33);
COMMIT;

START TRANSACTION ;
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Минск', 1);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Витебск', 1);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Брест', 1);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Гродно', 1);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Гомель', 1);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Могилев', 1);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Москва', 2);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Санкт-Петербург', 2);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Уфа', 2);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Пермь', 2);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Киев', 3);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Одесса', 3);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Нью Йорк', 4);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Вашингтон', 4);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Лос Анджелес', 4);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Бостон', 4);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Варшава', 5);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Берлин', 6);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Мюнхен', 6);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Дортмунд', 6);
INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Гамбург', 6);

INSERT INTO `contacts_maltsev`.`city` (`name`, `id_country`) values('Париж', 7);
COMMIT;

START TRANSACTION ;
INSERT INTO `contacts_maltsev`.`contact` (`first_name`, `last_name`, `gender`, `company_name`, `street`) values('Мальцев', 'Максим', 1, 'ПГУ', 'ул. Мариненко д.1А, кв.59');
INSERT INTO `contacts_maltsev`.`contact` (`first_name`, `last_name`, `gender`, `company_name`, `street`) values('Иванов', 'Иван', 1, 'БГУ', 'ул. Ленина д.25, кв.33');
INSERT INTO `contacts_maltsev`.`contact` (`first_name`, `last_name`, `gender`, `company_name`, `street`) values('Петров', 'Петр', 1, 'БГУИР', 'ул П. Бровки д.3, кв45');
COMMIT ;
