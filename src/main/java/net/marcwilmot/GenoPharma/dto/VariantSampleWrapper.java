package net.marcwilmot.GenoPharma.dto;

import java.util.List;

public class VariantSampleWrapper {

    private Long sampleId;
    private List<VariantsAndCallDto> variantsAndCalls;

    public VariantSampleWrapper() {
    }

    public VariantSampleWrapper(Long sampleId,
                                List<VariantsAndCallDto> variantsAndCalls) {
        this.sampleId = sampleId;
        this.variantsAndCalls = variantsAndCalls;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public List<VariantsAndCallDto> getVariantsAndCalls() {
        return variantsAndCalls;
    }

    public void setVariantsAndCalls(List<VariantsAndCallDto> variantsAndCalls) {
        this.variantsAndCalls = variantsAndCalls;
    }
}
