drop table if exists SelfieDetails;
create table SelfieDetails (
	Id			INT UNSIGNED NOT NULL AUTO_INCREMENT,
	SnapShotUrl		VARCHAR(512) NOT NULL,
	HashTag			VARCHAR(48) NOT NULL,
	PhotoText		VARCHAR(128) NOT NULL,
	TwitterId		INT UNSIGNED NOT NULL,
	PRIMARY KEY (Id)
	);


drop table if exists TwitterDetails;
create table TwitterDetails (
	Id			INT UNSIGNED NOT NULL AUTO_INCREMENT,
	AccessToken		VARCHAR(255) NOT NULL,
	AccessTokenSecret	VARCHAR(255) NOT NULL,
	PRIMARY KEY (Id)
	);



