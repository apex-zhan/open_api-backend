# 数据库初始化
# -- 创建库
# create database if not exists my_db;
#
-- 切换库
use openapi;
#
-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    `accessKey`  varchar(512)                           not null comment 'accessKey',
    `secretKey`  varchar(512)                           not null comment 'secretKey',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;
#
# -- 帖子表
# create table if not exists post
# (
#     id         bigint auto_increment comment 'id' primary key,
#     title      varchar(512)                       null comment '标题',
#     content    text                               null comment '内容',
#     tags       varchar(1024)                      null comment '标签列表（json 数组）',
#     thumbNum   int      default 0                 not null comment '点赞数',
#     favourNum  int      default 0                 not null comment '收藏数',
#     userId     bigint                             not null comment '创建用户 id',
#     createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
#     updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
#     isDelete   tinyint  default 0                 not null comment '是否删除',
#     index idx_userId (userId)
# ) comment '帖子' collate = utf8mb4_unicode_ci;
#
# -- 帖子点赞表（硬删除）
# create table if not exists post_thumb
# (
#     id         bigint auto_increment comment 'id' primary key,
#     postId     bigint                             not null comment '帖子 id',
#     userId     bigint                             not null comment '创建用户 id',
#     createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
#     updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
#     index idx_postId (postId),
#     index idx_userId (userId)
# ) comment '帖子点赞';
#
# -- 帖子收藏表（硬删除）
# create table if not exists post_favour
# (
#     id         bigint auto_increment comment 'id' primary key,
#     postId     bigint                             not null comment '帖子 id',
#     userId     bigint                             not null comment '创建用户 id',
#     createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
#     updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
#     index idx_postId (postId),
#     index idx_userId (userId)
# ) comment '帖子收藏';


# -- 创建库
# create database if not exists openapi;

-- 切换库
# use openapi;

# -- 用户表
# create table if not exists user
# (
#     id           bigint auto_increment comment 'id' primary key,
#     userName     varchar(256)                           null comment '用户昵称',
#     userAccount  varchar(256)                           not null comment '账号',
#     userAvatar   varchar(1024)                          null comment '用户头像',
#     gender       tinyint                                null comment '性别',
#     userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
#     userPassword varchar(512)                           not null comment '密码',
#     createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
#     updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
#     isDelete     tinyint      default 0                 not null comment '是否删除',
#     constraint uni_userAccount
#         unique (userAccount)
# ) comment '用户';


create table if not exists `user_interface_info`
(
    `id` bigint not null auto_increment comment '主键',
    `userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口 id',
    `totalNum` int default 0 not null comment '总 调用次数',
    `leftNum` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete tinyint default 0 not null comment '是否删除'
)comment '用户接口调用关系';


insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('许擎宇', '薛聪健', 'www.cary-king.net', '潘博涛', '谭聪健', 0, '石炫明', 9500534531);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('陆弘文', '白志强', 'www.leslee-kuhn.net', '潘懿轩', '马鸿涛', 0, '陈峻熙', 3982575846);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('毛建辉', '罗文', 'www.rosaria-kilback.io', '冯子默', '彭哲瀚', 0, '赵远航', 121776355);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('彭雨泽', '蔡煜祺', 'www.norris-bergstrom.biz', '董思源', '田晓博', 0, '潘擎宇', 740);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('傅志强', '陈梓晨', 'www.jordan-reinger.com', '金志强', '熊锦程', 0, '邓睿渊', 35542559);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('吕黎昕', '孔越彬', 'www.fe-okon.info', '万伟宸', '林昊然', 0, '孟荣轩', 1445);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('夏雪松', '许子骞', 'www.lashawna-legros.co', '蔡昊然', '胡鹏涛', 0, '钟立辉', 34075514);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('严钰轩', '阎志泽', 'www.kay-funk.biz', '莫皓轩', '郭黎昕', 0, '龚天宇', 70956);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('萧嘉懿', '曹熠彤', 'www.margarette-lindgren.biz', '田泽洋', '邓睿渊', 0, '梁志强', 98);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('杜驰', '冯思源', 'www.vashti-auer.org', '黎健柏', '武博文', 0, '李伟宸', 9);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('史金鑫', '蔡鹏涛', 'www.diann-keebler.org', '徐烨霖', '阎建辉', 0, '李烨伟', 125);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('林炫明', '贾旭尧', 'www.dotty-kuvalis.io', '梁雨泽', '龙伟泽', 0, '许智渊', 79998);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('何钰轩', '赖智宸', 'www.andy-adams.net', '崔思淼', '白鸿煊', 0, '邵振家', 7167482751);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('魏志强', '于立诚', 'www.ione-aufderhar.biz', '朱懿轩', '万智渊', 0, '唐昊强', 741098);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('严君浩', '金胤祥', 'www.duane-boyle.org', '雷昊焱', '侯思聪', 0, '郝思', 580514);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('姚皓轩', '金鹏', 'www.lyda-klein.biz', '杜昊强', '邵志泽', 0, '冯鸿涛', 6546);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('廖驰', '沈泽洋', 'www.consuelo-sipes.info', '彭昊然', '邓耀杰', 0, '周彬', 7761037);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('赖智渊', '邓志泽', 'www.emerson-mann.co', '熊明哲', '贺哲瀚', 0, '田鹏', 381422);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('许涛', '陆致远', 'www.vella-ankunding.name', '贾哲瀚', '莫昊焱', 0, '袁越彬', 4218096);
insert into openapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                      `method`, `userId`)
values ('吕峻熙', '沈鹏飞', 'www.shari-reichel.org', '郭鸿煊', '覃烨霖', 0, '熊黎昕', 493);
