package net.marcwilmot.GenoPharma.job.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import io.whiteking.GenoPharma.dto.*;
import net.marcwilmot.GenoPharma.dto.*;
import net.marcwilmot.GenoPharma.infra.GeneRangeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.*;

@Component
@StepScope
public class ExtractVariantsProcessor implements ItemProcessor<SampleDto, VariantSampleWrapper> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    //private final List<PgxGeneRangeDto> genesRanges;
    private final GeneRangeCache geneRangeCache;

    public ExtractVariantsProcessor(GeneRangeCache geneRangeCache) {
        //this.genesRanges = genesRanges;
        this.geneRangeCache = geneRangeCache;
    }


    @Override
    public VariantSampleWrapper process(SampleDto item) throws Exception {

        VariantSampleWrapper wrapper = new VariantSampleWrapper();
        wrapper.setSampleId(item.getSampleId());
        wrapper.setVariantsAndCalls(new ArrayList<>());

        try(VCFFileReader reader = new VCFFileReader(new File(item.getAbsolutePath()), true) ){

            for(PgxGeneRangeDto geneRange : geneRangeCache.get()){
                LOG.info("Analizing the gene: {}",geneRange.getGeneName());
                try (CloseableIterator<VariantContext> it = reader.query(geneRange.getChromosome(), geneRange.getGrch37Start(), geneRange.getGrch37End())) {

                    while (it.hasNext()) {
                        VariantContext vc = it.next();
                        Genotype gt = vc.getGenotype(0);

                        String gtString = gt.getGenotypeString();

                        String zygosity = gt.getType().name();

                        Integer dp = gt.hasDP() ? gt.getDP() : null;
                        Integer gq = gt.hasGQ() ? gt.getGQ() : null;

                        Double qual = Double.isNaN(vc.getPhredScaledQual()) ? null : vc.getPhredScaledQual();
                        String filter = vc.isFiltered() ? String.join(";", vc.getFilters())  : "PASS";

                        String chr = vc.getContig();
                        Integer pos = vc.getStart();
                        String ref = vc.getReference().toString();
                        String genomeReference = item.getReferenceGenome();

                        for(Allele alt : vc.getAlternateAlleles()){
                            String altStr = alt.getBaseString();

                            VariantDto variantDto = VariantDto.builder()
                                    .variantId(null)
                                    .genomeBuild(item.getReferenceGenome())
                                    .chromosome(chr)
                                    .position(pos)
                                    .ref(ref)
                                    .alt(altStr)
                                    .variantType(vc.getType() != null ? vc.getType().name() : null)
                                    .rsid(vc.hasID() ? vc.getID() : null)
                                    .createdAt(Instant.now())
                                    .build();

                            String infoJson = buildInfoJson(vc, gt);

                            SampleVariantCallDto callDto = SampleVariantCallDto.builder()
                                    .sampleVariantCallId(null)
                                    .sampleId(item.getSampleId())
                                    .variantId(null)
                                    .genotype(gtString)
                                    .zygosity(zygosity)
                                    .genotypeQuality(gq)
                                    .depth(dp)
                                    .variantQuality(qual)
                                    .filter(filter)
                                    .infoJson(infoJson)
                                    .createdAt(Instant.now())
                                    .build();

                            VariantsAndCallDto VaC = new VariantsAndCallDto(variantDto, callDto);

                            wrapper.getVariantsAndCalls().add(VaC);
                        }

                    }
                }
        }

        return wrapper;
    }
}

public static String buildInfoJson(VariantContext vc, Genotype gt){
        Map<String, Object> root = new HashMap<>();
        Map<String, Object> info = new HashMap<>();

        info = sanitizeMap(vc.getAttributes());

        if(!info.isEmpty())
            root.put("info", info);

        Map<String, Object> format = new HashMap<>();

        if(gt.hasAD())
            format.put("AD", gt.getAD());
        if(gt.hasPL())
            format.put("PL", gt.getPL());

        Object ps = gt.getExtendedAttribute("PS");

        if(ps != null)
            format.put("PS", ps);

        format.put("extended", sanitizeMap(gt.getExtendedAttributes()));

        if(!format.isEmpty())
            root.put("format", format);
        try{
            return root.isEmpty() ? null : OBJECT_MAPPER.writeValueAsString(root);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing infoJson", e);
        }

}

    public static Map<String, Object> sanitizeMap(Map<String, Object> input) {
            Map<String, Object> output = new LinkedHashMap<>();

            if(input == null || input.isEmpty()) return output;

            for(Map.Entry<String, Object> e : input.entrySet()){
                String key = e.getKey();
                Object value = e.getValue();
                if (value == null) continue;

                output.put(key, sanitizeValue(value));
            }

            return output;

        }

    public static Object sanitizeValue(Object v){

        if(v == null) return null;

        if( v instanceof int[] || v instanceof long[] || v instanceof  double[] || v instanceof  boolean[])
            return v;
        if( v instanceof  String || v instanceof Number || v instanceof Boolean)
            return v;

        if(v instanceof  Map<?,?> map){
            Map<String, Object> nested = new HashMap<>();

        for(Map.Entry<?, ?> me : map.entrySet()){
            nested.put(String.valueOf(me.getKey()), sanitizeValue(me.getValue()));
        }
        return nested;
        }

        if(v instanceof Iterable<?> it){
            List<Object> list = new ArrayList<>();
            for(Object o : it )
                list.add(sanitizeValue(o));
            return list;
        }

        return String.valueOf(v);
    }
}
