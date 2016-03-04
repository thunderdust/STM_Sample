CREATE database if not exists sampledb ;
use sampledb;

CREATE TABLE IF NOT EXISTS USER_ACCOUNT (
  id VARCHAR(25) NOT NULL ,
  password VARCHAR(100),
  firstname VARCHAR(25),
  lastname VARCHAR(25),
  email VARCHAR(100),
  office_id INT,
  language VARCHAR(20) DEFAULT 'en',
  PRIMARY KEY (id)
);
TRUNCATE USER_ACCOUNT;
/* admin account password is admin123 */
INSERT INTO USER_ACCOUNT (id, password, firstname, lastname, email, office_id)
  VALUES ('admin', '0192023a7bbd73250516f069df18b500', 'Administrator', 'System', 'admin@worksap.co.jp', 1),
         ('user1', '6ad14ba9986e3615423dfca256d04e3f', 'User', '001', 'user001@worksap.co.jp', 1);


CREATE TABLE IF NOT EXISTS ROLE (
  name VARCHAR(25) NOT NULL
);
TRUNCATE ROLE;
INSERT INTO ROLE (name)
  VALUES ('USER'), ('ADMIN');


CREATE TABLE IF NOT EXISTS USER_ROLE (
  user_account_id VARCHAR(25) NOT NULL,
  role VARCHAR(25) NOT NULL
);
TRUNCATE USER_ROLE;
INSERT INTO USER_ROLE (user_account_id, role)
  VALUES ('admin', 'ADMIN'), ('admin', 'USER'),
         ('user1', 'USER');

CREATE TABLE IF NOT EXISTS OFFICE (
  id INT NOT NULL AUTO_INCREMENT,
  name varchar(50),
  PRIMARY KEY (id)
);
TRUNCATE OFFICE;
INSERT INTO OFFICE (name)
  VALUES ('Singapore'), ('Tokyo'), ('Shanghai'), ('Osaka');
  
CREATE TABLE IF NOT EXISTS BOOK (
	id INT NOT NULL AUTO_INCREMENT,
    isbn VARCHAR(20) UNIQUE,
    title VARCHAR(100),
    price DECIMAL(8, 2),
    publisher_id INT,
    series_id INT,
    PRIMARY KEY(id)
);
TRUNCATE BOOK;

CREATE TABLE IF NOT EXISTS BOOK_AUTHOR (
	book_id INT NOT NULL,
    author_id INT NOT NULL
);
TRUNCATE BOOK_AUTHOR;

ALTER TABLE BOOK_AUTHOR
ADD CONSTRAINT fk_bookid
FOREIGN KEY(book_id)
REFERENCES BOOK(id)
ON DELETE CASCADE;

ALTER TABLE BOOK_AUTHOR
ADD CONSTRAINT fk_authorid
FOREIGN KEY(author_id)
REFERENCES AUTHOR(id);

CREATE TABLE IF NOT EXISTS AUTHOR (
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    PRIMARY KEY(id)
);
TRUNCATE AUTHOR;

CREATE TABLE IF NOT EXISTS SERIES (
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    PRIMARY KEY(id)
);
TRUNCATE SERIES;

CREATE TABLE IF NOT EXISTS PUBLISHER (
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    PRIMARY KEY(id)
);
TRUNCATE PUBLISHER;

INSERT INTO SERIES(name)
VALUES ('Amazing Spiderman'), ('Spectacular Spiderman');

INSERT INTO PUBLISHER(name)
VALUES ('Marvel Comics');

INSERT INTO AUTHOR(name)
VALUES ('Stan Lee'), ('Steve Ditko'), ('Roy Thomas');

INSERT INTO BOOK(isbn, title, price, publisher_id, series_id)
VALUES('978-3-16-148410-0', 'Spider-Man: Freak! Public Menace!', 30, 1, 1),
('978-3-16-148410-1', 'A Monster Called... Morbius!', 30, 1, 1),('978-0785116820', 'Essential Peter Parker, The Spectacular Spider-Man Vol. 1', 20, 1, 2),
('978-0785120421', 'Essential Peter Parker, The Spectacular Spider-Man Vol. 2', 40, 1, 2);

INSERT INTO BOOK_AUTHOR(book_id, author_id)
VALUES (1, 1), (1, 2), (2, 3), (3, 1), (4, 1);
