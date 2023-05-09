create table question
(
    id BIGINT auto_increment,
    title varchar(50) null,
    description text null,
    gmt_create BIGINT null,
    gmt_modified BIGINT null,
    creator varchar(100) null,
    comment_count int default 0 not null,
    view_count int default 0 not null,
    like_count int default 0 not null,
    tag varchar(256) null,
    constraint question_pk
        primary key (id)
);
