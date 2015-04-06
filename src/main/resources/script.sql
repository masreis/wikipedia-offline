create table PaginaWikipedia (id, title, timestamp, username, text, model, format, comment


CREATE TABLE PaginaWikipedia
(
   id int PRIMARY KEY NOT NULL,
   title varchar(255),
   timestamp timestamp,
   username varchar(255),
   text MEDIUMTEXT,
   model varchar(255),
   format varchar(255),
   comment varchar(255)   
)
;


CREATE TABLE CategoriaWikipedia
(
   --id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
   descricao varchar(255)
)
;


