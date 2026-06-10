package net.marcwilmot.GenoPharma.dao.mapper;

import net.marcwilmot.GenoPharma.constant.ErrorCode;
import net.marcwilmot.GenoPharma.constant.SampleStatus;
import net.marcwilmot.GenoPharma.dto.SampleDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SampleRowMapper implements RowMapper<SampleDto> {
    @Override
    public SampleDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer errorCodeDb = rs.getObject("error_code", Integer.class);
        ErrorCode errorCode = errorCodeDb != null ? ErrorCode.getErrorCode(errorCodeDb) : null;

        return SampleDto.builder()
                .sampleId(rs.getLong("sample_id"))
                .referenceGenome(rs.getString("reference_genome"))
                .hashCode(rs.getString("content_hash")) //todo rename for hash_code in db
                .status(SampleStatus.getSampleStatus(rs.getInt("status")))
                .errorCode(errorCode)
                .fileName(rs.getString("file_name"))
                .absolutePath(rs.getString("absolute_path"))
                .sizeBytes(rs.getLong("size_bytes"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .lastModifiedTime(rs.getTimestamp("last_modified_time").toInstant())
                .build();
    }
}
