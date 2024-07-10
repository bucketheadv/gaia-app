create database if not exists gaia_api;

use gaia_api;

create table t_http_executor (
                              id bigint(11) unsigned not null primary key auto_increment,
                              name varchar(255) not null comment '事件名称',
                              call_type tinyint(4) not null comment '调用类型, 1 微服务 2 普通HTTP调用',
                              service_name varchar(255) comment '服务名称',
                              url varchar(2000) comment 'url',
                              http_method varchar(20) not null comment 'http method',
                              timeout int(11) not null default 0 comment '超时时间',
                              online_status tinyint(1) not null default 0 comment '上线状态 0 未上线 1 已上线',
                              http_headers varchar(5000) comment 'HttpHeader参数',
                              http_params varchar(5000) comment 'HttpParam参数',
                              created_by varchar(255) not null default '' comment '创建人',
                              updated_by varchar(255) not null default '' comment '最后更新人',
                              deleted tinyint(1) not null default 0 comment '是否已删除，1 是 0 否',
                              create_time timestamp not null default current_timestamp comment '创建时间',
                              update_time timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
                              key `idx_create_time`(`create_time`)
) comment 'http执行器信息表';

create table t_http_execute_log (
                                 id bigint(11) unsigned not null primary key auto_increment,
                                 executor_id bigint(11) unsigned not null comment '执行器ID',
                                 url varchar(2000) comment '执行url',
                                 http_method varchar(20) not null comment 'http method',
                                 http_headers varchar(5000) comment 'HttpHeader参数',
                                 http_params varchar(5000) comment 'HttpParam参数',
                                 duration bigint(20) comment '执行时长/毫秒',
                                 execute_user varchar(255) not null default '' comment '执行人',
                                 execute_result int(11) not null default 0 comment '执行结果 0 未知 1 成功 2 失败',
                                 execute_response mediumtext comment '执行返回结果',
                                 create_time timestamp not null default current_timestamp comment '创建时间',
                                 update_time timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
                                 key `idx_executor_id`(`executor_id`),
                                 key `idx_execute_user`(`execute_user`),
                                 key `idx_create_time`(`create_time`)
) comment 'http执行器执行日志';

create table t_passport_info (
                               id bigint(11) unsigned not null primary key auto_increment,
                               user_id bigint(11) not null comment '用户id',
                               title varchar(255) not null comment '标题',
                               url varchar(2000) comment '网站地址',
                               username varchar(255) comment '网站用户名',
                               password varchar(255) comment '网站密码',
                               remark varchar(512) comment '备注',
                               deleted tinyint(1) not null default 0 comment '是否已删除 1 是 0 否',
                               create_time timestamp not null default current_timestamp comment '创建时间',
                               update_time timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
                               key `idx_user_id`(`user_id`),
                               key `idx_create_time`(`create_time`)
) comment '密码本';


create table t_user_login (
    id bigint(11) unsigned not null primary key auto_increment,
    username varchar(255) not null comment '用户名',
    password varchar(255) not null comment '密码',
    avatar varchar(512) not null comment '头像',
    access varchar(512) not null comment '权限列表, 如 ["super_admin"]',
    status tinyint(4) not null default 0 comment '是否被禁用 0 否 1 是',
    deleted tinyint(1) not null default 0 comment '是否已删除 1 是 0 否',
    create_time timestamp not null default current_timestamp comment '创建时间',
    update_time timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key `uk_username`(`username`),
    key `idx_create_time`(`create_time`)
) comment '后台管理员信息表';

insert into gaia_api.t_user_login (username, password, avatar, access)
values ('super_admin', '123456', 'http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960', '["super_admin"]');
