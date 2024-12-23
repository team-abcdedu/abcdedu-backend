-- 스키마 수정 : 생성일자, 수정일자 필드 추가
ALTER TABLE homework_replys
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE homework_replys
    ADD COLUMN updated_at TIMESTAMP;



