package net.marcwilmot.GenoPharma.infra;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Objects;

public class NamedParameterJdbcCursorItemReader<T> extends JdbcCursorItemReader<T> {

    private String namedSql;
    private SqlParameterSource parameterSource;

    public void setParameterSource(SqlParameterSource parameterSource) {
        this.parameterSource = parameterSource;
    }

    public void setNamedSql(String namedSql) {
        this.namedSql = namedSql;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Objects.requireNonNull(namedSql, "namedSql must not be null");
        Objects.requireNonNull(parameterSource, "parameterSource must not be null");

        ParsedSql parsed = NamedParameterUtils.parseSqlStatement(namedSql);
        String jdbcSql = NamedParameterUtils.substituteNamedParameters(parsed, parameterSource);

        super.setSql(jdbcSql);

        Object[] values =
                NamedParameterUtils.buildValueArray(parsed, parameterSource, null);

        super.setPreparedStatementSetter(ps -> {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
        });
        super.afterPropertiesSet();

    }
}
