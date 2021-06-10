package com.rukevwe.jobscheduler.worker;

import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.enums.Status;
import com.rukevwe.jobscheduler.enums.Trigger;
import com.rukevwe.jobscheduler.repository.JobRepository;
import com.rukevwe.jobscheduler.worker.util.DateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JobExecutor {

    @Autowired
    private WorkerFactory workerFactory;

    @Autowired
    private JobRepository jobRepository;

    @JmsListener(destination = "${ready-to-run.queue.name}")
    public void runDueJob(Job job) {
        execute(job);
    }

    @JmsListener(destination = "${ready-to-retry.queue.name}")
    public void runRetryJob(Job job) {
        execute(job);
    }

    private void execute(Job job) {
        job.setStatus(Status.RUNNING);
        job.setLastStartTime(new Date());

        IWorker worker = workerFactory.getWorker(job.getType());

        try {
            worker.executeJob(job);
            job.setLastEndTime(new Date());
            if (Trigger.ONE_TIME_TRIGGER != job.getTrigger()) {
                job.setNextStartTime(DateHandler.getNextStartTime(job));
            }
            job.setStatus(Status.SUCCESS);
        } catch (Exception ex) {
            job.setStatus(Status.FAILED);
            job.setFailedCount(job.getFailedCount() + 1);
            job.setNextStartTime(null);

            log.error("Error executing job with id {}, will attempt to retry", job.getId(), ex);
        }
        jobRepository.save(job);
    }
}
