CREATE TABLE tenant_prompt
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tenant_id VARCHAR(100) NOT NULL UNIQUE,
    content   TEXT         NOT NULL
);