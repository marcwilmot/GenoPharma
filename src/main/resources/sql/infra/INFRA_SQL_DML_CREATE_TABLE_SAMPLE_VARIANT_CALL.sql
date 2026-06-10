
CREATE TABLE  IF NOT EXISTS sample_variant_call (
                                     sample_variant_call_id BIGSERIAL PRIMARY KEY,

                                     sample_id      BIGINT NOT NULL,
                                     variant_id     BIGINT NOT NULL,

                                     genotype       VARCHAR(10)  NOT NULL,   -- ej: 0/1, 0/2, 1/1, ./.
                                     zygosity       VARCHAR(20)  NOT NULL,   -- HOM_REF, HET, HOM_VAR, NO_CALL

                                     depth          INTEGER,                 -- DP
                                     genotype_quality INTEGER,               -- GQ

                                     variant_quality DOUBLE PRECISION,       -- QUAL
                                     filter          VARCHAR(100),            -- PASS / LowQual / etc

                                     info_json      JSONB,                    -- INFO opcional

                                     created_at     TIMESTAMP NOT NULL,

                                     CONSTRAINT fk_svc_sample
                                         FOREIGN KEY (sample_id)
                                             REFERENCES sample (sample_id),

                                     CONSTRAINT fk_svc_variant
                                         FOREIGN KEY (variant_id)
                                             REFERENCES variant (variant_id),

                                     CONSTRAINT uq_sample_variant
                                         UNIQUE (sample_id, variant_id)
);
CREATE INDEX IF NOT EXISTS  idx_svc_sample
    ON sample_variant_call (sample_id);

CREATE INDEX IF NOT EXISTS  idx_svc_variant
    ON sample_variant_call (variant_id);

CREATE INDEX IF NOT EXISTS  idx_svc_zygosity
    ON sample_variant_call (zygosity);