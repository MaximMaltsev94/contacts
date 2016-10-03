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
