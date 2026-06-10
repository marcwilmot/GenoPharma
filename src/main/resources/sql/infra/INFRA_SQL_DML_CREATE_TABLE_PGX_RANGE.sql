CREATE TABLE IF NOT EXISTS PGX_RANGE (
                                         gene_name      VARCHAR(64) PRIMARY KEY,
    chromosome     VARCHAR(5)  NOT NULL,
    grch37_start   INTEGER      NOT NULL,
    grch37_end     INTEGER      NOT NULL,
    grch38_start   INTEGER      NOT NULL,
    grch38_end     INTEGER      NOT NULL,
    strand         CHAR(1)   NOT NULL
    );