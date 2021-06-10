package com.rukevwe.jobscheduler.event;


import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.enums.Status;
import com.rukevwe.jobscheduler.event.model.JobToBeQueuedEvent;
import com.rukevwe.jobscheduler.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobEventListener {

    private final JobRepository jobRepository;

    @EventListener
    public void handleJobQueued(JobToBeQueuedEvent jobToBeQueuedEvent) {
        Optional<Job> jobToBeQueuedOptional =  jobRepository.findById(jobToBeQueuedEvent.getJob().getId());

        jobToBeQueuedOptional.ifPresent(job -> {
            job.setStatus(Status.QUEUED);
            jobRepository.save(job);
        });
    }
}
