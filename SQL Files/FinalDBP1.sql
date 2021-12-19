create table store
	(isbn       		varchar(50),
	 sales				numeric(15,2),
	 expenses			numeric(15,2),
	 sold				numeric(15,0),
	 sold_last_month	numeric(15,0),
	 primary key (isbn)
	);

create table publisher
	(publisher_id		    varchar(50), 
	 publisher_name			varchar(50), 
	 publisher_address		varchar(50),
	 email			        varchar(50),
     phone			        varchar(25),
     banking		        numeric(15,2),
	 primary key (publisher_id)
	);

create table book_user
	(email      varchar(50), 
	 username	varchar(50), 
     pass	    varchar(50),
	 billing	varchar(50),
	 shipping	varchar(50),
     phone	    varchar(50),
	 primary key (email)
	);

create table isbn
	(isbn		    varchar(50), 
	 publisher_id	varchar(50), 
	 quantity		numeric(10,0),
     percent		numeric(10,2),
     price		    numeric(10,2),
	 primary key (isbn),
     foreign key (isbn) references store on delete cascade,
     foreign key (publisher_id) references publisher on delete cascade
	);

create table book
	(isbn			varchar(50), 
	 book_name		varchar(50),
     author		    varchar(50),
     year			numeric(4,0),
	 genre		    varchar(50), 
	 pages		    numeric(6,0),
	 primary key (isbn),
	 foreign key (isbn) references isbn on delete cascade
	);

create table checkout
	(order_number	varchar(50),
     email		    varchar(50), 
     billing	    varchar(50),
	 shipping	    varchar(50),
	 primary key (order_number),
     foreign key (email) references book_user on delete cascade
	);

create table book_order
	(order_id		   varchar(50),
	 isbn		    varchar(50),
	 order_number	varchar(50),
     quantity	    numeric(10,0),
	 primary key (order_id),
     foreign key (isbn) references book on delete cascade,
     foreign key (order_number) references checkout on delete cascade
	);

--TO CLEAR TABLES for Testing
--DROP TABLE IF EXISTS book_order CASCADE;
--DROP TABLE IF EXISTS checkout CASCADE;
--DROP TABLE IF EXISTS book_user CASCADE;
--DROP TABLE IF EXISTS publisher CASCADE;
--DROP TABLE IF EXISTS isbn CASCADE;
--DROP TABLE IF EXISTS book CASCADE;
--DROP TABLE IF EXISTS store CASCADE;