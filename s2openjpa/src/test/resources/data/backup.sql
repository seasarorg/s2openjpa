SET TRACE_LEVEL_FILE 1;
SET WRITE_DELAY 500;
SET CLUSTER '';
SET DEFAULT_TABLE_TYPE 0;
SET DEFAULT_LOCK_TIMEOUT 1000;
SET TRACE_LEVEL_SYSTEM_OUT 0;
SET CACHE_SIZE 65536;
CREATE USER IF NOT EXISTS SA SALT '1d649ffe4df40a02' HASH '372fde30c35267c3d2e7d61cf823f3b5058f4b12f4977174a59023a97ee5c391' ADMIN;
CREATE CACHED TABLE PUBLIC.CUSTOMER(
    ID INTEGER NOT NULL,
    NAME VARCHAR(255),
    ADDRESS VARCHAR(255),
    PHONE VARCHAR(255),
    AGE INTEGER,
    BIRTHDAY DATE,
    SEX INTEGER,
    DTYPE VARCHAR(31),
    DANGEON VARCHAR(255),
    DANGEON_LEVEL INTEGER,
    VERSION INTEGER
);
-- 3 = SELECT COUNT(*) FROM PUBLIC.CUSTOMER;
INSERT INTO PUBLIC.CUSTOMER(ID, NAME, ADDRESS, PHONE, AGE, BIRTHDAY, SEX, DTYPE, DANGEON, DANGEON_LEVEL, VERSION) VALUES
(1, 'Link', 'Hyrule', '000-0000-0000', 15, DATE '1998-01-01', 0, 'C', NULL, NULL, 0),
(2, 'Zelda', 'Hyrule', '000-0000-0000', 15, DATE '1998-01-01', 1, 'C', NULL, NULL, 0),
(3, 'Ganon', 'Darkness', '000-0000-0000', 15, DATE '1998-01-01', 0, 'E', 'Pyramid of Power', 10, 0);
CREATE PRIMARY KEY ON PUBLIC.CUSTOMER(ID);
CREATE CACHED TABLE PUBLIC.PRODUCT(
    ID INTEGER NOT NULL,
    NAME VARCHAR(255),
    CUSTOMER_ID INTEGER,
    VERSION INTEGER
);
-- 3 = SELECT COUNT(*) FROM PUBLIC.PRODUCT;
INSERT INTO PUBLIC.PRODUCT(ID, NAME, CUSTOMER_ID, VERSION) VALUES
(1, 'MASTER SORD', 1, 0),
(2, 'OKARINA', 2, 0),
(3, 'TRIDENT', 3, 0);
CREATE PRIMARY KEY ON PUBLIC.PRODUCT(ID);
CREATE CACHED TABLE PUBLIC.SEQUENCE(
    SEQ_NAME VARCHAR(50) NOT NULL,
    SEQ_COUNT DECIMAL
);
-- 1 = SELECT COUNT(*) FROM PUBLIC.SEQUENCE;
INSERT INTO PUBLIC.SEQUENCE(SEQ_NAME, SEQ_COUNT) VALUES
('SEQ_GEN', 3);
CREATE PRIMARY KEY ON PUBLIC.SEQUENCE(SEQ_NAME);
ALTER TABLE PUBLIC.PRODUCT ADD CONSTRAINT PUBLIC.PRODUCTCUSTOMER_ID FOREIGN KEY(CUSTOMER_ID) REFERENCES PUBLIC.CUSTOMER(ID) NOCHECK;

CREATE TABLE NORMAL (
	ID INTEGER PRIMARY KEY
	,NAME VARCHAR(255)
	,AMOUNT DECIMAL
	,START_DATE DATE
	,CREATED_AT TIMESTAMP
	,LOCK_VERSION INTEGER
);
INSERT INTO NORMAL(ID,NAME,AMOUNT,START_DATE,CREATED_AT,LOCK_VERSION) VALUES
(1,'名前１',12300,PARSEDATETIME('2005-03-02','yyyy-MM-dd'),PARSEDATETIME('2006-05-02 22:34:25','yyyy-MM-dd HH:mm:ss'),0),
(2,'名前２',22340,PARSEDATETIME('2003-05-30','yyyy-MM-dd'),PARSEDATETIME('2007-10-21 05:39:55','yyyy-MM-dd HH:mm:ss'),0);
CREATE SEQUENCE SEQ_NOMAL START WITH 3;

CREATE TABLE EMBEDDEDSAMPLE (
	PK1 INTEGER
	,PK2 DATE
	,NAME　VARCHAR(255)
	,PRIMARY KEY (PK1,PK2)
);
INSERT INTO EMBEDDEDSAMPLE(PK1,PK2,NAME) VALUES
(1,PARSEDATETIME('2008-01-03', 'yyyy-MM-dd'),'名前１'),
(2,PARSEDATETIME('2006-03-30', 'yyyy-MM-dd'),'名前２');

CREATE TABLE JOINDSAMPLE (
	ID INTEGER PRIMARY KEY
	,D_NAME VARCHAR(255)
);
CREATE TABLE JOIND_SAMPLE_CHILD (
	ID INTEGER PRIMARY KEY
	,CHILDNAME VARCHAR(255)
	,FOREIGN KEY(ID) REFERENCES JOINDSAMPLE(ID) 
);
CREATE TABLE JOINDSAMPLECHILD2 (
	ID INTEGER PRIMARY KEY
	,CHILD_NAME VARCHAR(255)
	,DATE1 DATE
	,DATE2 TIMESTAMP
	,DECIMAL DECIMAL
	,FOREIGN KEY(ID) REFERENCES JOINDSAMPLE(ID) 
);

INSERT INTO JOINDSAMPLE(ID,D_NAME) VALUES
(1,'名前１'),
(2,'名前２'),
(3,'名前３');
INSERT INTO JOIND_SAMPLE_CHILD(ID,CHILDNAME) VALUES
(2,'子名前２');
INSERT INTO JOINDSAMPLECHILD2(ID,CHILD_NAME,DATE1,DATE2,DECIMAL) VALUES
(3,'子名前３',PARSEDATETIME('2008-06-03','yyyy-MM-dd'),PARSEDATETIME('2007-05-25 10:32:45', 'yyyy-MM-dd HH:mm:ss'),12345);

CREATE TABLE TABLEPERCLASSSAMPLE (
	ID INTEGER PRIMARY KEY
	,NAME VARCHAR(255)
);
CREATE TABLE TABLEPERCLASSCHILD (
	ID INTEGER PRIMARY KEY
	,NAME VARCHAR(255)
	,CHILDNAME VARCHAR(255)	
	,DATE1 DATE
	,DATE2 TIMESTAMP
	,DECIMAL DECIMAL
);

INSERT INTO TABLEPERCLASSSAMPLE(ID,NAME) VALUES
(1,'名前１');

INSERT INTO TABLEPERCLASSCHILD(ID,NAME,CHILDNAME,DATE1,DATE2,DECIMAL) VALUES
(2,'名前２','子名前２',PARSEDATETIME('2008-06-03','yyyy-MM-dd'),PARSEDATETIME('2007-05-25 10:32:45', 'yyyy-MM-dd HH:mm:ss'),12345);
