CREATE TABLE CATEGORIES(
   ID                   SERIAL PRIMARY KEY NOT NULL,
   NAME                 text NOT NULL,
   CREATED_AT           timestamp NOT NULL DEFAULT NOW(),
   UPDATED_AT           timestamp NOT NULL DEFAULT NOW()
);
