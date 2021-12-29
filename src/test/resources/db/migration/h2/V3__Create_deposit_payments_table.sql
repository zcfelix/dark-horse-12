DROP TABLE IF EXISTS `deposit_payments`;
CREATE TABLE `deposit_payments`
(
    `id`             CHAR(36) NOT NULL PRIMARY KEY,
    `contract_id`    CHAR(36) NOT NULL,
    `transaction_id` CHAR(36) NOT NULL,
    `amount`         DECIMAL,
    `created_at`     TIMESTAMP,
    `expired_at`     TIMESTAMP,
    `paid`           BOOL
)