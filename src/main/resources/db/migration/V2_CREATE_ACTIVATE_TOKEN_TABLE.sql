CREATE TABLE activate_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    token varchar(64) NOT NULL UNIQUE,
    new_role varchar(255) NOT NULL,
    PRIMARY KEY (id)
) engine=MyISAM;