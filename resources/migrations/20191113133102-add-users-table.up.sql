CREATE TABLE USERS(
   ID                   SERIAL PRIMARY KEY NOT NULL,
   FIRST_NAME           text NOT NULL,
   LAST_NAME            text NOT NULL,
   EMAIL                text NOT NULL,
   PASSWORD             text NOT NULL,
   CREATED_AT           timestamp NOT NULL,
   UPDATED_AT           timestamp NOT NULL
);
