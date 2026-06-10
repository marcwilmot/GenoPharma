package net.marcwilmot.GenoPharma.service.interfaces;

import net.marcwilmot.GenoPharma.constant.ErrorCode;
import net.marcwilmot.GenoPharma.constant.GenomeReference;
import net.marcwilmot.GenoPharma.constant.SampleStatus;
import net.marcwilmot.GenoPharma.dto.SampleDto;

import java.nio.file.Path;
import java.util.Optional;

public interface SampleService {


    GenomeReference inferGenomeReference(Optional<Integer> position);
    void insertSample(SampleDto sampleDto);

    void updateSampleStatusAndErrorCode(SampleStatus sampleStatus, Optional<ErrorCode> errorCode, Long sampleId);

    String quickSha256(Path path, Integer size);

    Boolean existsByVcfHash(String sha256);
}
