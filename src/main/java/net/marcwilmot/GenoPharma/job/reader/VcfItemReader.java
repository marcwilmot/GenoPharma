package net.marcwilmot.GenoPharma.job.reader;


import net.marcwilmot.GenoPharma.dto.VcfFileDescriptorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.List;

@Component
@StepScope
public class VcfItemReader implements ItemStreamReader<VcfFileDescriptorDto> {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Path inputDirectory;
    private Iterator<Path> fileIterator;
    private int currentIndex = 0;

    public VcfItemReader(@Value("${vcf.inputDirectory}") Path inputDirectory) {
        this.inputDirectory = inputDirectory;
    }


    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

        if(executionContext.containsKey("fileIndex"))
            this.currentIndex = executionContext.getInt("fileIndex");

        try (var stream = Files.list(inputDirectory)){
            List<Path> files = stream
                    .filter(path -> path.getFileName().toString().endsWith(".vcf.gz"))
                    .sorted()
                    .toList();
            this.fileIterator = files.subList(currentIndex, files.size()).iterator();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public VcfFileDescriptorDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if(fileIterator == null || !fileIterator.hasNext())
            return null;

        Path path = fileIterator.next();
        currentIndex++;

        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

        return VcfFileDescriptorDto.builder()
                .absolutePath(path.toAbsolutePath().toString())
                .fileName(path.getFileName().toString())
                .sizeBytes(attrs.size())
                .lastModifiedTime(attrs.lastModifiedTime().toInstant())
                .build();
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putInt("fileIndex", currentIndex);
    }

    @Override
    public void close() throws ItemStreamException {
        ItemStreamReader.super.close();
    }

}
