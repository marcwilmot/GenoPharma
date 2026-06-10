package net.marcwilmot.GenoPharma.constant;

import java.util.Objects;

public enum SampleStatus {
    READY_TO_EXTRACT(1),
    READY_TO_ANNOTATE(2),
    ANNOTATED(3),
    INTERPRETED(4),
    REPORTED(5),
    FAILED(99);

    private final Integer code;

    SampleStatus(Integer code){
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }

    public static SampleStatus getSampleStatus(Integer code){

        for (SampleStatus  s : values())
            if (Objects.equals(s.code, code))
                return s;
        throw new IllegalArgumentException("Unknown SampleStatus code: " + code);
    }
}
