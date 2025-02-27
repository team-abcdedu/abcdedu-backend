-- boards 테이블 생성
CREATE TABLE boards (
                        id BIGINT AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
);

-- members 테이블 생성
CREATE TABLE members (
                         id BIGINT AUTO_INCREMENT,
                         created_at TIMESTAMP(6),
                         student_id BIGINT,
                         updated_at TIMESTAMP(6),
                         name VARCHAR(10) NOT NULL,
                         school VARCHAR(20),
                         email VARCHAR(40) NOT NULL UNIQUE,
                         encoded_password VARCHAR(60) NOT NULL,
                         image_object_key VARCHAR(100),
                         role ENUM('ADMIN', 'BASIC', 'STUDENT'),
                         PRIMARY KEY (id)
);

-- lectures 테이블 생성
CREATE TABLE lectures (
                          id BIGINT AUTO_INCREMENT,
                          title VARCHAR(10) NOT NULL,
                          sub_title VARCHAR(40) NOT NULL,
                          description VARCHAR(200) NOT NULL,
                          PRIMARY KEY (id)
);

-- sub_lectures 테이블 생성
CREATE TABLE sub_lectures (
                              id BIGINT AUTO_INCREMENT,
                              order_number INT NOT NULL,
                              lecture_id BIGINT NOT NULL,
                              title VARCHAR(50) NOT NULL,
                              description VARCHAR(200) NOT NULL,
                              PRIMARY KEY (id),
                              FOREIGN KEY (lecture_id) REFERENCES lectures (id)
);

-- assignments 테이블 생성
CREATE TABLE assignments (
                             id BIGINT AUTO_INCREMENT,
                             sub_lecture_id BIGINT NOT NULL,
                             body VARCHAR(20) NOT NULL,
                             title VARCHAR(20) NOT NULL,
                             PRIMARY KEY (id),
                             FOREIGN KEY (sub_lecture_id) REFERENCES sub_lectures (id)
);

-- assignment_files 테이블 생성
CREATE TABLE assignment_files (
                                  id BIGINT AUTO_INCREMENT,
                                  sub_lecture_id BIGINT NOT NULL,
                                  object_key VARCHAR(100),
                                  assignment_type ENUM('ANSWER', 'DATA', 'EXAM', 'THEORY'),
                                  PRIMARY KEY (id),
                                  FOREIGN KEY (sub_lecture_id) REFERENCES sub_lectures (id)
);

-- assignment_questions 테이블 생성
CREATE TABLE assignment_questions (
                                      id BIGINT AUTO_INCREMENT,
                                      order_number INT NOT NULL,
                                      assignment_id BIGINT NOT NULL,
                                      body VARCHAR(100) NOT NULL,
                                      title VARCHAR(100) NOT NULL,
                                      assignment_answer_type ENUM('MULTIPLE_CHOICE', 'OX', 'SUBJECTIVE'),
                                      PRIMARY KEY (id),
                                      FOREIGN KEY (assignment_id) REFERENCES assignments (id)
);

-- assignment_submissions 테이블 생성
CREATE TABLE assignment_submissions (
                                        id BIGINT AUTO_INCREMENT,
                                        assignment_id BIGINT NOT NULL,
                                        created_at TIMESTAMP(6),
                                        member_id BIGINT NOT NULL,
                                        updated_at TIMESTAMP(6),
                                        PRIMARY KEY (id),
                                        FOREIGN KEY (assignment_id) REFERENCES assignments (id),
                                        FOREIGN KEY (member_id) REFERENCES members (id)
);

-- assignment_answers 테이블 생성
CREATE TABLE assignment_answers (
                                    id BIGINT AUTO_INCREMENT,
                                    assignment_question_id BIGINT NOT NULL,
                                    assignment_submission_id BIGINT NOT NULL,
                                    body VARCHAR(200) NOT NULL,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (assignment_question_id) REFERENCES assignment_questions (id),
                                    FOREIGN KEY (assignment_submission_id) REFERENCES assignment_submissions (id)
);

-- assignment_answer_files 테이블 생성
CREATE TABLE assignment_answer_files (
                                         id BIGINT AUTO_INCREMENT,
                                         assignment_file_id BIGINT NOT NULL UNIQUE,
                                         object_key VARCHAR(100),
                                         PRIMARY KEY (id),
                                         FOREIGN KEY (assignment_file_id) REFERENCES assignment_files (id)
);

-- posts 테이블 생성
CREATE TABLE posts (
                       id BIGINT AUTO_INCREMENT,
                       comment_allow BOOLEAN NOT NULL,
                       deleted BOOLEAN,
                       secret BOOLEAN NOT NULL,
                       board_id BIGINT NOT NULL,
                       comment_count BIGINT,
                       created_at TIMESTAMP(6),
                       member_id BIGINT NOT NULL,
                       updated_at TIMESTAMP(6),
                       view_count BIGINT NOT NULL,
                       title VARCHAR(100) NOT NULL,
                       content VARCHAR(1000) NOT NULL,
                       file_url VARCHAR(255),
                       PRIMARY KEY (id),
                       FOREIGN KEY (board_id) REFERENCES boards (id),
                       FOREIGN KEY (member_id) REFERENCES members (id)
);

-- comments 테이블 생성
CREATE TABLE comments (
                          id BIGINT AUTO_INCREMENT,
                          deleted BOOLEAN,
                          created_at TIMESTAMP(6),
                          member_id BIGINT NOT NULL,
                          post_id BIGINT NOT NULL,
                          updated_at TIMESTAMP(6),
                          content VARCHAR(200) NOT NULL,
                          PRIMARY KEY (id),
                          FOREIGN KEY (member_id) REFERENCES members (id),
                          FOREIGN KEY (post_id) REFERENCES posts (id)
);

-- homeworks 테이블 생성
CREATE TABLE homeworks (
                           id BIGINT AUTO_INCREMENT,
                           member_id BIGINT NOT NULL,
                           title VARCHAR(50) NOT NULL,
                           additional_description VARCHAR(100),
                           description VARCHAR(300),
                           PRIMARY KEY (id),
                           FOREIGN KEY (member_id) REFERENCES members (id)
);

-- homework_questions 테이블 생성
CREATE TABLE homework_questions (
                                    id BIGINT AUTO_INCREMENT,
                                    is_answer_required BOOLEAN NOT NULL,
                                    homework_id BIGINT NOT NULL,
                                    additional_content VARCHAR(500),
                                    content VARCHAR(500) NOT NULL,
                                    order_number VARCHAR(255) NOT NULL,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (homework_id) REFERENCES homeworks (id)
);

-- homework_replys 테이블 생성
CREATE TABLE homework_replys (
                                 id BIGINT AUTO_INCREMENT,
                                 homework_id BIGINT NOT NULL,
                                 homework_question_id BIGINT NOT NULL,
                                 member_id BIGINT NOT NULL,
                                 answer VARCHAR(1000),
                                 PRIMARY KEY (id),
                                 FOREIGN KEY (homework_id) REFERENCES homeworks (id),
                                 FOREIGN KEY (homework_question_id) REFERENCES homework_questions (id),
                                 FOREIGN KEY (member_id) REFERENCES members (id)
);

-- surveys 테이블 생성
CREATE TABLE surveys (
                         id BIGINT AUTO_INCREMENT,
                         created_at TIMESTAMP(6),
                         member_id BIGINT NOT NULL,
                         updated_at TIMESTAMP(6),
                         title VARCHAR(50) NOT NULL,
                         additional_description VARCHAR(100),
                         description VARCHAR(300),
                         PRIMARY KEY (id),
                         FOREIGN KEY (member_id) REFERENCES members (id)
);

-- survey_questions 테이블 생성
CREATE TABLE survey_questions (
                                  id BIGINT AUTO_INCREMENT,
                                  is_answer_required BOOLEAN NOT NULL,
                                  survey_id BIGINT NOT NULL,
                                  content VARCHAR(100) NOT NULL,
                                  order_number VARCHAR(255) NOT NULL,
                                  type ENUM('CHOICE', 'ESSAY') NOT NULL,
                                  PRIMARY KEY (id),
                                  FOREIGN KEY (survey_id) REFERENCES surveys (id)
);

-- survey_question_choices 테이블 생성
CREATE TABLE survey_question_choices (
                                         id BIGINT AUTO_INCREMENT,
                                         order_number INT NOT NULL,
                                         survey_question_id BIGINT NOT NULL,
                                         description VARCHAR(100) NOT NULL,
                                         PRIMARY KEY (id),
                                         FOREIGN KEY (survey_question_id) REFERENCES survey_questions (id)
);

-- survey_replys 테이블 생성
CREATE TABLE survey_replys (
                               id BIGINT AUTO_INCREMENT,
                               created_at TIMESTAMP(6),
                               member_id BIGINT NOT NULL,
                               survey_id BIGINT NOT NULL,
                               survey_question_id BIGINT NOT NULL,
                               updated_at TIMESTAMP(6),
                               answer VARCHAR(400),
                               PRIMARY KEY (id),
                               FOREIGN KEY (member_id) REFERENCES members (id),
                               FOREIGN KEY (survey_id) REFERENCES surveys (id),
                               FOREIGN KEY (survey_question_id) REFERENCES survey_questions (id)
);

-- contacts 테이블 생성
CREATE TABLE contacts (
                          id BIGINT AUTO_INCREMENT,
                          user_name VARCHAR(255),
                          phone_number VARCHAR(50),
                          email VARCHAR(255),
                          title VARCHAR(255),
                          content VARCHAR(500),
                          contact_type ENUM('TRAINING', 'CLASS', 'ETC'),
                          created_at TIMESTAMP(6),
                          updated_at TIMESTAMP(6),
                          PRIMARY KEY (id)
);
