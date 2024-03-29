DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`
(
    `id`             CHAR(36)     NOT NULL PRIMARY KEY,
    `name`           VARCHAR(256) NOT NULL,
    `description`    VARCHAR(1024),
    `price`          INT NOT NULL,
    `prepareMinutes` INT NOT NULL,
    `created_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)