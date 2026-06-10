package net.marcwilmot.GenoPharma.constant;

public enum GenomeReference {

    GRCh37("GRCh37"),
    GRCh38("GRCh38");
    //UNKNOWN("UNKNOWN");

    private final String genomeReference;

    GenomeReference(String genomeReference){this.genomeReference = genomeReference;}

    public String getGenomeReference(){
        return  genomeReference;
    }
}
