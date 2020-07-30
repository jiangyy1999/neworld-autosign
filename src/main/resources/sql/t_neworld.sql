create schema if not exists t_neworld collate utf8_general_ci;

create table if not exists t_log
(
    id int auto_increment
        primary key,
    msg varchar(255) null,
    execute_time timestamp default CURRENT_TIMESTAMP null,
    success tinyint(1) null,
    type int default 0 null,
    user_id int null
);

create table if not exists t_user
(
    id int auto_increment
        primary key,
    username varchar(255) null,
    passwd varchar(255) null
);

