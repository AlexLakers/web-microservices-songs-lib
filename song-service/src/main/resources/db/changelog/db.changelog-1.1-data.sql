--liquibase formatted dql

--changeset alex:2
INSERT INTO song(id,author_id,name,album) VALUES
(1,5,'500 Miles','In A Private Moment'),
(2,5,'Adam and Evil','Spinout'),
(3,5,'All Shook Up','Elvis'' Golden Records'),
(4,1,'222','Memory Almost Full'),
(5,1,'About You','Driving Rain'),
(6,1,'All Day',null),
(7,3,'9 to 5','Under the Radar Volume 2'),
(8,3,'16 Tons','Swings Both Ways'),
(9,3,'The 80s','Rudebox'),
(10,2,'2 Bad',null),
(11,4,'Addicted','Rebel Heart');



