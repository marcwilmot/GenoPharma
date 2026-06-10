package net.marcwilmot.GenoPharma.dao.impl;

import net.marcwilmot.GenoPharma.constant.ErrorCode;
import net.marcwilmot.GenoPharma.constant.SampleStatus;
import net.marcwilmot.GenoPharma.constant.SqlDmlConstants;
import net.marcwilmot.GenoPharma.constant.SqlQueryConstants;
import net.marcwilmot.GenoPharma.dao.interfaces.SampleDao;
import net.marcwilmot.GenoPharma.dto.SampleDto;
import net.marcwilmot.GenoPharma.infra.SqlScriptHelper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Repository
public class SampleDaoImpl implements SampleDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SqlScriptHelper sqlScriptHelper;

    public SampleDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, SqlScriptHelper sqlScriptHelper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.sqlScriptHelper = sqlScriptHelper;
    }

    @Override
    public void insertSample(SampleDto sampleDto) {

        final MapSqlParameterSource queryParams = new MapSqlParameterSource();

        queryParams.addValue("contentHash", sampleDto.getHashCode());
        queryParams.addValue("status", sampleDto.getStatus().getCode());
        queryParams.addValue("errorCode", sampleDto.getErrorCode() !=null ? sampleDto.getErrorCode().getCode() : null);
        queryParams.addValue("fileName", sampleDto.getFileName());
        queryParams.addValue("absolutePath", sampleDto.getAbsolutePath());
        queryParams.addValue("sizeBytes", sampleDto.getSizeBytes());
        queryParams.addValue("referenceGenome", sampleDto.getReferenceGenome());
        queryParams.addValue("lastModifiedTime", Timestamp.from(sampleDto.getLastModifiedTime()));
        queryParams.addValue("createdAt", Timestamp.from(sampleDto.getCreatedAt()));


        namedParameterJdbcTemplate.update(sqlScriptHelper.getSql(SqlDmlConstants.SAMPLE_SQL_DML_INSERT_SAMPLE), queryParams);

    }

    @Override
    public void updateSampleStatusAndErrorCode(SampleStatus sampleStatus, Optional<ErrorCode> errorCode, Long sampleId){

        final MapSqlParameterSource queryParams = new MapSqlParameterSource();
        queryParams.addValue("status", sampleStatus.getCode());
        queryParams.addValue("errorCode", errorCode.map(ErrorCode::getCode).orElse(null));
        queryParams.addValue("sampleId", sampleId);
        queryParams.addValue("lastModifiedTime", Timestamp.from(Instant.now())); //todo takeit from stepcontext

        namedParameterJdbcTemplate.update(sqlScriptHelper.getSql(SqlDmlConstants.SAMPLE_SQL_DML_UPDATE_STATUS_AND_ERROR_CODE), queryParams);
    }

    @Override
    public Boolean existsByVcfHash(String sha256) {

        final MapSqlParameterSource queryParams = new MapSqlParameterSource();
        queryParams.addValue("hashCode", sha256);

        Boolean ex = namedParameterJdbcTemplate.queryForObject(sqlScriptHelper.getSql(SqlQueryConstants.SAMPLE_SQL_QUERY_EXISTS_VCF_HASH),queryParams, Boolean.class);

        return  ex;
    }

}
