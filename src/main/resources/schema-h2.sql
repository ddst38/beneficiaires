-- Définition de la table à créer en base
-- uniquement pour plateforme H2

SET MODE PostgreSQL;

CREATE TABLE skmb_beneficiaire_jdbc (
    beneficiaire_id BIGINT      NOT NULL,
    matricule       VARCHAR(13) NOT NULL,
    date_naissance  DATE        NOT NULL,
    rang            SMALLINT    NOT NULL,
    nom             VARCHAR(70) NOT NULL, -- 70 char car chiffrage
    prenom          VARCHAR(70) NOT NULL, -- 70 char car chiffrage
    CONSTRAINT skmb_beneficiaire_jdbc_pk    PRIMARY KEY (beneficiaire_id),
    CONSTRAINT skmb_beneficiaire_jdbc_uk1   UNIQUE (matricule, date_naissance, rang)
) ;
CREATE INDEX skmb_beneficiaire_jdbc_n1 ON skmb_beneficiaire_jdbc (matricule);
CREATE SEQUENCE skmb_beneficiaire_jdbc_beneficiaire_id_seq INCREMENT BY 1;
ALTER TABLE skmb_beneficiaire_jdbc ALTER COLUMN beneficiaire_id SET DEFAULT nextval ('skmb_beneficiaire_jdbc_beneficiaire_id_seq');
