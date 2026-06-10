package net.marcwilmot.GenoPharma.dao.interfaces;

import net.marcwilmot.GenoPharma.dto.PgxGeneRangeDto;

import java.util.List;

public interface GeneRangeDao {
    List<PgxGeneRangeDto> getAllPgxGeneRange();

}
