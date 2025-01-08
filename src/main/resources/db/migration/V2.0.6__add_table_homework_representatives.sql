-- 공통과제 중 대표 과제 정보를 가진 테이블을 생성한다.
CREATE TABLE homework_representatives (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    homework_id BIGINT NOT NULL,
    member_id BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_homework FOREIGN KEY (homework_id) REFERENCES homeworks (id),
    CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES members (id)
);
