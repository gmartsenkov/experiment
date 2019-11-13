CREATE TABLE USERS(
   ID                   INT PRIMARY KEY NOT NULL,
   FIRST_NAME           CHAR(300) NOT NULL,
   LAST_NAME            CHAR(300) NOT NULL,
   EMAIL                CHAR(300) NOT NULL,
   PASSWORD             CHAR(300) NOT NULL,
   CREATED_AT           timestamp NOT NULL,
   UPDATED_AT           timestamp NOT NULL
);
