package net.marcwilmot.GenoPharma.dto;

public class PgxGeneRangeDto {

    private final String geneName;
    private final String chromosome;
    private final Integer grch37Start;
    private final Integer grch37End;
    private final Integer grch38Start;
    private final Integer grch38End;
    private final Character strand;

    public static Builder builder() {
        return new Builder();
    }

    private PgxGeneRangeDto(Builder builder) {
        this.geneName = builder.geneName;
        this.chromosome = builder.chromosome;
        this.grch37Start = builder.grch37Start;
        this.grch37End = builder.grch37End;
        this.grch38Start = builder.grch38Start;
        this.grch38End = builder.grch38End;
        this.strand = builder.strand;
    }

    public static class Builder {
        private String geneName;
        private String chromosome;
        private Integer grch37Start;
        private Integer grch37End;
        private Integer grch38Start;
        private Integer grch38End;
        private Character strand;

        private Builder() {}

        public Builder geneName(String geneName) {
            this.geneName = geneName;
            return this;
        }

        public Builder chromosome(String chromosome) {
            this.chromosome = chromosome;
            return this;
        }

        public Builder grch37Start(Integer grch37Start) {
            this.grch37Start = grch37Start;
            return this;
        }

        public Builder grch37End(Integer grch37End) {
            this.grch37End = grch37End;
            return this;
        }

        public Builder grch38Start(Integer grch38Start) {
            this.grch38Start = grch38Start;
            return this;
        }

        public Builder grch38End(Integer grch38End) {
            this.grch38End = grch38End;
            return this;
        }

        public Builder strand(Character strand) {
            this.strand = strand;
            return this;
        }

        public PgxGeneRangeDto build() {
            return new PgxGeneRangeDto(this);
        }
    }

    public String getGeneName() {
        return geneName;
    }

    public String getChromosome() {
        return chromosome;
    }

    public Integer getGrch37Start() {
        return grch37Start;
    }

    public Integer getGrch37End() {
        return grch37End;
    }

    public Integer getGrch38Start() {
        return grch38Start;
    }

    public Integer getGrch38End() {
        return grch38End;
    }

    public Character getStrand() {
        return strand;
    }
}
