CREATE TABLE IF NOT EXISTS quantity_measurements (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_value     DOUBLE NOT NULL,
    first_unit      VARCHAR(50) NOT NULL,
    second_value    DOUBLE NOT NULL,
    second_unit     VARCHAR(50) NOT NULL,
    operation_type  VARCHAR(50) NOT NULL,
    measurement_type VARCHAR(50) NOT NULL,
    result_value    DOUBLE,
    result_unit     VARCHAR(50),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);