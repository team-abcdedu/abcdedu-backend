-- 1,000자 제한이었던 것을 2,000자로 늘렸다.
ALTER TABLE posts ALTER COLUMN content VARCHAR(2000);

