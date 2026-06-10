package net.marcwilmot.GenoPharma.service.impl;

import net.marcwilmot.GenoPharma.dao.interfaces.GeneRangeDao;
import net.marcwilmot.GenoPharma.dto.PgxGeneRangeDto;
import net.marcwilmot.GenoPharma.service.interfaces.GeneRangeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneRangeServiceImpl implements GeneRangeService {

    private final GeneRangeDao geneRangeDao;

    public GeneRangeServiceImpl(GeneRangeDao geneRangeDao) {
        this.geneRangeDao = geneRangeDao;
    }

    @Override
    public List<PgxGeneRangeDto> getAllPgxGeneRange() {
        return geneRangeDao.getAllPgxGeneRange();
    }
}
