package net.marcwilmot.GenoPharma.dao.interfaces;


import net.marcwilmot.GenoPharma.dto.SampleVariantCallDto;
import net.marcwilmot.GenoPharma.dto.VariantDto;
import net.marcwilmot.GenoPharma.dto.VariantKey;

import java.util.List;
import java.util.Map;

public interface ExtractVariantsDao {

    Integer upsertVariant(VariantDto variantDto);
    void upsertVariantsBatch(List<VariantDto> variantsToUpsert );
    void upsertVariantsCallsBatch(List<SampleVariantCallDto> calls);

    Map<VariantKey, Integer> fetchVariantIdsByKeys(List<VariantKey> keys);

    void upsertSampleVariantCall(SampleVariantCallDto sampleVariantCallDto);
}
