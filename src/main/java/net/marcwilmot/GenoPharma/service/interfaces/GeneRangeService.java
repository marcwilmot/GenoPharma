package net.marcwilmot.GenoPharma.service.interfaces;


import net.marcwilmot.GenoPharma.dto.PgxGeneRangeDto;

import java.util.List;

public interface GeneRangeService {

    List<PgxGeneRangeDto> getAllPgxGeneRange();
}
