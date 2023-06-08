create table notification
(
    id BIGINT auto_increment,
    notifier VARCHAR(255) null,
    receiver VARCHAR(255) null,
    type int null,
    outerId BIGINT null,
    status int null,
    gmt_create BIGINT null,
    constraint notification_pk
        primary key (id)
);

