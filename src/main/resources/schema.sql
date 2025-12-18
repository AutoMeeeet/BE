-- USERS 테이블
CREATE TABLE IF NOT EXISTS `USERS` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(128) NULL,
    `nickname` VARCHAR(16) NOT NULL,
    `provider` ENUM('KAKAO', 'GOOGLE', 'LOCAL') NOT NULL,
    `profile_image` VARCHAR(512) NULL,
    PRIMARY KEY (`user_id`)
);

-- MEETING 테이블 (date 삭제, start_dttm / end_dttm로 변경)
CREATE TABLE IF NOT EXISTS `MEETING` (
    `meeting_id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(225) NOT NULL,
    `start_dttm` DATETIME NOT NULL,
    `end_dttm` DATETIME NOT NULL,
    `meeting_state` ENUM('CONFIRMED', 'PENDING', 'FINISHED') NOT NULL,
    `location_type` ENUM('ONLINE', 'OFFLINE') NOT NULL,
    `location` VARCHAR(255) NOT NULL,
    `meeting_url` VARCHAR(2048) NOT NULL,
    PRIMARY KEY (`meeting_id`)
);

-- MEETING_PARTICIPANT 테이블 (FK 제거)
CREATE TABLE IF NOT EXISTS `MEETING_PARTICIPANT` (
    `meeting_participant_id` BIGINT NOT NULL AUTO_INCREMENT,
    `meeting_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `role` ENUM('PARTICIPANT', 'HOST') NOT NULL,
    `email_notification` BOOLEAN NOT NULL,
    `permission` ENUM('READ', 'WRITE', 'GRANT') NOT NULL,
    `timetable_case` BOOLEAN NOT NULL,
    `vote_case` BOOLEAN NOT NULL,
    PRIMARY KEY (`meeting_participant_id`)
);

-- MEETING_AVAILABILITY 테이블 (FK 제거)
CREATE TABLE IF NOT EXISTS `MEETING_AVAILABILITY` (
    `meeting_availability_id` BIGINT NOT NULL AUTO_INCREMENT,
    `meeting_participant_id` BIGINT NOT NULL,
    `start_dttm` DATETIME NOT NULL,
    `end_dttm` DATETIME NOT NULL,
    `weight` INT NOT NULL,
    PRIMARY KEY (`meeting_availability_id`)
);

-- MEETING_REFERENCE 테이블 (FK 제거)
CREATE TABLE IF NOT EXISTS `MEETING_REFERENCE` (
    `reference_id` BIGINT NOT NULL AUTO_INCREMENT,
    `meeting_id` BIGINT NOT NULL,
    `reference_url` VARCHAR(2048) NOT NULL,
    PRIMARY KEY (`reference_id`)
);

-- MINUTES 테이블 (FK 제거)
CREATE TABLE IF NOT EXISTS `MINUTES` (
    `minutes_id` BIGINT NOT NULL AUTO_INCREMENT,
    `meeting_id` BIGINT NOT NULL,
    `minutes_url` VARCHAR(2048) NOT NULL,
    PRIMARY KEY (`minutes_id`)
);
