drop table if exists user_table;
drop table if exists auth_table;
drop table if exists game_table;

create table user_table
(
    id integer not null primary key auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    email_address varchar(255) not null
);


create table auth_table
(
    user_id integer not null foreign key,
    username VARCHAR(255) not null,
    auth_token VARCHAR(255) not null,
    foreign key(user_id) references user_table(id)
);

create table game_table
(
    game_id integer not null primary key auto_increment,
    white_username varchar(255),
    black_username VARCHAR(255),
    gameName VARCHAR(255) not null,
    game MEDIUMBLOB
);

/*
insert into member (name, email_address) values ('Ann', 'ann@cs.byu.edu');
insert into member (name, email_address) values ('Bob', 'bob@cs.byu.edu');
insert into member (name, email_address) values ('Chris', 'chris@cs.byu.edu');

insert into genre (genre, description) values ('NonFiction', 'Books that are not fiction.');
insert into genre (genre, description) values ('Fiction', 'Books that are not true.');
insert into genre (genre, description) values ('HistoricalFiction', 'Fictitious books about actual events.');

insert into category (id, name) values (1, 'Top');
insert into category (id, name, parent_id) values (2, 'Must Read', 1);
insert into category (id, name, parent_id) values (3, 'Must Read (New)', 2);
insert into category (id, name, parent_id) values (4, 'Must Read (Old)', 2);
insert into category (id, name, parent_id) values (5, 'Must Read (Really Old)', 2);
insert into category (id, name, parent_id) values (6, 'Optional', 1);
insert into category (id, name, parent_id) values (7, 'Optional (New)', 3);
insert into category (id, name, parent_id) values (8, 'Optional (Old)', 3);
insert into category (id, name, parent_id) values (9, 'Optional (Really Old)', 3);

insert into book (title, author, genre, category_id) values ('Decision Points', 'George W. Bush', 'NonFiction', 7);
insert into book (title, author, genre, category_id) values ('The Work and the Glory', 'Gerald Lund', 'HistoricalFiction', 3);
insert into book (title, author, genre, category_id) values ('Dracula', 'Bram Stoker', 'Fiction', 8);
insert into book (title, author, genre, category_id) values ('The Holy Bible', 'The Lord', 'NonFiction', 5);

insert into books_read (member_id, book_id) values (1, 1);
insert into books_read (member_id, book_id) values (1, 2);
insert into books_read (member_id, book_id) values (2, 2);
insert into books_read (member_id, book_id) values (2, 3);
insert into books_read (member_id, book_id) values (3, 3);
insert into books_read (member_id, book_id) values (3, 4);

 */