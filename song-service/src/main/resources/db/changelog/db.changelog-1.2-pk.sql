--liquibase formatted dql

--changeset alex:3
SELECT SETVAL ('song_id_seq', (SELECT MAX(id) FROM song));



