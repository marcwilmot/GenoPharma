package net.marcwilmot.GenoPharma.dto;

import java.util.Objects;

public final class VariantKey {

    private final String genomeBuild; // GRCh37 / GRCh38
    private final String chromosome;  // 1, chr1, X
    private final int position;       // 1-based
    private final String ref;          // A
    private final String alt;          // G

    private VariantKey(Builder builder) {
        this.genomeBuild = builder.genomeBuild;
        this.chromosome = builder.chromosome;
        this.position = builder.position;
        this.ref = builder.ref;
        this.alt = builder.alt;
    }

    public String getGenomeBuild() {
        return genomeBuild;
    }

    public String getChromosome() {
        return chromosome;
    }

    public int getPosition() {
        return position;
    }

    public String getRef() {
        return ref;
    }

    public String getAlt() {
        return alt;
    }

    /* ========= Builder ========= */

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String genomeBuild;
        private String chromosome;
        private int position;
        private String ref;
        private String alt;

        private Builder() {
        }

        public Builder genomeBuild(String genomeBuild) {
            this.genomeBuild = genomeBuild;
            return this;
        }

        public Builder chromosome(String chromosome) {
            this.chromosome = chromosome;
            return this;
        }

        public Builder position(int position) {
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

        public VariantKey build() {
            // Validaciones mínimas (puedes endurecerlas si quieres)
            Objects.requireNonNull(genomeBuild, "genomeBuild must not be null");
            Objects.requireNonNull(chromosome, "chromosome must not be null");
            Objects.requireNonNull(ref, "ref must not be null");
            Objects.requireNonNull(alt, "alt must not be null");

            if (position <= 0) {
                throw new IllegalStateException("position must be > 0");
            }

            return new VariantKey(this);
        }
    }

    /* ========= equals / hashCode ========= */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariantKey)) return false;
        VariantKey that = (VariantKey) o;
        return position == that.position &&
                genomeBuild.equals(that.genomeBuild) &&
                chromosome.equals(that.chromosome) &&
                ref.equals(that.ref) &&
                alt.equals(that.alt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genomeBuild, chromosome, position, ref, alt);
    }

    @Override
    public String toString() {
        return genomeBuild + ":" + chromosome + ":" + position + ":" + ref + ">" + alt;
    }
}
