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

-- MEETING 테이블
CREATE TABLE IF NOT EXISTS `MEETING` (
  `meeting_id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(225) NOT NULL,
  `start_time` DATETIME NULL,
  `end_time` DATETIME NULL,
  `meeting_state` ENUM('확정', '미확정', '종료') NOT NULL,
  `location_type` ENUM('온라인', '오프라인') NOT NULL,
  `location` VARCHAR(255) NOT NULL,
  `date` DATETIME NOT NULL,
  `meeting_url` VARCHAR(2048) NOT NULL,
  PRIMARY KEY (`meeting_id`)
);

-- MEETING_PARTICIPANT 테이블
CREATE TABLE IF NOT EXISTS `MEETING_PARTICIPANT` (
  `meeting_participant_id` BIGINT NOT NULL AUTO_INCREMENT,
  `meeting_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `role` ENUM('참여자', '주최자') NOT NULL,
  `email_notification` BOOLEAN NOT NULL,
  `permission` ENUM('읽기', '쓰기', '권한주기') NOT NULL,
  `timetable_case` BOOLEAN NOT NULL,
  `vote_case` BOOLEAN NOT NULL,
  PRIMARY KEY (`meeting_participant_id`),
  FOREIGN KEY (`meeting_id`) REFERENCES `MEETING`(`meeting_id`),
  FOREIGN KEY (`user_id`) REFERENCES `USERS`(`user_id`)
);

-- MEETING_AVAILABILITY 테이블
CREATE TABLE IF NOT EXISTS `MEETING_AVAILABILITY` (
  `meeting_availability_id` BIGINT NOT NULL AUTO_INCREMENT,
  `meeting_participant_id` BIGINT NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `weight` INT NOT NULL,
  PRIMARY KEY (`meeting_availability_id`),
  FOREIGN KEY (`meeting_participant_id`) REFERENCES `MEETING_PARTICIPANT`(`meeting_participant_id`)
);

-- MEETING_REFERENCE 테이블
CREATE TABLE IF NOT EXISTS `MEETING_REFERENCE` (
  `reference_id` BIGINT NOT NULL AUTO_INCREMENT,
  `meeting_id` BIGINT NOT NULL,
  `reference_url` VARCHAR(2048) NOT NULL,
  PRIMARY KEY (`reference_id`),
  FOREIGN KEY (`meeting_id`) REFERENCES `MEETING`(`meeting_id`)
);

-- MINUTES 테이블
CREATE TABLE IF NOT EXISTS `MINUTES` (
  `minutes_id` BIGINT NOT NULL AUTO_INCREMENT,
  `meeting_id` BIGINT NOT NULL,
  `minutes_url` VARCHAR(2048) NOT NULL,
  PRIMARY KEY (`minutes_id`),
  FOREIGN KEY (`meeting_id`) REFERENCES `MEETING`(`meeting_id`)
);
