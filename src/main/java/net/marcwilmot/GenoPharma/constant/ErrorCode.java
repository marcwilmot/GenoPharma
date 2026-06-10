package net.marcwilmot.GenoPharma.constant;

import java.util.Objects;

public enum ErrorCode {

    FILE_EMPTY(1),
    FILE_ERROR(2),
    DUPLICATE_VCF(3),
    VCF_HEADER_ERROR(4),
    UNKNOWN_GENOME_REFERENCE(5),
    NO_GENOTYPING_DATA(6),
    UNEXPECTED_SAMPLE_COUNT(7),
    MISSING_GT(8),
    NO_INDEX(9);

    private final Integer code;

    ErrorCode(Integer code) {
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }

    public static ErrorCode getErrorCode (Integer code){

        for (ErrorCode  s : values())
            if (Objects.equals(s.code, code))
                return s;
        throw new IllegalArgumentException("Unknown ErrorCode code: " + code);
    }
}
