package net.marcwilmot.GenoPharma.service.interfaces;

import net.marcwilmot.GenoPharma.dto.SampleVariantCallDto;
import net.marcwilmot.GenoPharma.dto.VariantDto;
import net.marcwilmot.GenoPharma.dto.VariantKey;

import java.util.List;
import java.util.Map;

public interface ExtractVariantsService {

    Integer upsertVariant(VariantDto variantDto);
    void upsertVariantsBatch(List<VariantDto> variants);
    void upsertVariantsCallsBatch(List<SampleVariantCallDto> calls);
    Map<VariantKey, Integer> fetchVariantIdsByKeys(List<VariantKey> keys);
    void upsertSampleVariantCall(SampleVariantCallDto sampleVariantCallDto);


}
