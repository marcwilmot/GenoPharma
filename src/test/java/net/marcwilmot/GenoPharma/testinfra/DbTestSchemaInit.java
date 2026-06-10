package net.marcwilmot.GenoPharma.testinfra;

import net.marcwilmot.GenoPharma.constant.SqlDmlConstants;
import net.marcwilmot.GenoPharma.infra.SqlScriptHelper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public final class DbTestSchemaInit {

    private DbTestSchemaInit() {}

    public static void createGenoPharmaSchemas(JdbcTemplate jdbc, SqlScriptHelper sqlScriptHelper) {

        List<String> ddlQueries = List.of(
                SqlDmlConstants.INFRA_SQL_DML_CREATE_TABLE_SAMPLE,
                SqlDmlConstants.INFRA_SQL_DML_CREATE_TABLE_PGX_RANGE,
                SqlDmlConstants.INFRA_SQL_DML_CREATE_TABLE_VARIANT,
                SqlDmlConstants.INFRA_SQL_DML_CREATE_TABLE_SAMPLE_VARIANT_CALL,
                SqlDmlConstants.INFRA_SQL_DML_INSERT_PGX_RANGE
        );

        for(String query : ddlQueries )
            jdbc.execute(sqlScriptHelper.getSql(query));

    }

    public static void truncateGenoPharmaTables(JdbcTemplate jdbc) {
        jdbc.execute("""
        TRUNCATE TABLE
            sample_variant_call,
            sample,
            variant,
            pgx_range
        RESTART IDENTITY
        CASCADE;
    """);

    }
}
