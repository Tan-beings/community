create table fk_tag_question
(
    id BIGINT AUTO_INCREMENT,
    fk_tag_id int not null,
    fk_question_id bigint null,
    constraint fk_tag_question_pk
        primary key (id)
);
