-- 스키마 수정: 생성일자, 수정일자 필드 추가
ALTER TABLE homeworks
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP;

-- 기존 데이터에 필드 기본값 업데이트
UPDATE homeworks
SET created_at = COALESCE(created_at, CURRENT_TIMESTAMP),
    updated_at = COALESCE(updated_at, CURRENT_TIMESTAMP);
