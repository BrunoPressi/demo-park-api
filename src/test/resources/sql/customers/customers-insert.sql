insert into users (id, username, password, role)
values (100, 'jose@gmail.com', '$2a$12$Hoal93V7RWyYrYtXURHPMeH5GDVmQgiknuFcGArM3TJLVJ6q1pYpW', 'ROLE_ADMIN');

insert into users (id, username, password, role)
values (101, 'jim@gmail.com', '$2a$12$Hoal93V7RWyYrYtXURHPMeH5GDVmQgiknuFcGArM3TJLVJ6q1pYpW', 'ROLE_CUSTOMER');

insert into users (id, username, password, role)
values (102, 'pen@gmail.com', '$2a$12$Hoal93V7RWyYrYtXURHPMeH5GDVmQgiknuFcGArM3TJLVJ6q1pYpW', 'ROLE_CUSTOMER');


insert into customers (id, cpf, name, user_id) values (203, '60639545033', 'Pen Purple', 102);