# 1	free
# 2	qna
# 3	project
# 4	rating


# 1. 기존 boardId을 저장하던 posts의 열을 수정한다.
ALTER TABLE posts CHANGE board_id board_type VARCHAR(40) NOT NULL;

# 2. id로 되어 있던 값들을 값으로 바꾼다.
update posts set board_type = "FREE"
where board_type = 1;

update posts set board_type = "QNA"
where board_type = 2;

update posts set board_type = "PROJECT"
where board_type = 3;

update posts set board_type = "RATING"
where board_type = 4;