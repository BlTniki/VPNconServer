create table hibernate_sequence (
    next_val bigint
) engine=MyISAM;

insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );

CREATE TABLE User (
    id BIGINT NOT NULL AUTO_INCREMENT,
    login varchar(64) not null unique,
    password varchar(255) not null,
    PRIMARY KEY (id)
) engine=MyISAM;

CREATE TABLE Host (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ipadress varchar(15) not null,
    serverPublicKey varchar(255) not null,
    PRIMARY KEY (id)
) engine=MyISAM;

CREATE TABLE UserHost (
    id BIGINT NOT NULL AUTO_INCREMENT,
    peerIp varchar(15) not null,
    peerPrivateKey varchar(255) not null,
    peerPublicKey varchar(255) not null,
    peerConfName varchar(255) not null,
    PRIMARY KEY (id)
) engine=MyISAM;