WITH upsert AS (
INSERT INTO variant (genome_build, chromosome, position, ref, alt, variant_type, rsid, created_at)
VALUES (:build, :chr, :pos, :ref, :alt, :type, :rsid, :createdAt)
ON CONFLICT (genome_build, chromosome, position, ref, alt)
    DO UPDATE
           SET rsid = EXCLUDED.rsid
       WHERE variant.rsid IS NULL
         AND EXCLUDED.rsid IS NOT NULL
           RETURNING variant_id
           )
SELECT variant_id
FROM upsert
UNION ALL
SELECT v.variant_id
FROM variant v
WHERE v.genome_build = :build
  AND v.chromosome   = :chr
  AND v.position     = :pos
  AND v.ref          = :ref
  AND v.alt          = :alt
  AND NOT EXISTS (SELECT 1 FROM upsert);