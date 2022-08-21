create table hibernate_sequence (
    next_val bigint
) engine=MyISAM;

insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );

CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    login varchar(64) not null unique,
    password varchar(255) not null,
    PRIMARY KEY (id)
) engine=MyISAM;

CREATE TABLE host (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ipadress varchar(21) not null unique,
    server_public_key varchar(255) not null,
    PRIMARY KEY (id)
) engine=MyISAM;

CREATE TABLE peer (
    id BIGINT NOT NULL AUTO_INCREMENT,
    peer_ip varchar(21) not null,
    peer_private_key varchar(255) not null,
    peer_public_key varchar(255) not null,
    peer_conf_name varchar(255) not null,
    PRIMARY KEY (id)
) engine=MyISAM;