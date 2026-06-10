package net.marcwilmot.GenoPharma.dto;

import java.time.Instant;

public class VcfFileDescriptorDto {
    private final String fileName;
    private final String absolutePath;
    private final Long sizeBytes;
    private final Instant lastModifiedTime;

    public static Builder builder(){
        return new VcfFileDescriptorDto.Builder();
    }

    private VcfFileDescriptorDto(Builder builder){
        this.fileName = builder.fileName;
        this.absolutePath = builder.absolutePath;
        this.sizeBytes = builder.sizeBytes;
        this.lastModifiedTime = builder.lastModifiedTime;
    }

    public static class Builder {
        private String fileName;
        private String absolutePath;
        private Long sizeBytes;
        private Instant lastModifiedTime;

        private Builder(){}
        public Builder fileName(String fileName){
            this.fileName = fileName;
            return this;
        }

        public Builder absolutePath(String absolutePath){
            this.absolutePath = absolutePath;
            return this;
        }
        public Builder sizeBytes(Long sizeBytes){
            this.sizeBytes = sizeBytes;
            return this;
        }
        public Builder lastModifiedTime(Instant lastModifiedTime){
            this.lastModifiedTime = lastModifiedTime;
            return this;
        }
        public VcfFileDescriptorDto build(){
            return new VcfFileDescriptorDto(this);
        }
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

}
