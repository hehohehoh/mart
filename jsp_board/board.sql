
 create table hr.t_member(
 id varchar2(10) primary key,
 pwd varchar2(10) not null,
 name varchar2(50),
 email varchar2(50) unique,       
 joindate date
 ) tablespace example;
 



select sysdate from dual;

desc t_member;


create table hr.t_board(
 articleNO number(10) primary key,
 parentNO number(10) default 0 not null,
 title varchar2(100) not null,
 content CLOB not null,
 imageFileName varchar2(100),
 writeDate timestamp(0) default systimestamp(0) not null,
 id varchar2(20) not null,
 constraint FK_PNO FOREIGN KEY(parentNO) references hr.t_board(articleNO) on delete cascade,
 constraint FK_ID FOREIGN KEY(id) references hr.t_member(id) on delete cascade
) tablespace example;

drop table t_board;

insert into t_member values
 ('admin', 1111, '관리자', 'admin@goott.com', sysdate);

insert into hr.t_board values
 (0, default, 'baseTitle', 'baseContent', null, default, 'admin'); 

INSERT INTO hr.t_board VALUES
(1,0,'테스트글입니다.','테스트글입니다',NULL, DEFAULT, 'hong') ;

INSERT INTO hr.t_board VALUES
(2,0,'안녕하세요.','상품후기입니다',NULL, DEFAULT, 'hong') ;

INSERT INTO hr.t_board VALUES
(3,2,'답변입니다.','상품후기에 대한 답변입니다.',NULL, DEFAULT, 'hong') ;

INSERT INTO hr.t_board VALUES
(4,0,'김유신입니다.','김유신 테스트글입니다',NULL, DEFAULT, 'kim') ;

INSERT INTO hr.t_board VALUES
(5,3,'답변입니다.','상품 좋습니다.',NULL, DEFAULT, 'lee') ;

INSERT INTO hr.t_board VALUES
(6,2,'상품 후기입니다.','이순신의 상품 사용 후기를 올립니다',NULL, DEFAULT, 'lee') ;

INSERT INTO hr.t_board VALUES
(7,0,'테스트글입니다.','테스트글입니다',NULL, DEFAULT, 'hong') ;

INSERT INTO hr.t_board VALUES
(8,0,'안녕하세요.','상품후기입니다',NULL, DEFAULT, 'hong') ;

INSERT INTO hr.t_board VALUES
(9,2,'답변입니다.','상품후기에 대한 답변입니다.',NULL, DEFAULT, 'hong') ;

INSERT INTO hr.t_board VALUES
(10,0,'김유신입니다.','김유신 테스트글입니다',NULL, DEFAULT, 'kim') ;

INSERT INTO hr.t_board VALUES
(11,3,'답변입니다.','상품 좋습니다.',NULL, DEFAULT, 'lee') ;

INSERT INTO hr.t_board VALUES
(12,2,'상품 후기입니다.','이순신의 상품 사용 후기를 올립니다',NULL, DEFAULT, 'lee') ;

INSERT INTO hr.t_board VALUES
(13,12,'김유신입니다.','김유신 테스트글입니다',NULL, DEFAULT, 'kim') ;

INSERT INTO hr.t_board VALUES
(14,11,'답변입니다.','상품 좋습니다.',NULL, DEFAULT, 'lee') ;

INSERT INTO hr.t_board VALUES
(15,13,'상품 후기입니다.','이순신의 상품 사용 후기를 올립니다',NULL, DEFAULT, 'lee') ;

alter table

COMMIT ;
select * from t_board;
commit;
rollback;

select level, articleNO, parentNO, id 
from t_board
where articleNO != 0
start with parentNO = 0
connect by prior articleNO = parentNO and articleNO != 0
order siblings by articleNO desc;


select level, articleNO, parentNO, 
        LPAD(' ', 4*(level-1)) || title title, content, writeDate
from t_board
start with parentNO = 0
connect by prior articleNO = parentNO
order siblings by articleNO desc;

alter table hr.t_board
drop constraint FK_PNO;

delete from t_board where articleNO = 0; 
commit;

desc t_board;

create sequence ano_seq
start with 23
increment by 1
maxvalue 999999999
nocycle;


select ano_seq.nextval from dual;
select ano_seq.currval from dual;