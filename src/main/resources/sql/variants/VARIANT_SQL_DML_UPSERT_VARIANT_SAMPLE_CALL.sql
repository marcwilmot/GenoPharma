INSERT INTO sample_variant_call (
    sample_id, variant_id, genotype, zygosity, depth, genotype_quality,
    variant_quality, filter, info_json, created_at
)
VALUES (
           :sampleId, :variantId, :genotype, :zygosity, :dp, :gq,
           :qual, :filter, CAST(:infoJson AS jsonb), :createdAt
       )
    ON CONFLICT (sample_id, variant_id)
DO UPDATE SET
    genotype = EXCLUDED.genotype,
           zygosity = EXCLUDED.zygosity,
           depth = EXCLUDED.depth,
           genotype_quality = EXCLUDED.genotype_quality,
           variant_quality = EXCLUDED.variant_quality,
           filter = EXCLUDED.filter,
           info_json = EXCLUDED.info_json;
