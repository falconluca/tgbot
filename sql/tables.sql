-- 存储Emoji https://stackoverflow.com/a/50264108/9076327
CREATE DATABASE 'watch-dog-bot' DEFAULT CHARSET = utf8mb4 DEFAULT COLLATE = utf8mb4_unicode_ci;

create table memo (
    id int primary key auto_increment,
    content varchar(255) not null default '',
    created_at datetime not null,
    creator_id int not null,
    origin_text varchar(255) not null default '',
    url varchar(255) not null default '',
    confirm_code varchar(10) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci;

create table memo_tag (
    id int primary key auto_increment,
    value varchar(255) not null default '',
    color varchar(10) not null default ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci;

create table memo_tag_rel (
    id int primary key auto_increment,
    memo_id int not null,
    tag_id int not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci;

truncate table memo;
truncate table memo_tag;
truncate table memo_tag_rel;
