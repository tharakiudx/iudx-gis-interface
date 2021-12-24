--gis table
CREATE TABLE IF NOT EXISTS gis
(
   _id uuid NOT NULL,
   url varchar NOT NULL,
   isOpen BOOLEAN NOT NULL,
   api varchar NOT NULL,
   port NUMBER NOT NULL,
   created_at timestamp without time zone NOT NULL,
   modified_at timestamp without time zone NOT NULL,
   CONSTRAINT gis_pk PRIMARY KEY (_id)
)
-- Functions for audit[new,update] on table/column
-- modified_at column function
CREATE
OR REPLACE
   FUNCTION update_modified () RETURNS TRIGGER AS $$
BEGIN NEW.modified_at = now ();
RETURN NEW;
END;
$$ language 'plpgsql';
-- created_at column function
CREATE
OR REPLACE
   FUNCTION update_created () RETURNS TRIGGER AS $$
BEGIN NEW.created_at = now ();
RETURN NEW;
END;
$$ language 'plpgsql';
-- Triggers for gis
-- gis table
CREATE TRIGGER update_gis_modified BEFORE INSERT OR UPDATE ON gis FOR EACH ROW EXECUTE PROCEDURE update_modified ();
CREATE TRIGGER update_gis_created BEFORE INSERT ON gis FOR EACH ROW EXECUTE PROCEDURE update_created ();