package net.marcwilmot.GenoPharma.infra;


import net.marcwilmot.GenoPharma.dto.PgxGeneRangeDto;
import net.marcwilmot.GenoPharma.service.interfaces.GeneRangeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneRangeCache {

    private final GeneRangeService geneRangeService;
    private volatile List<PgxGeneRangeDto> cached;

    public GeneRangeCache(GeneRangeService geneRangeService) {
        this.geneRangeService = geneRangeService;
    }

    public List<PgxGeneRangeDto> get() {
        List<PgxGeneRangeDto> local = cached;

        if(local == null){
            synchronized (this){
                local = cached;
                if(local == null){
                    local = geneRangeService.getAllPgxGeneRange();
                    cached = local;
                }
            }
        }
        return local;
    }

    public void clear() {
        cached = null;
    }
}
