update sample s set
    status = :status,
    error_code = :errorCode,
    last_modified_time = :lastModifiedTime
where sample_id = :sampleId;