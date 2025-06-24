CREATE TABLE Users (
	user_id SERIAL PRIMARY KEY,
	username VARCHAR(20) UNIQUE NOT NULL,
	password VARCHAR(20) NOT NULL,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	age INTEGER NOT NULL,
	CONSTRAINT chk_age
	CHECK(age > 0),
	profile_pic BYTEA NOT NULL,
	question TEXT NOT NULL,
	answer TEXT NOT NULL,
	date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Collections (
	collection_id SERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	description TEXT,
	date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Books (
	book_id SERIAL PRIMARY KEY,
	title VARCHAR(100) NOT NULL,
	author VARCHAR(100) NOT NULL,
	cover BYTEA NOT NULL,
	status TEXT NOT NULL,
	number_of_pages INTEGER NOT NULL,
	genre VARCHAR(50),
	rating INTEGER
	CONSTRAINT chk_rating
	CHECK(rating >= 0 and rating <= 5),
	description TEXT,
	current_page INTEGER,
	start_date DATE,
	end_date DATE,
	date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Characters (
	character_id SERIAL PRIMARY KEY,
	name VARCHAR(50),
	alias VARCHAR(50),
	character_type TEXT NOT NULL,
	gender TEXT NOT NULL,
	age INTEGER
	CONSTRAINT chk_age
	CHECK(age > 0),
	description TEXT,
	importance TEXT,
	death TEXT,
	physical_appearance TEXT,
	relate_to_mainChar TEXT,
	date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Connector (
	user_id INTEGER,
	collection_id INTEGER,
	book_id INTEGER,
	character_id INTEGER
);