CREATE TABLE
    USER
(
    id bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    userId VARCHAR(9) COMMENT '用户ID',
    age int COMMENT '用户头像',
    createTime TIMESTAMP NULL COMMENT '创建时间',
    updateTime TIMESTAMP NULL COMMENT '更新时间',
    userName VARCHAR(64),
    PRIMARY KEY (id)
)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into user (id, userId, age, createTime, updateTime, userName) values (1, '10001', 18, '2022-04-13 00:00:00', '2022-04-13 00:00:01', '张三');