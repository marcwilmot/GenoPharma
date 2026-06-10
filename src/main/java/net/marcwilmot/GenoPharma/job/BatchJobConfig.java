package net.marcwilmot.GenoPharma.job;

import net.marcwilmot.GenoPharma.dto.SampleDto;
import net.marcwilmot.GenoPharma.dto.VariantSampleWrapper;
import net.marcwilmot.GenoPharma.dto.VcfFileDescriptorDto;
import net.marcwilmot.GenoPharma.job.listener.JobGenoPharmaListener;
import net.marcwilmot.GenoPharma.job.processor.ExtractVariantsProcessor;
import net.marcwilmot.GenoPharma.job.processor.GenerateIndexProcessor;
import net.marcwilmot.GenoPharma.job.processor.VcfItemProcessor;
import net.marcwilmot.GenoPharma.job.reader.VcfItemReader;
import net.marcwilmot.GenoPharma.job.tasklet.DownloadVcfFilesTasklet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.invoke.MethodHandles;

@Configuration
public class BatchJobConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${batch.chunk.size:1}")
    private int chunkSize;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobGenoPharmaListener jobGenoPharmaListener;
    private final DownloadVcfFilesTasklet downloadVcfFilesTasklet;
    private final VcfItemReader vcfItemReader;
    private final VcfItemProcessor vcfItemProcessor;
    private final ItemWriter<SampleDto> vcfItemWriter;
    private final ItemReader<SampleDto> generateIndexReader;
    private final GenerateIndexProcessor generateIndexProcessor;
    private final ItemWriter<SampleDto> generateIndexWriter;
    private final ItemReader<SampleDto> extractVariantsReader;
    private final ExtractVariantsProcessor extractVariantProcessor;
    private final ItemWriter<VariantSampleWrapper> extractVariantWriter;


    public BatchJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, JobGenoPharmaListener jobGenoPharmaListener, DownloadVcfFilesTasklet downloadVcfFilesTasklet, VcfItemReader vcfItemReader, VcfItemProcessor vcfItemProcessor, @Qualifier("vcfItemWriter") ItemWriter<SampleDto> vcfItemWriter, @Qualifier("generateIndexReader") ItemReader<SampleDto> generateIndexReader, GenerateIndexProcessor generateIndexProcessor, @Qualifier("generateIndexWriter") ItemWriter<SampleDto> generateIndexWriter, @Qualifier("extractVariantsReader") ItemReader<SampleDto> extractVariantsReader, ExtractVariantsProcessor extractVariantProcessor, @Qualifier ("extractVariantsWriter") ItemWriter<VariantSampleWrapper> extractVariantWriter) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.jobGenoPharmaListener = jobGenoPharmaListener;
        this.downloadVcfFilesTasklet = downloadVcfFilesTasklet;
        this.vcfItemReader = vcfItemReader;
        this.vcfItemProcessor = vcfItemProcessor;
        this.vcfItemWriter = vcfItemWriter;
        this.generateIndexReader = generateIndexReader;
        this.generateIndexProcessor = generateIndexProcessor;
        this.generateIndexWriter = generateIndexWriter;
        this.extractVariantsReader = extractVariantsReader;
        this.extractVariantProcessor = extractVariantProcessor;
        this.extractVariantWriter = extractVariantWriter;
    }

    @Bean
    public Job GenoPharmaJob(){
        LOG.debug("Building GenoPharmaJob...");
        return new JobBuilder("GenoPharmaJob", jobRepository )
                .incrementer(new RunIdIncrementer())
                .listener(jobGenoPharmaListener)
                .flow(step01DownloadVcfFiles())
                .next(step02RegisterSampleStep())
                .next(step03GenerateIndexStep())
                .next(step04ExtractVariantsStep())
                .end()
                .build();
    }

    @Bean
    public Step step01DownloadVcfFiles() {

        LOG.info("Building step01DownloadVcfFiles...");
        return new StepBuilder("step01DownloadVcfFiles", jobRepository)
                .tasklet(downloadVcfFilesTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step step02RegisterSampleStep(){
        LOG.info("Building step02RegistreSampleStep...");
        return new StepBuilder("step02RegisterSampleStep", jobRepository)
                .<VcfFileDescriptorDto, SampleDto> chunk(chunkSize, transactionManager)
                .reader(vcfItemReader)
                .processor(vcfItemProcessor)
                .writer(vcfItemWriter)
                .build();
    }

    @Bean
    public Step step03GenerateIndexStep(){
        LOG.info("Building step03GenerateIndexStep...");

        return new StepBuilder("step03GenerateIndexStep", jobRepository)
                .<SampleDto, SampleDto> chunk(chunkSize, transactionManager)
                .reader(generateIndexReader)
                .processor(generateIndexProcessor)
                .writer(generateIndexWriter)
                .build();

    }

    @Bean
    public Step step04ExtractVariantsStep(){
        LOG.info("Building step04ExtractVariantsStep...");

        return new StepBuilder("step04ExtractVariantsStep", jobRepository)
                .<SampleDto, VariantSampleWrapper> chunk(chunkSize, transactionManager)
                .reader(extractVariantsReader)
                .processor(extractVariantProcessor)
                .writer(extractVariantWriter)
                .build();
    }
}
