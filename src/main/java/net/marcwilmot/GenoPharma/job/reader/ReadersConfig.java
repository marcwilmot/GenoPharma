package net.marcwilmot.GenoPharma.job.reader;

import net.marcwilmot.GenoPharma.constant.ErrorCode;
import net.marcwilmot.GenoPharma.constant.SampleStatus;
import net.marcwilmot.GenoPharma.constant.SqlQueryConstants;
import net.marcwilmot.GenoPharma.dao.mapper.SampleRowMapper;
import net.marcwilmot.GenoPharma.dto.SampleDto;
import net.marcwilmot.GenoPharma.infra.NamedParameterJdbcCursorItemReader;
import net.marcwilmot.GenoPharma.infra.SqlScriptHelper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.DataSource;

@Configuration
public class ReadersConfig {

    private final SqlScriptHelper sqlScriptHelper;

    public ReadersConfig(SqlScriptHelper sqlScriptHelper) {
        this.sqlScriptHelper = sqlScriptHelper;
    }

    @Bean(name = "generateIndexReader")
    @StepScope
    public NamedParameterJdbcCursorItemReader<SampleDto> GenerateIndexReader(DataSource dataSource){

        NamedParameterJdbcCursorItemReader<SampleDto> reader = new NamedParameterJdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setRowMapper(new SampleRowMapper());
        reader.setNamedSql(sqlScriptHelper.getSql(SqlQueryConstants.SAMPLE_SQL_QUERY_SELECT_ALL_NO_INDEX_SAMPLES));

        reader.setParameterSource(new MapSqlParameterSource()
                .addValue("status", SampleStatus.FAILED.getCode())
                .addValue("errorCode", ErrorCode.NO_INDEX.getCode()));

        return reader;

    }

    @Bean(name = "extractVariantsReader")
    @StepScope
    public NamedParameterJdbcCursorItemReader<SampleDto> ExtractVariantsReader(DataSource dataSource){

        NamedParameterJdbcCursorItemReader<SampleDto> reader = new NamedParameterJdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setRowMapper(new SampleRowMapper());
        reader.setNamedSql(sqlScriptHelper.getSql(SqlQueryConstants.SAMPLE_SQL_QUERY_SELECT_ALL_READY_TO_EXTRACT));

        reader.setParameterSource(new MapSqlParameterSource()
                .addValue("status", SampleStatus.READY_TO_EXTRACT.getCode()));

        return reader;

    }
}
