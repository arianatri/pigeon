DROP DATABASE IF EXISTS pigeon;
CREATE DATABASE IF NOT EXISTS pigeon;
USE pigeon;

CREATE TABLE users (
	id          INT         NOT NULL AUTO_INCREMENT,
	name        VARCHAR(20) NOT NULL,
	imei        VARCHAR(20) NOT NULL,
	device_hash TEXT        NOT NULL,
	fcm_id      TEXT,
	api_key     VARCHAR(20) NOT NULL,
	is_active   TINYINT(4)  NOT NULL DEFAULT 1,
	created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
	UNIQUE KEY (api_key),
	UNIQUE KEY (imei)
);

CREATE TABLE locations(
	id          INT NOT NULL AUTO_INCREMENT,
	user_id     INT NOT NULL,
	lat         VARCHAR(20)  NOT NULL,
	lon         VARCHAR(20)  NOT NULL,
	speed       BIGINT NOT NULL,
	device_timestamp BIGINT NOT NULL,
	created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);
