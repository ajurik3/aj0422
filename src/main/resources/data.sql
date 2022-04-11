drop table if exists sale;
drop table if exists tool_type_pricing_map;
drop table if exists tool_pricing;
drop table if exists tool;
drop table if exists tool_type;

create table tool_type (
    tool_type_id bigint primary key,
    tool_type varchar(100) not null
);

create table tool (
    tool_code varchar(50) primary key,
    tool_type_id bigint not null,
    brand varchar(100) not null,
    active boolean not null default true,
    foreign key(tool_type_id) references tool_type(tool_type_id)
);

create table tool_pricing (
    tool_pricing_id bigint primary key,
    tool_rate decimal not null,
    weekday boolean not null default true,
    weekend boolean not null default true,
    holiday boolean not null default true,
    create_time timestamp not null default current_timestamp()
);

create table tool_type_pricing_map (
    tool_type_id bigint,
    tool_pricing_id bigint,
    primary key(tool_type_id, tool_pricing_id),
    foreign key(tool_type_id) references tool_type(tool_type_id),
    foreign key(tool_pricing_id) references tool_pricing(tool_pricing_id)
);

create table sale (
    sale_id bigint primary key,
    sale_time timestamp not null default current_timestamp(),
    days int not null,
    discount decimal not null default 0.0,
    tool_code varchar(50) not null,
    pre_discount_total decimal not null default 0.0,
    foreign key(tool_code) references tool(tool_code)
);

insert into tool_type (tool_type_id, tool_type) values (1, 'Chainsaw'), (2, 'Ladder'), (3, 'Jackhammer');
insert into tool (tool_code, tool_type_id, brand) values
    ('CHNS',1 , 'Stihl'), ('LADW', 2, 'Wener'), ('JAKD', 3, 'DeWalt'), ('JAKR', 3, 'Ridgid');
insert into tool_pricing (tool_pricing_id, tool_rate, weekday, weekend, holiday, create_time) values
    (1, 1.49, true, true, false, '1995-01-01'),
    (2, 1.49, true, false, true, '2000-01-01'),
    (3, 2.99, true, false, false, '2000-01-01'),
    (4, 1.99, true, true, false, '2000-01-01'),
    (5, 0.99, true, true, false, '1990-01-01');
insert into tool_type_pricing_map (tool_type_id, tool_pricing_id) values (2, 1), (2, 4), (2, 5), (1, 2), (3, 3);
insert into sale (sale_id, sale_time, days, discount, tool_code)
VALUES (1, '2020-07-02', 3, 0.1, 'LADW'),
       (2, '2015-07-02', 5, 0.25, 'CHNS'),
       (3, '2015-09-03', 6, 0.0, 'JAKD'),
       (4, '2015-07-02', 9, 0.0, 'JAKR'),
       (5, '2020-07-02', 4, 0.5, 'JAKR');