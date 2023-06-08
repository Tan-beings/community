create table tag
(
    id int,
    name varchar(50) not null,
    gmt_created BIGINT not null,
    gmt_modified bigint not null,
    constraint tag_pk
        primary key (id)
);
