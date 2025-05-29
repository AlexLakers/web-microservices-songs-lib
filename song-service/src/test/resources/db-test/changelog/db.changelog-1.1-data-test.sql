-- liquibase formatted sql

--changeset alex:2
INSERT INTO song(id, name, album, author_id)
VALUES (1, 'TestName3', 'TestAlbum1', 3),
       (2, 'TestName2', 'TestAlbum2', 2),
       (3, 'TestName1', 'TestAlbum3', 1);

