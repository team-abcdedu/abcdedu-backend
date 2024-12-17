-- 스키마 수정 : 생성일자, 수정일자 필드 추가
ALTER TABLE homework_replys
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE homework_replys
    ADD COLUMN updated_at TIMESTAMP;

-- 기존 데이터에 필드 기본값 업데이트 : 자동 집계되는 날짜와 비교 하기 위해 특정 이전값으로 설정
UPDATE homework_replys
SET created_at = '2024-11-01 10:00:00',
    updated_at = '2024-11-01 10:00:00'
WHERE created_at IS NULL OR updated_at IS NULL;


