CREATE TABLE `route_commondb`.`common_user`
(
    `user_id`    varchar(45) NOT NULL,
    `nickname`   varchar(200) DEFAULT NULL,
    `shard_no`   int          DEFAULT NULL,
    `created_at` datetime     DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB;


CREATE TABLE `route_commondb`.`key_generator`
(
    `id_type`  varchar(45) NOT NULL,
    `id_value` bigint DEFAULT NULL,
    PRIMARY KEY (`id_type`)
) ENGINE=InnoDB;


CREATE TABLE `route_userdb1`.`user`
(
    `user_id`    varchar(45) NOT NULL,
    `nickname`   varchar(200) DEFAULT NULL,
    `created_at` datetime     DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE `route_userdb2`.`user`
(
    `user_id`    varchar(45) NOT NULL,
    `nickname`   varchar(200) DEFAULT NULL,
    `created_at` datetime     DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB;