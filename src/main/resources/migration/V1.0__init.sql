create table student (
    id varchar(255) not null,
    given_name varchar(255) not null,
    surname varchar(255) not null,
    middle_name varchar(255) not null,
    age integer,
    course integer,

    primary key(id)
)