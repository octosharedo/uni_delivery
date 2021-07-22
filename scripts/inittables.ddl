CREATE TABLE clt (
    id     VARCHAR(7) NOT NULL,
    name   VARCHAR(30) NOT NULL,
    phone  VARCHAR(11) NOT NULL
);

ALTER TABLE clt ADD CONSTRAINT clt_pk PRIMARY KEY ( id );

CREATE TABLE dlv (
    id               VARCHAR(7) NOT NULL,
    name             VARCHAR(30) NOT NULL,
    phone            VARCHAR(11) NOT NULL,
    employment_date  DATETIME NOT NULL,
    payment_info     VARCHAR(50) NOT NULL,
    free             BLOB NOT NULL
);

ALTER TABLE dlv ADD CONSTRAINT dlv_pk PRIMARY KEY ( id );

ALTER TABLE dlv ADD CONSTRAINT dlv_phone__un UNIQUE ( phone );

CREATE TABLE opr (
    quantity  SMALLINT NOT NULL,
    ord_id    VARCHAR(7) NOT NULL,
    prd_id    VARCHAR(7) NOT NULL
);

ALTER TABLE opr ADD CONSTRAINT opr_pk PRIMARY KEY ( ord_id,
                                                    prd_id );

CREATE TABLE ord (
    id            VARCHAR(7) NOT NULL,
    cost          DECIMAL(7, 2) NOT NULL,
    address       VARCHAR(50) NOT NULL,
    payment       VARCHAR(50) NOT NULL,
    complete      BLOB NOT NULL,
    datetime      DATETIME NOT NULL,
    deliverytime  SMALLINT NOT NULL,
    dlv_id        VARCHAR(7) NOT NULL,
    clt_id        VARCHAR(7) NOT NULL
);

ALTER TABLE ord ADD CONSTRAINT ord_pk PRIMARY KEY ( id );

CREATE TABLE prd (
    id           VARCHAR(7) NOT NULL,
    name         VARCHAR(20) NOT NULL,
    category     VARCHAR(15) NOT NULL,
    price        DECIMAL(6, 2) NOT NULL,
    description  VARCHAR(100)
);

ALTER TABLE prd ADD CONSTRAINT prd_pk PRIMARY KEY ( id );

CREATE TABLE spr (
    prd_id    VARCHAR(7) NOT NULL,
    wrh_id    VARCHAR(3) NOT NULL,
    quantity  SMALLINT NOT NULL
);

ALTER TABLE spr ADD CONSTRAINT spr_pk PRIMARY KEY ( prd_id,
                                                    wrh_id );

CREATE TABLE wrh (
    id       VARCHAR(3) NOT NULL,
    address  VARCHAR(50) NOT NULL,
    phone    VARCHAR(11) NOT NULL
);

ALTER TABLE wrh ADD CONSTRAINT wrh_pk PRIMARY KEY ( id );

ALTER TABLE opr
    ADD CONSTRAINT opr_ord_fk FOREIGN KEY ( ord_id )
        REFERENCES ord ( id );

ALTER TABLE opr
    ADD CONSTRAINT opr_prd_fk FOREIGN KEY ( prd_id )
        REFERENCES prd ( id );

ALTER TABLE ord
    ADD CONSTRAINT ord_clt_fk FOREIGN KEY ( clt_id )
        REFERENCES clt ( id );

ALTER TABLE ord
    ADD CONSTRAINT ord_dlv_fk FOREIGN KEY ( dlv_id )
        REFERENCES dlv ( id );

ALTER TABLE spr
    ADD CONSTRAINT spr_prd_fk FOREIGN KEY ( prd_id )
        REFERENCES prd ( id );

ALTER TABLE spr
    ADD CONSTRAINT spr_wrh_fk FOREIGN KEY ( wrh_id )
        REFERENCES wrh ( id );


