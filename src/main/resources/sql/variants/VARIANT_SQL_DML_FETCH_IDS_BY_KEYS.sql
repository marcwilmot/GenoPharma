WITH v (genome_build, chromosome, position, ref, alt) AS (
    VALUES
    {{VALUES}}
    )
SELECT
    va.variant_id,
    va.genome_build,
    va.chromosome,
    va.position,
    va.ref,
    va.alt
FROM variant va
         JOIN v
              ON va.genome_build = v.genome_build
                  AND va.chromosome   = v.chromosome
                  AND va.position     = v.position
                  AND va.ref          = v.ref
                  AND va.alt          = v.alt;
