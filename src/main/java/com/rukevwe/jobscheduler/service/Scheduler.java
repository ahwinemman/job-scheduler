package com.rukevwe.jobscheduler.service;

import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.enums.Priority;
import com.rukevwe.jobscheduler.enums.Status;
import com.rukevwe.jobscheduler.enums.Trigger;
import com.rukevwe.jobscheduler.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class Scheduler implements ApplicationListener<ApplicationReadyEvent> {

    private static Lock pickReadyJobsLock = new ReentrantLock();
    private ExecutorService executorService;
    private JmsTemplate jmsTemplate;

    private JobRepository jobRepository;

    @Value("${schedule.retryFailedJobs}")
    private boolean retryFailedJobs;

    @Value("${ready-to-retry.queue.name}")
    private String readyToRetryQueue;

    @Value("${ready-to-run.queue.name}")
    private String readyToRunQueue;

    @Autowired
    public Scheduler(JobRepository jobRepository, ExecutorService executorService, JmsTemplate jmsTemplate) {
        this.jobRepository = jobRepository;
        this.executorService = executorService;
        this.jmsTemplate = jmsTemplate;
    }

    @Scheduled(cron = "${schedule.pickReadyJobs}")
    public void pickReadyToRunJobs() {
        pickReadyJobsLock.lock();
        executorService.execute(new RetryFailedJobs());
        executorService.execute(new RunDueJobs());
        pickReadyJobsLock.unlock();
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        pickReadyToRunJobs();
    }

    public void queueImmediateJobs(Job job) {
        if (Trigger.ONE_TIME_TRIGGER == job.getTrigger()) {
            jmsTemplate.convertAndSend(readyToRunQueue, job);
        }
    }

    private class RetryFailedJobs implements Runnable {

        @Override
        public void run() {
            TreeMap<Priority, Job> sortedFailedJobs = new TreeMap<>();
            log.info("retry failed jobs");

            if (retryFailedJobs) {
                // get failed jobs
                List<Job> failedJobs = jobRepository.findByStatus(Status.FAILED);
                // sort based on priority
                for (Job job : failedJobs) {
                    sortedFailedJobs.put(job.getPriority(), job);
                }
            }

            for (Map.Entry<Priority, Job> failedJobMap : sortedFailedJobs.entrySet()) {
                Job failedJob = failedJobMap.getValue();
                failedJob.setStatus(Status.QUEUED);
                jobRepository.save(failedJob);
                jmsTemplate.convertAndSend(readyToRetryQueue, failedJob);
            }
        }
    }

    private class RunDueJobs implements Runnable {
        @Override
        public void run() {
            TreeMap<Priority, Job> sortedDueJobs = new TreeMap<>();

            log.info("running due jobs");
            List<Status> nonTerminalStatus = Arrays.asList(Status.QUEUED, Status.RUNNING);
            List<Job> dueJobs = jobRepository.findByNextStartTimeAfterAndStatusNotInOrLastStartTimeIsNull(new Date(), nonTerminalStatus);
            for (Job job : dueJobs) {
                sortedDueJobs.put(job.getPriority(), job);
            }
            for (Map.Entry<Priority, Job> dueJobMap : sortedDueJobs.entrySet()) {
                Job dueJob = dueJobMap.getValue();
                dueJob.setStatus(Status.QUEUED);
                jobRepository.save(dueJob);
                jmsTemplate.convertAndSend(readyToRunQueue, dueJob);
            }
        }
    }
}
