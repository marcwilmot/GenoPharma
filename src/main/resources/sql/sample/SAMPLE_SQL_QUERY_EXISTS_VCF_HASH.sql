select exists (
    select 1
    from sample
    where content_hash = :hashCode
);