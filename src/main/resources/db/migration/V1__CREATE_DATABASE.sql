create table hibernate_sequence (
    next_val bigint
) engine=MyISAM;

insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );

CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    login varchar(64) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    role varchar(255) NOT NULL DEFAULT "DISABLED_USER",
    token varchar(255),
    PRIMARY KEY (id)
) engine=MyISAM;

CREATE TABLE host (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ipadress varchar(64) NOT NULL UNIQUE,
    server_password varchar(255) NOT NULL,
    server_public_key varchar(255) NOT NULL,
    PRIMARY KEY (id)
) engine=MyISAM;

CREATE TABLE peer (
    id BIGINT NOT NULL AUTO_INCREMENT,
    peer_ip varchar(21) NOT NULL,
    peer_private_key varchar(255) not null,
    peer_public_key varchar(255) not null,
    peer_conf_name varchar(255) not null,
    PRIMARY KEY (id)
) engine=MyISAM;