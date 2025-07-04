CREATE TABLE author
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name VARCHAR(128)                            NOT NULL,
    last_name  VARCHAR(128)                            NOT NULL,
    birth_date DATE,
    CONSTRAINT pk_author_id PRIMARY KEY (id),
    CONSTRAINT unique_author_first_name_last_name UNIQUE (first_name, last_name)
);