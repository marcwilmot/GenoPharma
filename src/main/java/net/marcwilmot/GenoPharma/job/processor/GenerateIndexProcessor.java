package net.marcwilmot.GenoPharma.job.processor;

import htsjdk.samtools.SAMSequenceDictionary;
import htsjdk.tribble.Feature;
import htsjdk.tribble.index.IndexFactory;
import htsjdk.tribble.index.tabix.TabixFormat;
import htsjdk.tribble.index.tabix.TabixIndex;
import htsjdk.variant.vcf.VCFCodec;
import net.marcwilmot.GenoPharma.constant.SampleStatus;
import net.marcwilmot.GenoPharma.dto.SampleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@StepScope
public class GenerateIndexProcessor implements ItemProcessor<SampleDto, SampleDto> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public SampleDto process(SampleDto item) throws Exception {

        Path vcfGzPath = Path.of(item.getAbsolutePath());
        Path tbi = Path.of(item.getAbsolutePath() + ".tbi");
        if (Files.exists(tbi)) {
            SampleDto.builder()
                    .sampleId(item.getSampleId())
                    .status(SampleStatus.READY_TO_EXTRACT)
                    .errorCode(null)
                    .lastModifiedTime(item.getLastModifiedTime()) //todo modify
                    .build();
        }

        VCFCodec vcfCodec = new VCFCodec();

        @SuppressWarnings("unchecked")
        htsjdk.tribble.FeatureCodec<Feature, Object> codec =
                (htsjdk.tribble.FeatureCodec<Feature, Object>) (htsjdk.tribble.FeatureCodec<?, ?>) vcfCodec;

        SAMSequenceDictionary dict = null;

        TabixIndex index = IndexFactory.createTabixIndex(
                vcfGzPath,              // Path o File
                codec,
                TabixFormat.VCF,
                dict
        );

        index.write(tbi);


        return SampleDto.builder()
                .sampleId(item.getSampleId())
                .status(SampleStatus.READY_TO_EXTRACT)
                .errorCode(null)
                //.fileName(item.getFileName())
                //.absolutePath(item.getAbsolutePath())
                //.sizeBytes(item.getSizeBytes())
                //.createdAt(item.getCreatedAt())
                .lastModifiedTime(item.getLastModifiedTime()) //todo modify
                .build();
    }
}
