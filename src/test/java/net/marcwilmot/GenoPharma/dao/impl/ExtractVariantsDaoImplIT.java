package net.marcwilmot.GenoPharma.dao.impl;

import net.marcwilmot.GenoPharma.constant.SampleStatus;
import net.marcwilmot.GenoPharma.dao.interfaces.ExtractVariantsDao;
import net.marcwilmot.GenoPharma.dao.interfaces.SampleDao;
import net.marcwilmot.GenoPharma.dto.SampleDto;
import net.marcwilmot.GenoPharma.dto.SampleVariantCallDto;
import net.marcwilmot.GenoPharma.dto.VariantDto;
import net.marcwilmot.GenoPharma.infra.SqlScriptHelper;
import net.marcwilmot.GenoPharma.testinfra.AbstractPostgresIT;
import net.marcwilmot.GenoPharma.testinfra.DbTestSchemaInit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;

@SpringBootTest
public class ExtractVariantsDaoImplIT extends AbstractPostgresIT {

    @Autowired ExtractVariantsDao dao;
    @Autowired JdbcTemplate jdbcTemplate;
    @Autowired SqlScriptHelper sqlScriptHelper;
    @Autowired SampleDao sampleDao;


    @BeforeEach
    void setUp() {
        DbTestSchemaInit.createGenoPharmaSchemas(jdbcTemplate, sqlScriptHelper);
        DbTestSchemaInit.truncateGenoPharmaTables(jdbcTemplate);
    }


    @Test
    public void upsertVariant_insertsVariantAndReturnsId(){
        VariantDto variantDto = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        Integer id = dao.upsertVariant(variantDto);
        assertThat(id).isNotNull();

        Integer count = jdbcTemplate.queryForObject("select count(*) from variant", Integer.class);
        assertThat(count).isEqualTo(1);

    }

    @Test
    public void upsertVariant_isIdempotent(){
        VariantDto variantDto = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        Integer id1 = dao.upsertVariant(variantDto);
        Integer id2 = dao.upsertVariant(variantDto);

        assertThat(id1).isNotNull();
        assertThat(id2).isNotNull();

        Integer count = jdbcTemplate.queryForObject("select count(*) from variant", Integer.class);
        assertThat(count).isEqualTo(1);

    }

    @Test
    public void upsertVariant_setsRsid_whenExistingRsidIsNull(){
        VariantDto variantWhitoutRsid = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid(null)
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        VariantDto variantWithRsid = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        Integer id = dao.upsertVariant(variantWhitoutRsid);
        Integer id2 = dao.upsertVariant(variantWithRsid);

        assertThat(id).isNotNull();
        assertThat(id2).isNotNull();

        Integer count = jdbcTemplate.queryForObject("select count(*) from variant", Integer.class);
        assertThat(count).isEqualTo(1);

        Integer c = jdbcTemplate.queryForObject(
                "select count(*) from variant where rsid = ?",
                Integer.class,
                variantWithRsid.getRsid()
        );

        assertThat(c).isEqualTo(1);

    }

    @Test
    public void upsertVariant_doesNotOverwriteExistingRsid(){
        VariantDto variantWithRsid = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        VariantDto variantWithRsid2 = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs124")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        Integer id = dao.upsertVariant(variantWithRsid);

        Integer id2 = dao.upsertVariant(variantWithRsid2);

        assertThat(id).isNotNull();
        assertThat(id2).isNotNull();

        Integer count = jdbcTemplate.queryForObject("select count(*) from variant", Integer.class);
        assertThat(count).isEqualTo(1);

        Integer c = jdbcTemplate.queryForObject(
                "select count(*) from variant where rsid = ?",
                Integer.class,
                variantWithRsid.getRsid()
        );

        assertThat(c).isEqualTo(1);

    }

    @Test
    public void upsertVariant_preservesOtherColumns_onConflict(){
        VariantDto variant= VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        VariantDto variant2 = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("INDEL")
                .rsid("rs124")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        Integer id = dao.upsertVariant(variant);

        Integer id2 = dao.upsertVariant(variant2);

        assertThat(id).isNotNull();
        assertThat(id2).isNotNull();

        Integer count = jdbcTemplate.queryForObject("select count(*) from variant", Integer.class);
        assertThat(count).isEqualTo(1);

        Integer c = jdbcTemplate.queryForObject(
                "select count(*) from variant where variant_type = ?",
                Integer.class,
                variant.getVariantType()
        );

        assertThat(c).isEqualTo(1);

    }

    @Test
    public void upsertVariantsBatch_insertsRows_whenNotExists(){
        VariantDto variantDto = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        dao.upsertVariantsBatch(List.of(variantDto));

        Integer count = jdbcTemplate.queryForObject("select count(*) from variant", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void upsertVariantsBatch_isIdempotent_forSamePayload(){
        VariantDto variantDto = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        dao.upsertVariantsBatch(List.of(variantDto));
        dao.upsertVariantsBatch(List.of(variantDto));

        Integer count = jdbcTemplate.queryForObject("select count(*) from variant", Integer.class);
        assertThat(count).isEqualTo(1);

    }

    @Test
    public void upsertVariantsBatch_setsRsid_whenExistingRsidIsNull() {
        VariantDto variantWhitoutRsid = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid(null)
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        VariantDto variantWithRsid = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        dao.upsertVariantsBatch(List.of(variantWhitoutRsid));
        dao.upsertVariantsBatch(List.of(variantWithRsid));

        Integer count = jdbcTemplate.queryForObject("select count(*) from variant", Integer.class);
        assertThat(count).isEqualTo(1);

        Integer c = jdbcTemplate.queryForObject(
                "select count(*) from variant where rsid = ?",
                Integer.class,
                variantWithRsid.getRsid()
        );

        assertThat(c).isEqualTo(1);
    }

    @Test
    public void upsertVariantsBatch_doesNotOverwriteExistingRsid() {
        VariantDto variantWithRsid = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        VariantDto variantWithRsid2 = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs124")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        dao.upsertVariantsBatch(List.of(variantWithRsid));
        dao.upsertVariantsBatch(List.of(variantWithRsid2));

        Integer count = jdbcTemplate.queryForObject("select count(*) from variant", Integer.class);
        assertThat(count).isEqualTo(1);

        Integer c = jdbcTemplate.queryForObject(
                "select count(*) from variant where rsid = ?",
                Integer.class,
                variantWithRsid.getRsid()
        );

        assertThat(c).isEqualTo(1);

    }

    @Test
    public void upsertVariantsBatch_preservesOtherColumns_onConflict() {

        VariantDto variant= VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        VariantDto variant2 = VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("INDEL")
                .rsid("rs124")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

        dao.upsertVariantsBatch(List.of(variant));
        dao.upsertVariantsBatch(List.of(variant2));

        Integer count = jdbcTemplate.queryForObject("select count(*) from variant", Integer.class);
        assertThat(count).isEqualTo(1);

        Integer c = jdbcTemplate.queryForObject(
                "select count(*) from variant where variant_type = ?",
                Integer.class,
                variant.getVariantType()
        );

        assertThat(c).isEqualTo(1);

    }


    @Test
    public void upsertVariantsCallsBatch_insertsRows_whenNotExists (){

        this.insertSampleAndVariant();

        SampleVariantCallDto call = createSampleVariantCallDto();

        dao.upsertVariantsCallsBatch(List.of(call));

        Integer count = jdbcTemplate.queryForObject("select count(*) from sample_variant_call where sample_id = ? and variant_id = ?",
                Integer.class, call.getSampleId(), call.getVariantId());

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void upsertVariantsCallsBatch_isIdempotent_forSamePayload (){

        this.insertSampleAndVariant();

        SampleVariantCallDto call = createSampleVariantCallDto();

        dao.upsertVariantsCallsBatch(List.of(call));
        dao.upsertVariantsCallsBatch(List.of(call));

        Integer count = jdbcTemplate.queryForObject("select count(*) from sample_variant_call where sample_id = ? and variant_id = ?",
                Integer.class, call.getSampleId(), call.getVariantId());

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void upsertVariantsCallsBatch_updatesColumns_onConflict (){

    }

    @Test
    public void upsertVariantsCallsBatch_preservesNonUpdatedColumns_onConflict (){

    }

    @Test
    public void upsertVariantsCallsBatch_updatesInfoJson_onConflict (){

    }

    @Test
    public void upsertVariantsCallsBatch_insertsNewAndUpdatesExisting_inOneBatch (){

    }



    private static VariantDto createVariantDto(){
        return VariantDto.builder()
                .genomeBuild("GRCh38")
                .chromosome("1")
                .position(123)
                .ref("A")
                .alt("T")
                .variantType("SNP")
                .rsid("rs123")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();

    }

    private static SampleDto createSample(){
        return SampleDto.builder()
                .hashCode("abc123hash")
                .status(SampleStatus.READY_TO_ANNOTATE)
                .errorCode(null)
                .fileName("sample.vcf")
                .absolutePath("/data/vcf/sample.vcf")
                .sizeBytes(1024L)
                .lastModifiedTime(Instant.parse("2026-03-05T10:00:00Z"))
                .referenceGenome("GRCh38")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();
    }

    private static SampleVariantCallDto createSampleVariantCallDto(){
        return  SampleVariantCallDto.builder()
                .sampleId(1L)
                .variantId(1)
                .genotype("0/1")
                .zygosity("HET")
                .depth(30)
                .genotypeQuality(99)
                .variantQuality(500.0)
                .filter("PASS")
                .infoJson("{\"DP\":30}")
                .createdAt(Instant.parse("2026-03-05T10:00:00Z"))
                .build();
    }

    private void insertSampleAndVariant(){
        sampleDao.insertSample(createSample());
        dao.upsertVariant(createVariantDto());

    }
}
