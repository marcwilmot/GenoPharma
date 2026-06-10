package net.marcwilmot.GenoPharma.dto;

import java.time.Instant;

public class VariantDto {
    private final Long variantId;

    private final String genomeBuild;   // GRCh37
    private final String chromosome;    // 13
    private final Integer position;

    private final String ref;
    private final String alt;

    private final String variantType;   // SNP, INDEL
    private final String rsid;
    private final Instant createdAt;


    public static Builder builder() {
        return new Builder();
    }

    private VariantDto(Builder builder) {
        this.variantId = builder.variantId;
        this.genomeBuild = builder.genomeBuild;
        this.chromosome = builder.chromosome;
        this.position = builder.position;
        this.ref = builder.ref;
        this.alt = builder.alt;
        this.variantType = builder.variantType;
        this.rsid = builder.rsid;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {

        private Long variantId;
        private String genomeBuild;
        private String chromosome;
        private Integer position;
        private String ref;
        private String alt;
        private String variantType;
        private String rsid;
        private Instant createdAt;


        private Builder() {}

        public Builder variantId(Long variantId) {
            this.variantId = variantId;
            return this;
        }

        public Builder genomeBuild(String genomeBuild) {
            this.genomeBuild = genomeBuild;
            return this;
        }

        public Builder chromosome(String chromosome) {
            this.chromosome = chromosome;
            return this;
        }

        public Builder position(Integer position) {
            this.position = position;
            return this;
        }

        public Builder ref(String ref) {
            this.ref = ref;
            return this;
        }

        public Builder alt(String alt) {
            this.alt = alt;
            return this;
        }

        public Builder variantType(String variantType) {
            this.variantType = variantType;
            return this;
        }

        public Builder rsid(String rsid) {
            this.rsid = rsid;
            return this;
        }

        public Builder createdAt (Instant createdAt){
            this.createdAt = createdAt;
            return this;
        }

        public VariantDto build() {
            return new VariantDto(this);
        }
    }

    public Long getVariantId() {
        return variantId;
    }

    public String getGenomeBuild() {
        return genomeBuild;
    }

    public String getChromosome() {
        return chromosome;
    }

    public Integer getPosition() {
        return position;
    }

    public String getRef() {
        return ref;
    }

    public String getAlt() {
        return alt;
    }

    public String getVariantType() {
        return variantType;
    }

    public String getRsid() {
        return rsid;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public VariantKey key(){
        return VariantKey.builder()
                .chromosome(chromosome)
                .position(position)
                .genomeBuild(genomeBuild)
                .ref(ref)
                .alt(alt)
                .build();
    }
}

