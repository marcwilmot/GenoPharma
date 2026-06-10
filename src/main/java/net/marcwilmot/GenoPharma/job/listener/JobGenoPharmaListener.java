package net.marcwilmot.GenoPharma.job.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.Instant;

@Component
public class JobGenoPharmaListener implements JobExecutionListener {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public JobGenoPharmaListener(){}

    @Override
    public void beforeJob(JobExecution jobExecution){

        jobExecution.getExecutionContext().put("sampleDate", Instant.now());

        LOG.info("Starting job {} at {}", jobExecution.getJobInstance().getJobName(), jobExecution.getExecutionContext().get("sampleDate"));
    }
}
