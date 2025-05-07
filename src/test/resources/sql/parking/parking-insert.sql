insert into users (id, username, password, role)
values (100, 'mario@gmail.com', '$2a$12$Hoal93V7RWyYrYtXURHPMeH5GDVmQgiknuFcGArM3TJLVJ6q1pYpW', 'ROLE_ADMIN');

insert into users (id, username, password, role)
values (101, 'jim@gmail.com', '$2a$12$Hoal93V7RWyYrYtXURHPMeH5GDVmQgiknuFcGArM3TJLVJ6q1pYpW', 'ROLE_CUSTOMER');

insert into users (id, username, password, role)
values (102, 'max@gmail.com', '$2a$12$Hoal93V7RWyYrYtXURHPMeH5GDVmQgiknuFcGArM3TJLVJ6q1pYpW', 'ROLE_CUSTOMER');

insert into users (id, username, password, role)
values (103, 'charles@gmail.com', '$2a$12$Hoal93V7RWyYrYtXURHPMeH5GDVmQgiknuFcGArM3TJLVJ6q1pYpW', 'ROLE_CUSTOMER');

insert into customers (id, cpf, name, user_id) values (203, '60639545033', 'Jim Gray', 101);
insert into customers (id, cpf, name, user_id) values (204, '50339980052', 'Max Brown', 102);
insert into customers (id, cpf, name, user_id) values (205, '75714063074', 'Charles Green', 103);

insert into parking_spot (id, code, status) values (200,'A-01', 'FREE');
insert into parking_spot (id, code, status) values (201,'A-02', 'OCCUPIED');
insert into parking_spot (id, code, status) values (203,'A-03', 'FREE');
insert into parking_spot (id, code, status) values (204,'A-04', 'FREE');
insert into parking_spot (id, code, status) values (205,'A-05', 'FREE');

insert into customer_parking_spot
    (id, car_brand, car_color, car_model, entry_date, license_plate_number, number_receipt, customer_id, parking_spot_id)
        values (500, 'Chevrolet', 'Black',
                'Vectra 2.0', '2025-05-07 13:11:00',
                'FIT-1020', '20250507-131100', '203', '201');

insert into customer_parking_spot
(id, car_brand, car_color, car_model, entry_date, exit_date, license_plate_number, number_receipt, customer_id, parking_spot_id)
values (501, 'Chevrolet', 'Black',
        'Vectra 2.0', '2025-05-09 13:11:35', '2025-05-09 14:00:00',
        'FIT-1020', '20250507-131135', '203', '203');
