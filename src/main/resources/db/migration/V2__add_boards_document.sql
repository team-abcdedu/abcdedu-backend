-- unique 제약 조건 추가
ALTER TABLE boards
    ADD CONSTRAINT unique_boardName UNIQUE (name);

insert into boards (name)
values ('document');