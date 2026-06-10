CREATE TABLE IF NOT EXISTS  variant (
                         variant_id       BIGSERIAL PRIMARY KEY,

                         genome_build     VARCHAR(10)  NOT NULL,   -- GRCh37 / GRCh38
                         chromosome       VARCHAR(5)   NOT NULL,   -- 1..22, X, Y, MT
                         position         INTEGER      NOT NULL,   -- 1-based
                         ref              VARCHAR(500) NOT NULL,
                         alt              VARCHAR(500) NOT NULL,

                         variant_type     VARCHAR(20),             -- SNP, INDEL, MNP, etc
                         rsid             VARCHAR(50),

                         created_at       TIMESTAMP NOT null,

                         CONSTRAINT uq_variant UNIQUE (
                                                       genome_build,
                                                       chromosome,
                                                       position,
                                                       ref,
                                                       alt
                             )
);
CREATE INDEX IF NOT EXISTS  idx_variant_locus
    ON variant (chromosome, position);

CREATE INDEX IF NOT EXISTS idx_variant_rsid
    ON variant (rsid);