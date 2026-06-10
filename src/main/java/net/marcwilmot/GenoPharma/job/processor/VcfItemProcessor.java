package net.marcwilmot.GenoPharma.job.processor;

import htsjdk.variant.vcf.*;
import net.marcwilmot.GenoPharma.constant.ErrorCode;
import net.marcwilmot.GenoPharma.constant.GenomeReference;
import net.marcwilmot.GenoPharma.constant.SampleStatus;
import net.marcwilmot.GenoPharma.dto.SampleDto;
import net.marcwilmot.GenoPharma.dto.VcfFileDescriptorDto;
import net.marcwilmot.GenoPharma.service.interfaces.SampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Optional;

@StepScope
@Component
public class VcfItemProcessor implements ItemProcessor <VcfFileDescriptorDto, SampleDto> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Instant sampleDate;
    private final SampleService sampleService;

    public VcfItemProcessor(@Value("#{jobExecutionContext['sampleDate']}") Instant sampleDate, SampleService sampleService) {
        this.sampleDate = sampleDate;
        this.sampleService = sampleService;
    }

    @Override
    public SampleDto process(VcfFileDescriptorDto item) {

        LOG.info("Starting validations for the sample: {} ...", item.getFileName());

        //VALIDATE IF THE SIZE OF THE FILE IS > 0
        if (item.getSizeBytes() == 0)
            return failedSample(item, ErrorCode.FILE_EMPTY, null);

        Integer oneMB = 1048576;
        String hash = sampleService.quickSha256(Path.of(item.getAbsolutePath()), oneMB);

        if(sampleService.existsByVcfHash(hash))
            return  failedSample(item, ErrorCode.DUPLICATE_VCF, hash);

        //VALIDATE IF IS POSSIBLE TO OPEN THE VCF FILE
        try(VCFFileReader reader = new VCFFileReader(new File(item.getAbsolutePath()), false) ){
            //VALIDATE IF IS POSSIBLE TO PARSE THE HEADER
            VCFHeader header;
            try{
                header = reader.getFileHeader();
            } catch (Exception e){
                return failedSample(item, ErrorCode.VCF_HEADER_ERROR, hash);
            }

            Optional<Integer> chr1Length = header.getContigLines().stream()
                    .filter(c -> "1".equals(c.getID()) ||  "chr1".equals(c.getID()))
                    .map(c -> c.getSAMSequenceRecord().getSequenceLength())
                    .findFirst();

            //VALIDATE IS HAS KNOWED GENOMA REFERENCE
            GenomeReference genomeReference = sampleService.inferGenomeReference(chr1Length);
            if(genomeReference == null)
                return failedSample(item, ErrorCode.UNKNOWN_GENOME_REFERENCE, hash);

            //VALIDATE IF HAS GENOTYPE DATA
            if(!header.hasGenotypingData())
                return failedSample(item, ErrorCode.NO_GENOTYPING_DATA, hash);

            //VALIDATE IF IT HAS SAMPLES
            if(header.getNGenotypeSamples() != 1)
                return failedSample(item, ErrorCode.UNEXPECTED_SAMPLE_COUNT, hash);

            //VALIDATE IF HAVE THE FORMAT HEADER GT
            if(header.getFormatHeaderLine("GT") == null)
                return failedSample(item, ErrorCode.MISSING_GT, hash);

            //VALIDATE IF INDEX EXISTS
            try(VCFFileReader readerIndex = new VCFFileReader(new File(item.getAbsolutePath()), true)){
                if(!readerIndex.isQueryable())
                    return failedSample(item, ErrorCode.NO_INDEX, hash);

                LOG.info("Validations completed!");
                return SampleDto.builder()
                        .hashCode(hash)
                        .status(SampleStatus.READY_TO_EXTRACT)
                        .fileName(item.getFileName())
                        .absolutePath(item.getAbsolutePath())
                        .sizeBytes(item.getSizeBytes())
                        .referenceGenome(genomeReference.getGenomeReference())
                        .lastModifiedTime(sampleDate)
                        .createdAt(sampleDate)
                        .build();

            } catch (Exception e){
                return failedSample(item, ErrorCode.NO_INDEX, hash);
            }

        } catch (Exception e){
            return failedSample(item, ErrorCode.FILE_ERROR, hash);
        }
    }

    private SampleDto failedSample(VcfFileDescriptorDto item, ErrorCode errorCode, String hash) {
        LOG.warn("Sample {} failed {}", item.getFileName(), errorCode);
        return SampleDto.builder()
                .status(SampleStatus.FAILED)
                .hashCode(hash)
                .errorCode(errorCode)
                .fileName(item.getFileName())
                .absolutePath(item.getAbsolutePath())
                .sizeBytes(item.getSizeBytes())
                .createdAt(sampleDate)
                .lastModifiedTime(sampleDate)
                .build();
    }
}
