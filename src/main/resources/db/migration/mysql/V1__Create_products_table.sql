DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`
(
    `id`             CHAR(36)     NOT NULL PRIMARY KEY,
    `name`           VARCHAR(256) NOT NULL,
    `description`    VARCHAR(1024),
    `price`          INT UNSIGNED NOT NULL, # use cent as unit, e.g. 1 yuan = 100 cents
    `prepareMinutes` INT UNSIGNED NOT NULL,
    `created_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)