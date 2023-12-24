-- member 테이블에 데이터 삽입
INSERT INTO users (id, email, password, name, point)
VALUES (1, 'kwon93@naver.com', 'kdh1234', '권동혁', 100);

-- member_roles 테이블에 데이터 삽입
INSERT INTO users_roles (users_id, roles)
VALUES (1, 'USER');
