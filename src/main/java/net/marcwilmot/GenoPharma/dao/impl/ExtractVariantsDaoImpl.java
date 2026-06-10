package net.marcwilmot.GenoPharma.dao.impl;

import net.marcwilmot.GenoPharma.constant.SqlDmlConstants;
import net.marcwilmot.GenoPharma.dao.interfaces.ExtractVariantsDao;
import net.marcwilmot.GenoPharma.dto.SampleVariantCallDto;
import net.marcwilmot.GenoPharma.dto.VariantDto;
import net.marcwilmot.GenoPharma.dto.VariantKey;
import net.marcwilmot.GenoPharma.infra.SqlScriptHelper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ExtractVariantsDaoImpl implements ExtractVariantsDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final SqlScriptHelper sqlScriptHelper;

    public ExtractVariantsDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate, SqlScriptHelper sqlScriptHelper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.sqlScriptHelper = sqlScriptHelper;
    }


    @Override
    public Integer upsertVariant(VariantDto variantDto) {

        final MapSqlParameterSource queryParams = new MapSqlParameterSource();


        queryParams.addValue("build", variantDto.getGenomeBuild());
        queryParams.addValue("chr", variantDto.getChromosome());
        queryParams.addValue("pos", variantDto.getPosition());
        queryParams.addValue("ref", variantDto.getRef());
        queryParams.addValue("alt", variantDto.getAlt());
        queryParams.addValue("type", variantDto.getVariantType());
        queryParams.addValue("rsid", variantDto.getRsid());
        queryParams.addValue("createdAt", Timestamp.from(variantDto.getCreatedAt()));

        return namedParameterJdbcTemplate.queryForObject(sqlScriptHelper.getSql(SqlDmlConstants.VARIANT_SQL_DML_UPSERT_VARIANT_RETURN_VARIANT_ID),queryParams, Integer.class);
    }

    @Override
    public void upsertVariantsBatch(List<VariantDto> variants) {
        MapSqlParameterSource[] batch = new MapSqlParameterSource[variants.size()];

        for(int i = 0; i < variants.size(); i++){
            VariantDto v = variants.get(i);

            batch[i] = new MapSqlParameterSource()
                    .addValue("build", v.getGenomeBuild())
                    .addValue("chr", v.getChromosome())
                    .addValue("ref", v.getRef())
                    .addValue("pos", v.getPosition())
                    .addValue("alt", v.getAlt())
                    .addValue("type", v.getVariantType())
                    .addValue("rsid", v.getRsid())
                    .addValue("createdAt", Timestamp.from(v.getCreatedAt()));
        }
        namedParameterJdbcTemplate.batchUpdate(sqlScriptHelper.getSql(SqlDmlConstants.VARIANT_SQL_DML_UPSERT_VARIANT), batch);

    }

    @Override
    public void upsertVariantsCallsBatch(List<SampleVariantCallDto> calls) {

        MapSqlParameterSource[] batch = new MapSqlParameterSource[calls.size()];

        for(int i = 0; i < calls.size(); i++){
            SampleVariantCallDto call = calls.get(i);

            batch[i] = new MapSqlParameterSource()
                    .addValue("sampleId", call.getSampleId())
                    .addValue("variantId", call.getVariantId())
                    .addValue("genotype", call.getGenotype())
                    .addValue("zygosity", call.getZygosity())
                    .addValue("dp", call.getDepth())
                    .addValue("gq", call.getGenotypeQuality())
                    .addValue("qual", call.getVariantQuality())
                    .addValue("filter", call.getFilter())
                    .addValue("infoJson", call.getInfoJson())
                    .addValue("createdAt", Timestamp.from(call.getCreatedAt()));
        }
        namedParameterJdbcTemplate.batchUpdate(sqlScriptHelper.getSql(SqlDmlConstants.VARIANT_SQL_DML_UPSERT_VARIANT_SAMPLE_CALL), batch);
        
    }

    @Override
    public Map<VariantKey, Integer> fetchVariantIdsByKeys(List<VariantKey> keys) {
        if (keys == null || keys.isEmpty()) return Map.of();

        String template = sqlScriptHelper.getSql(SqlDmlConstants.VARIANT_SQL_DML_FETCH_IDS_BY_KEYS);

        StringBuilder values = new StringBuilder();
        //MapSqlParameterSource params = new MapSqlParameterSource();
        List<Object> params = new ArrayList<>(keys.size() * 5);

        for (int i = 0; i < keys.size(); i++) {
            if (i > 0) values.append(",\n    ");
            values.append("(?,?,?,?,?)");

            VariantKey k = keys.get(i);
            params.add(k.getGenomeBuild());
            params.add(k.getChromosome());
            params.add(k.getPosition());
            params.add(k.getRef());
            params.add(k.getAlt());
        }

        String sql = template.replace("{{VALUES}}", values.toString());


            return jdbcTemplate.query(sql, params.toArray(), rs -> {
                Map<VariantKey, Integer> out = new HashMap<>(keys.size() * 2);
                while(rs.next()){
                    VariantKey key = VariantKey.builder()
                            .genomeBuild(rs.getString("genome_build"))
                            .chromosome(rs.getString("chromosome"))
                            .position(rs.getInt("position"))
                            .ref(rs.getString("ref"))
                            .alt(rs.getString("alt"))
                            .build();
                    out.put(key, rs.getInt("variant_id"));
                }
                return out;

            });
    }

    @Override
    public void upsertSampleVariantCall(SampleVariantCallDto sampleVariantCallDto) {
        final MapSqlParameterSource queryParams = new MapSqlParameterSource();

        queryParams.addValue("sampleId", sampleVariantCallDto.getSampleId());
        queryParams.addValue("variantId", sampleVariantCallDto.getVariantId());
        queryParams.addValue("genotype", sampleVariantCallDto.getGenotype());
        queryParams.addValue("zygosity", sampleVariantCallDto.getZygosity());
        queryParams.addValue("dp", sampleVariantCallDto.getDepth());
        queryParams.addValue("gq", sampleVariantCallDto.getGenotypeQuality());
        queryParams.addValue("qual", sampleVariantCallDto.getVariantQuality());
        queryParams.addValue("filter", sampleVariantCallDto.getFilter());
        queryParams.addValue("infoJson", sampleVariantCallDto.getInfoJson());
        queryParams.addValue("createdAt", Timestamp.from(sampleVariantCallDto.getCreatedAt()));


        namedParameterJdbcTemplate.update(sqlScriptHelper.getSql(SqlDmlConstants.VARIANT_SQL_DML_UPSERT_VARIANT_SAMPLE_CALL), queryParams);
    }
}
