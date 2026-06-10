package net.marcwilmot.GenoPharma.constant;

public class SqlDmlConstants {

    private SqlDmlConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String INFRA_SQL_DML_CREATE_TABLE_SAMPLE = "infra/INFRA_SQL_DML_CREATE_TABLE_SAMPLE";
    public static final String INFRA_SQL_DML_CREATE_TABLE_PGX_RANGE = "infra/INFRA_SQL_DML_CREATE_TABLE_PGX_RANGE";
    public static final String INFRA_SQL_DML_CREATE_TABLE_SAMPLE_VARIANT_CALL = "infra/INFRA_SQL_DML_CREATE_TABLE_SAMPLE_VARIANT_CALL";
    public static final String INFRA_SQL_DML_CREATE_TABLE_VARIANT = "infra/INFRA_SQL_DML_CREATE_TABLE_VARIANT";
    public static final String INFRA_SQL_DML_INSERT_PGX_RANGE = "infra/INFRA_SQL_DML_INSERT_PGX_RANGE";

    public static final String SAMPLE_SQL_DML_INSERT_SAMPLE = "sample/SAMPLE_SQL_DML_INSERT_SAMPLE";
    public static final String SAMPLE_SQL_DML_UPDATE_STATUS_AND_ERROR_CODE = "sample/SAMPLE_SQL_DML_UPDATE_STATUS_AND_ERROR_CODE";
    public static final String VARIANT_SQL_DML_UPSERT_VARIANT_RETURN_VARIANT_ID = "variants/VARIANT_SQL_DML_UPSERT_VARIANT_RETURN_VARIANT_ID";
    public static final String VARIANT_SQL_DML_UPSERT_VARIANT = "variants/VARIANT_SQL_DML_UPSERT_VARIANT";
    public static final String VARIANT_SQL_DML_FETCH_IDS_BY_KEYS = "variants/VARIANT_SQL_DML_FETCH_IDS_BY_KEYS";
    public static final String VARIANT_SQL_DML_UPSERT_VARIANT_SAMPLE_CALL = "variants/VARIANT_SQL_DML_UPSERT_VARIANT_SAMPLE_CALL";
}
