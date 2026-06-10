package net.marcwilmot.GenoPharma.dao.impl;

import net.marcwilmot.GenoPharma.constant.SqlQueryConstants;
import net.marcwilmot.GenoPharma.dao.interfaces.GeneRangeDao;
import net.marcwilmot.GenoPharma.dto.PgxGeneRangeDto;
import net.marcwilmot.GenoPharma.infra.SqlScriptHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Repository
public class GeneRangeDaoImpl implements GeneRangeDao {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private  final SqlScriptHelper sqlScriptHelper;

    public GeneRangeDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, SqlScriptHelper sqlScriptHelper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.sqlScriptHelper = sqlScriptHelper;
    }

    @Override
    public List<PgxGeneRangeDto> getAllPgxGeneRange() {

        final MapSqlParameterSource queryParams = new MapSqlParameterSource();

        try {
            return namedParameterJdbcTemplate.query(
                    sqlScriptHelper.getSql(SqlQueryConstants.GENE_SQL_QUERY_SELECT_ALL_GENE_RANGES),
                    queryParams,
                    (rs, rowNum) -> PgxGeneRangeDto.builder()
                            .geneName(rs.getString("gene_name"))
                            .chromosome(rs.getString("chromosome"))
                            .grch37Start(rs.getInt("grch37_start"))
                            .grch37End(rs.getInt("grch37_end"))
                            .grch38Start(rs.getInt("grch38_start"))
                            .grch38End(rs.getInt("grch38_end"))
                            .strand(rs.getString("strand").charAt(0))
                            .build()
            );        }catch (Exception e){

            LOG.error("ERROR EN ALLPGXGENERANGE", e);
            return List.of();
        }
    }
}
