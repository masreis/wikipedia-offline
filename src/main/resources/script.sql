--

CREATE TABLE PaginaWikipedia
(
   id int PRIMARY KEY NOT NULL,
   title varchar(255),
   timestamp timestamp,
   username varchar(255),
   text MEDIUMTEXT,
   model varchar(255),
   format varchar(255),
   comment MEDIUMTEXT   
)
;

CREATE TABLE CategoriaWikipedia
(
   id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
   descricao varchar(255)
)
;
