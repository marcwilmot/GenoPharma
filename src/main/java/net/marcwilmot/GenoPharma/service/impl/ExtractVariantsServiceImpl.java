package net.marcwilmot.GenoPharma.service.impl;

import net.marcwilmot.GenoPharma.dao.interfaces.ExtractVariantsDao;
import net.marcwilmot.GenoPharma.dto.SampleVariantCallDto;
import net.marcwilmot.GenoPharma.dto.VariantDto;
import net.marcwilmot.GenoPharma.dto.VariantKey;
import net.marcwilmot.GenoPharma.service.interfaces.ExtractVariantsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExtractVariantsServiceImpl implements ExtractVariantsService {

    private final ExtractVariantsDao extractVariantsDao;

    public ExtractVariantsServiceImpl(ExtractVariantsDao extractVariantsDao) {
        this.extractVariantsDao = extractVariantsDao;
    }

    @Override
    public Integer upsertVariant(VariantDto variantDto) {
        return extractVariantsDao.upsertVariant(variantDto) ;
    }

    @Override
    public void upsertVariantsBatch(List<VariantDto> variantsToUpsert) {
        extractVariantsDao.upsertVariantsBatch(variantsToUpsert);
    }

    @Override
    public void upsertVariantsCallsBatch(List<SampleVariantCallDto> calls) {

        extractVariantsDao.upsertVariantsCallsBatch(calls);
    }

    @Override
    public Map<VariantKey, Integer> fetchVariantIdsByKeys(List<VariantKey> keys) {
        final int KEY_BATCH = 500;

        Map<VariantKey, Integer> map = new HashMap<>(keys.size() * 2);

        for(int i = 0; i < keys.size(); i += KEY_BATCH ){
            List<VariantKey> sub = keys.subList(i, Math.min(i + KEY_BATCH, keys.size()));

            map.putAll(extractVariantsDao.fetchVariantIdsByKeys(sub));
        }
        return map;
    }


    @Override
    public void upsertSampleVariantCall(SampleVariantCallDto sampleVariantCallDto) {
        extractVariantsDao.upsertSampleVariantCall(sampleVariantCallDto);

    }
}
