ALTER TABLE posts
    MODIFY COLUMN deleted BOOLEAN DEFAULT FALSE;

ALTER TABLE comments
    MODIFY COLUMN deleted BOOLEAN DEFAULT FALSE;
