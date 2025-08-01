create table user
(
    id          int unsigned auto_increment comment 'ID,主键'
        primary key,
    name        varchar(10) not null comment '姓名',
    province    varchar(20) not null comment '省份',
    city        varchar(30) not null comment '市区',
    address     varchar(50) not null comment '地址',
    zip         varchar(30) not null comment '邮编',
    update_time datetime    not null comment '最后修改时间'
)
    comment '用户表';
