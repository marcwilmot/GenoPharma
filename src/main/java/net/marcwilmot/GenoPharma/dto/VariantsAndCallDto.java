package net.marcwilmot.GenoPharma.dto;

public class VariantsAndCallDto {

    private  VariantDto variantDto;
    private  SampleVariantCallDto sampleVariantCall;

    public VariantsAndCallDto(VariantDto variantDto, SampleVariantCallDto sampleVariantCall) {
        this.variantDto = variantDto;
        this.sampleVariantCall = sampleVariantCall;
    }

    public VariantDto getVariantDto() {
        return variantDto;
    }

    public void setVariantDto(VariantDto variantDto) {
        this.variantDto = variantDto;
    }

    public SampleVariantCallDto getSampleVariantCall() {
        return sampleVariantCall;
    }

    public void setSampleVariantCalls(SampleVariantCallDto sampleVariantCall) {
        this.sampleVariantCall = sampleVariantCall;
    }
}