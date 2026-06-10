package net.marcwilmot.GenoPharma.job.tasklet;

import net.marcwilmot.GenoPharma.service.interfaces.SFTPService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class DownloadVcfFilesTasklet implements Tasklet {

    private final SFTPService sftpService;

    public DownloadVcfFilesTasklet(SFTPService sftpService) {
        this.sftpService = sftpService;
    }


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        sftpService.downloadFiles();

        return RepeatStatus.FINISHED;
    }
}
