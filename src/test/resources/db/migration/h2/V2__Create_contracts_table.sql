DROP TABLE IF EXISTS `contracts`;
CREATE TABLE `contracts`
(
    `id`                CHAR(36) NOT NULL PRIMARY KEY,
    `deposit_payed`     BOOL     NOT NULL DEFAULT FALSE,
    `service_charge_rate` INT,
    `created_at`        TIMESTAMP         DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)