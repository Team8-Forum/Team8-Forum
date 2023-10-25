create table users
(
    user_id    int auto_increment
        primary key,
    first_name varchar(50)          not null,
    last_name  varchar(60)          not null,
    username   varchar(60)          not null,
    password   varchar(50)          not null,
    email      varchar(70)          not null,
    is_admin   tinyint(1)           not null,
    is_blocked tinyint(1) default 0 not null,
    constraint users_pk2
        unique (email),
    constraint users_pk3
        unique (username)
);

create table phone_numbers
(
    user_id      int         not null
        primary key,
    phone_number varchar(10) null,
    constraint phone_numbers_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table posts
(
    post_id       int auto_increment
        primary key,
    user_id       int                                  not null,
    title         varchar(100)                         not null,
    content       text                                 not null,
    creation_date datetime default current_timestamp() not null,
    constraint posts_users_fk
        foreign key (user_id) references users (user_id)
);

create table comments
(
    comment_id int auto_increment
        primary key,
    comment    text not null,
    post_id    int  not null,
    user_id    int  not null,
    constraint comments_posts_fk
        foreign key (post_id) references posts (post_id),
    constraint comments_users__fk
        foreign key (user_id) references users (user_id)
);

create table posts_likes
(
    user_id int null,
    post_id int null,
    constraint posts_likes_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint posts_likes_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

