create table comment
(
    id BIGINT auto_increment,
    parent_id BIGINT null comment '父级回复ID',
    type int not null,
    commentator varchar(100) not null,
    gmt_createTime BIGINT not null,
    gmt_modified BIGINT not null,
    content text not null,
    like_count BIGINT default 0 null,
    constraint comment_pk
        primary key (id)
);