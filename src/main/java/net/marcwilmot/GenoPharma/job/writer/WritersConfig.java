package net.marcwilmot.GenoPharma.job.writer;

import net.marcwilmot.GenoPharma.dto.*;
import net.marcwilmot.GenoPharma.dto.*;
import net.marcwilmot.GenoPharma.service.interfaces.ExtractVariantsService;
import net.marcwilmot.GenoPharma.service.interfaces.SampleService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class WritersConfig {

    private final SampleService sampleService;
    private final ExtractVariantsService extractVariantsService;

    public WritersConfig(SampleService sampleService, ExtractVariantsService extractVariantsService) {
        this.sampleService = sampleService;
        this.extractVariantsService = extractVariantsService;
    }

    @Bean(name = "vcfItemWriter")
    @StepScope
    public ItemWriter<SampleDto> vcfItemWriter(){

        return chunk -> {
            for(SampleDto item : chunk){
                sampleService.insertSample(item);
            }
        };
    }

    @Bean(name = "generateIndexWriter")
    @StepScope
    public ItemWriter<SampleDto> generateIndexWriter(){

        return updateStatusWriter();

    }
    @Bean(name = "extractVariantsWriter")
    @StepScope
    public ItemWriter<VariantSampleWrapper> extractVariantsWriter(){

        return chunk -> {
            for(VariantSampleWrapper item : chunk){
                List<VariantsAndCallDto> calls = item.getVariantsAndCalls();

                Map<VariantKey, VariantDto> uniqueVariants = new LinkedHashMap<>();
                for(VariantsAndCallDto c : calls){
                    VariantDto v = c.getVariantDto();
                    uniqueVariants.putIfAbsent(v.key(), v);
                }

                List<VariantDto> variantsToUpsert = new ArrayList<>(uniqueVariants.values());

                extractVariantsService.upsertVariantsBatch(variantsToUpsert);


                List<VariantKey> keys = new ArrayList<>(uniqueVariants.keySet());

                Map<VariantKey, Integer> idsByKey = extractVariantsService.fetchVariantIdsByKeys(keys);

                for(VariantsAndCallDto c : calls){
                    VariantKey key = c.getVariantDto().key();
                    Integer variantId = idsByKey.get(key);
                    c.getSampleVariantCall().setVariantId(variantId);
                }
                List<SampleVariantCallDto> variantCalls =
                        calls.stream()
                                .map(VariantsAndCallDto::getSampleVariantCall)
                                .toList();

                extractVariantsService.upsertVariantsCallsBatch(variantCalls);


            }

        };
    }
//    @Bean(name = "extractVariantsWriter")
//    @StepScope
//    public ItemWriter<VariantSampleWrapper> extractVariantsWriter(){
//
//        return chunk -> {
//            for (VariantSampleWrapper item : chunk) {
//                for(VariantsAndCallDto call : item.getVariantsAndCalls()){
//                   Integer variantId = extractVariantsService.upsertVariant(call.getVariantDto());
//                   call.getSampleVariantCall().setVariantId(variantId);
//                    extractVariantsService.upsertSampleVariantCall(call.getSampleVariantCall());
//
//                }
//            sampleService.updateSampleStatusAndErrorCode(SampleStatus.READY_TO_ANNOTATE, Optional.empty(), item.getSampleId());
//            }
//
//
//        };
//    }

    private ItemWriter<SampleDto> updateStatusWriter() {
        return chunk -> {
            for (SampleDto item : chunk) {
                sampleService.updateSampleStatusAndErrorCode(item.getStatus(), Optional.empty(), item.getSampleId());
            }
        };
    }
}
