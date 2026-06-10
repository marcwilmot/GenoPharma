package net.marcwilmot.GenoPharma.dto;
import net.marcwilmot.GenoPharma.constant.ErrorCode;
import net.marcwilmot.GenoPharma.constant.SampleStatus;

import java.time.Instant;

public class SampleDto {

    private final Long sampleId;
    private final String hashCode;

    private final SampleStatus status;
    private final ErrorCode errorCode;

    private final String fileName;
    private final String absolutePath;
    private final Long sizeBytes;
    private final Instant lastModifiedTime;

    private final String vcfVersion;
    private final String referenceGenome;

    private final Instant createdAt;

    public static Builder builder() {
        return new Builder();
    }

    private SampleDto(Builder builder) {
        this.sampleId = builder.sampleId;
        this.hashCode = builder.hashCode;
        this.status = builder.status;
        this.errorCode = builder.errorCode;
        this.fileName = builder.fileName;
        this.absolutePath = builder.absolutePath;
        this.sizeBytes = builder.sizeBytes;
        this.lastModifiedTime = builder.lastModifiedTime;
        this.vcfVersion = builder.vcfVersion;
        this.referenceGenome = builder.referenceGenome;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {

        private Long sampleId;
        private String hashCode;

        private SampleStatus status;
        private ErrorCode errorCode;

        private String fileName;
        private String absolutePath;
        private Long sizeBytes;
        private Instant lastModifiedTime;

        private String vcfVersion;
        private String referenceGenome;

        private Instant createdAt;

        private Builder() {}

        public Builder sampleId(Long sampleId) {
            this.sampleId = sampleId;
            return this;
        }

        public Builder hashCode(String hashCode) {
            this.hashCode = hashCode;
            return this;
        }

        public Builder status(SampleStatus status) {
            this.status = status;
            return this;
        }

        public Builder errorCode(ErrorCode errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder absolutePath(String absolutePath) {
            this.absolutePath = absolutePath;
            return this;
        }

        public Builder sizeBytes(Long sizeBytes) {
            this.sizeBytes = sizeBytes;
            return this;
        }

        public Builder lastModifiedTime(Instant lastModifiedTime) {
            this.lastModifiedTime = lastModifiedTime;
            return this;
        }

        public Builder vcfVersion(String vcfVersion) {
            this.vcfVersion = vcfVersion;
            return this;
        }

        public Builder referenceGenome(String referenceGenome) {
            this.referenceGenome = referenceGenome;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public SampleDto build() {
            return new SampleDto(this);
        }
    }

    public Long getSampleId() {
        return sampleId;
    }

    public String getHashCode() {
        return hashCode;
    }

    public SampleStatus getStatus() {
        return status;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public String getVcfVersion() {
        return vcfVersion;
    }

    public String getReferenceGenome() {
        return referenceGenome;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
