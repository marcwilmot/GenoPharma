INSERT INTO variant (genome_build, chromosome, position, ref, alt, variant_type, rsid, created_at)
VALUES (:build, :chr, :pos, :ref, :alt, :type, :rsid, :createdAt)
    ON CONFLICT (genome_build, chromosome, position, ref, alt)
    DO UPDATE
               SET rsid = EXCLUDED.rsid
       WHERE variant.rsid IS NULL
         AND EXCLUDED.rsid IS NOT NULL;