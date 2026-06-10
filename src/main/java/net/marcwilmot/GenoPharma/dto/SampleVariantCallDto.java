package net.marcwilmot.GenoPharma.dto;

import java.time.Instant;

public class SampleVariantCallDto {

    private final Long sampleVariantCallId;

    private final Long sampleId;
    private Integer variantId;

    private final String genotype;          // "0/2"
    private final String zygosity;          // HET, HOM_VAR, etc

    private final Integer depth;            // DP
    private final Integer genotypeQuality;  // GQ

    private final Double variantQuality;    // QUAL
    private final String filter;            // PASS

    private final String infoJson;           // JSON como String
    private final Instant createdAt;


    public static Builder builder() {
        return new Builder();
    }

    private SampleVariantCallDto(Builder builder) {
        this.sampleVariantCallId = builder.sampleVariantCallId;
        this.sampleId = builder.sampleId;
        this.variantId = builder.variantId;
        this.genotype = builder.genotype;
        this.zygosity = builder.zygosity;
        this.depth = builder.depth;
        this.genotypeQuality = builder.genotypeQuality;
        this.variantQuality = builder.variantQuality;
        this.filter = builder.filter;
        this.infoJson = builder.infoJson;
        this.createdAt = builder.createdAt;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public static class Builder {

        private Long sampleVariantCallId;
        private Long sampleId;
        private Integer variantId;
        private String genotype;
        private String zygosity;
        private Integer depth;
        private Integer genotypeQuality;
        private Double variantQuality;
        private String filter;
        private String infoJson;
        private Instant createdAt;


        private Builder() {}

        public Builder sampleVariantCallId(Long sampleVariantCallId) {
            this.sampleVariantCallId = sampleVariantCallId;
            return this;
        }

        public Builder sampleId(Long sampleId) {
            this.sampleId = sampleId;
            return this;
        }

        public Builder variantId(Integer variantId) {
            this.variantId = variantId;
            return this;
        }

        public Builder genotype(String genotype) {
            this.genotype = genotype;
            return this;
        }

        public Builder zygosity(String zygosity) {
            this.zygosity = zygosity;
            return this;
        }

        public Builder depth(Integer depth) {
            this.depth = depth;
            return this;
        }

        public Builder genotypeQuality(Integer genotypeQuality) {
            this.genotypeQuality = genotypeQuality;
            return this;
        }

        public Builder variantQuality(Double variantQuality) {
            this.variantQuality = variantQuality;
            return this;
        }

        public Builder filter(String filter) {
            this.filter = filter;
            return this;
        }

        public Builder infoJson(String infoJson) {
            this.infoJson = infoJson;
            return this;
        }

        public Builder createdAt (Instant createdAt){
            this.createdAt = createdAt;
            return this;
        }

        public SampleVariantCallDto build() {
            return new SampleVariantCallDto(this);
        }
    }

    public Long getSampleVariantCallId() {
        return sampleVariantCallId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public String getGenotype() {
        return genotype;
    }

    public String getZygosity() {
        return zygosity;
    }

    public Integer getDepth() {
        return depth;
    }

    public Integer getGenotypeQuality() {
        return genotypeQuality;
    }

    public Double getVariantQuality() {
        return variantQuality;
    }

    public String getFilter() {
        return filter;
    }

    public String getInfoJson() {
        return infoJson;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
