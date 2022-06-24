
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--gis table
CREATE TABLE IF NOT EXISTS gis
(
   iudx_resource_id character varying NOT NULL,
   url varchar NOT NULL,
   isOpen BOOLEAN NOT NULL,
   port integer NOT NULL,
   created_at timestamp without time zone NOT NULL,
   modified_at timestamp without time zone NOT NULL,
   username varchar,
   password varchar,
   tokenurl character varying,
   CONSTRAINT gis_pk PRIMARY KEY (iudx_resource_id)
);

---
-- Functions for audit[cerated,updated] on table/column
---

-- modified_at column function
CREATE
OR REPLACE
   FUNCTION update_modified () RETURNS TRIGGER AS '
BEGIN NEW.modified_at = now ();
RETURN NEW;
END;
' language 'plpgsql';

-- created_at column function
CREATE
OR REPLACE
   FUNCTION update_created () RETURNS TRIGGER AS '
BEGIN NEW.created_at = now ();
RETURN NEW;
END;
' language 'plpgsql';

-- gis table
CREATE TRIGGER update_rt_created BEFORE INSERT ON gis FOR EACH ROW EXECUTE PROCEDURE update_created ();
CREATE TRIGGER update_rt_modified BEFORE INSERT
OR UPDATE ON
   gis FOR EACH ROW EXECUTE PROCEDURE update_modified ();
   
   
 INSERT INTO gis(iudx_resource_id, url, isopen, port)
    VALUES ('iisc.ac.in/89a36273d77dac4cf38114fca1bbe64392547f86/rs.iudx.io/surat-itms-realtime-information/surat-itms-live-eta_test', 'https://map.surat.gov.in/surat', false, 1234);
 
 INSERT INTO gis(iudx_resource_id, url, isopen, port,username,password,tokenurl)
    VALUES ('iisc.ac.in/89a36273d77dac4cf38114fca1bbe64392547f86/rs.iudx.io/varanasi/varanasi_test', 'https://map.varanasismartcity.gov.in/varanasismartcity', true, 1234,'username','password','URL');
 
 
 
 
 
 
 
 
 
 
 
 
   