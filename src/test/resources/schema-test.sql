-- ========================
-- USERS 테이블
-- ========================
CREATE TABLE IF NOT EXISTS USERS (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(128),
  nickname VARCHAR(16) NOT NULL,
  provider VARCHAR(20) NOT NULL,
  profile_image VARCHAR(512),
  role VARCHAR(20) NOT NULL
);

-- ========================
-- MEETING 테이블
-- ========================
CREATE TABLE IF NOT EXISTS MEETING (
  meeting_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  meeting_state VARCHAR(20) NOT NULL,
  location_type VARCHAR(20) NOT NULL,
  location VARCHAR(255) NOT NULL,
  meeting_url VARCHAR(2048) NOT NULL,
  capacity INT NOT NULL,
  token VARCHAR(255) NOT NULL,
  invite_expires_at TIMESTAMP NOT NULL,
  meeting_status INT NOT NULL
);

-- ========================
-- MEETING_PARTICIPANT 테이블
-- ========================
CREATE TABLE IF NOT EXISTS MEETING_PARTICIPANT (
  meeting_participant_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  meeting_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  role VARCHAR(20) NOT NULL,
  email_notification BOOLEAN NOT NULL,
  permission VARCHAR(20) NOT NULL,
  timetable_case BOOLEAN NOT NULL,
  vote_case BOOLEAN NOT NULL,
  FOREIGN KEY (meeting_id) REFERENCES MEETING(meeting_id),
  FOREIGN KEY (user_id) REFERENCES USERS(user_id)
);

-- ========================
-- MEETING_AVAILABILITY 테이블
-- ========================
CREATE TABLE IF NOT EXISTS MEETING_AVAILABILITY (
  meeting_availability_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  meeting_participant_id BIGINT NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  weight INT NOT NULL,
  FOREIGN KEY (meeting_participant_id) REFERENCES MEETING_PARTICIPANT(meeting_participant_id)
);

-- ========================
-- MEETING_REFERENCE 테이블
-- ========================
CREATE TABLE IF NOT EXISTS MEETING_REFERENCE (
  reference_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  meeting_id BIGINT NOT NULL,
  reference_url VARCHAR(2048) NOT NULL,
  FOREIGN KEY (meeting_id) REFERENCES MEETING(meeting_id)
);

-- ========================
-- MINUTES 테이블
-- ========================
CREATE TABLE IF NOT EXISTS MINUTES (
  minutes_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  meeting_id BIGINT NOT NULL,
  minutes_url VARCHAR(2048) NOT NULL,
  FOREIGN KEY (meeting_id) REFERENCES MEETING(meeting_id)
);

-- ========================
-- MEETING_VOTE_CANDIDATE 테이블
-- ========================
CREATE TABLE IF NOT EXISTS MEETING_VOTE_CANDIDATE (
  meeting_candidate_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  meeting_id BIGINT NOT NULL,
  FOREIGN KEY (meeting_id) REFERENCES MEETING(meeting_id)
);

-- ========================
-- MEETING_VOTE_RECORD 테이블
-- ========================
CREATE TABLE IF NOT EXISTS MEETING_VOTE_RECORD (
  meeting_vote_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  meeting_candidate_id BIGINT NOT NULL,
  meeting_participant_id BIGINT NOT NULL,
  FOREIGN KEY (meeting_candidate_id) REFERENCES MEETING_VOTE_CANDIDATE(meeting_candidate_id),
  FOREIGN KEY (meeting_participant_id) REFERENCES MEETING_PARTICIPANT(meeting_participant_id)
);


-- ==============================
-- 테스트 데이터
-- ==============================

-- 사용자
INSERT INTO USERS (user_id, email, password, nickname, provider, role)
VALUES
(1, 'test@test.com',  'pw', 'tester',  'LOCAL', 'USER'),
(2, 'test2@test.com', 'pw', 'tester2', 'LOCAL', 'USER');

-- 회의 (확정된 미래 회의)
INSERT INTO MEETING (meeting_id, title, start_time, end_time, meeting_state, location_type, location, meeting_url, capacity, token, invite_expires_at, meeting_status)
VALUES
(1, '확정된 미래 회의',
 NOW(), DATEADD('HOUR', 2, NOW()),
 'CONFIRMED', 'ONLINE', '비대면', 'https://meet.example.com/1', 10,
 'test-token-1', DATEADD('DAY', 7, NOW()), 3);

-- 회의 (종료된 회의)
INSERT INTO MEETING (meeting_id, title, start_time, end_time, meeting_state, location_type, location, meeting_url, capacity, token, invite_expires_at, meeting_status)
VALUES
(2, '종료된 회의',
 DATEADD('DAY', -2, NOW()), DATEADD('DAY', -1, NOW()),
 'FINISHED', 'OFFLINE', '서울', 'https://meet.example.com/2', 5,
 'test-token-2', DATEADD('DAY', -1, NOW()), 3);

-- 참가자
INSERT INTO MEETING_PARTICIPANT (meeting_participant_id, meeting_id, user_id, role, email_notification, permission, timetable_case, vote_case)
VALUES
(1, 1, 1, 'HOST',   TRUE, 'WRITE', FALSE, FALSE),
(2, 1, 2, 'MEMBER', TRUE, 'WRITE', FALSE, FALSE),
(3, 2, 1, 'HOST',   TRUE, 'WRITE', FALSE, FALSE);

-- 가용 시간 (meeting_id=1 참가자들의 가능 시간)
INSERT INTO MEETING_AVAILABILITY (meeting_participant_id, start_time, end_time, weight)
VALUES
(1, DATEADD('HOUR', 1, NOW()), DATEADD('HOUR', 3, NOW()), 1),
(2, DATEADD('HOUR', 1, NOW()), DATEADD('HOUR', 4, NOW()), 1);

-- 참고 자료
INSERT INTO MEETING_REFERENCE (meeting_id, reference_url)
VALUES
(1, 'https://docs.example.com/ref1');

-- 회의록
INSERT INTO MINUTES (meeting_id, minutes_url)
VALUES
(2, 'https://docs.example.com/minutes2');

-- 투표 후보 시간 (meeting_id=1)
INSERT INTO MEETING_VOTE_CANDIDATE (meeting_candidate_id, start_time, end_time, meeting_id)
VALUES
(1, DATEADD('HOUR', 1, NOW()), DATEADD('HOUR', 2, NOW()), 1),
(2, DATEADD('HOUR', 3, NOW()), DATEADD('HOUR', 4, NOW()), 1);

-- 투표 기록 (참가자 1, 2가 후보 1번에 투표)
INSERT INTO MEETING_VOTE_RECORD (meeting_candidate_id, meeting_participant_id)
VALUES
(1, 1),
(1, 2);