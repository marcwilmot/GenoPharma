package net.marcwilmot.GenoPharma.dao.interfaces;

import net.marcwilmot.GenoPharma.constant.ErrorCode;
import net.marcwilmot.GenoPharma.constant.SampleStatus;
import net.marcwilmot.GenoPharma.dto.SampleDto;

import java.util.Optional;

public interface SampleDao {

    void insertSample(SampleDto sampleDto);
    void updateSampleStatusAndErrorCode(SampleStatus sampleStatus, Optional<ErrorCode> errorCode, Long sampleId);
    Boolean existsByVcfHash(String sha256);
}
