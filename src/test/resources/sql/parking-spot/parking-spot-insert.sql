insert into users (id, username, password, role)
values (100, 'mario@gmail.com', '$2a$12$Hoal93V7RWyYrYtXURHPMeH5GDVmQgiknuFcGArM3TJLVJ6q1pYpW', 'ROLE_ADMIN');

insert into users (id, username, password, role)
values (101, 'jim@gmail.com', '$2a$12$Hoal93V7RWyYrYtXURHPMeH5GDVmQgiknuFcGArM3TJLVJ6q1pYpW', 'ROLE_CUSTOMER');

insert into parking_spot (id, code, status) values (200,'A-01', 'FREE');
insert into parking_spot (id, code, status) values (201,'A-02', 'OCCUPIED');
insert into parking_spot (id, code, status) values (203,'A-03', 'FREE');
insert into parking_spot (id, code, status) values (204,'A-04', 'FREE');
