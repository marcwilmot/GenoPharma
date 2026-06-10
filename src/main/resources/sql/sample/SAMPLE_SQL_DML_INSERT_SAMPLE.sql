INSERT INTO sample (
    content_hash,
    status,
    error_code,
    file_name,
    absolute_path,
    size_bytes,
    last_modified_time,
    reference_genome,
    created_at
) VALUES (
             :contentHash,
             :status,
             :errorCode,
             :fileName,
             :absolutePath,
             :sizeBytes,
             :lastModifiedTime,
             :referenceGenome,
             :createdAt
         );
