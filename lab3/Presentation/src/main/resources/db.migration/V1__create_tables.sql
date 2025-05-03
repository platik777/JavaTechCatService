create table if not exists owner (
    id integer primary key not null generated always as identity,
    name varchar(100),
    birthday date
);

CREATE TABLE if not exists catEntity (
    id integer primary key not null generated always as identity,
    name varchar(100),
    birthday date,
    breed varchar(100),
    age integer,
    color varchar(100),
    owner_id integer references owner (id) not null
);

create table if not exists friends (
    first_friend_id integer references catEntity (id) not null,
    second_friend_id integer references catEntity (id) not null,
    primary key (first_friend_id, second_friend_id)
);