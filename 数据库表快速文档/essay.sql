create table essay
(
    id       int unsigned auto_increment comment 'ID,主键'
        primary key,
    admin_id int unsigned  not null comment '管理员ID，关联管理员表',
    title    varchar(20)   not null comment '文章标题',
    content  varchar(2000) not null comment '文章内容'
)
    comment '文章表';
