package com.rukevwe.jobscheduler.service;

import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.model.JobStatusResponse;
import com.rukevwe.jobscheduler.model.ScheduleRequest;
import com.rukevwe.jobscheduler.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JobServiceImpl implements JobService {

    private JobRepository jobRepository;
    private Scheduler scheduler;

    @Value("${ready-to-run.queue.name}")
    private String readyToRunQueue;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }


    // TODO: Paginate job list
    @Override
    public List<Job> getJobList() {
        List<Job> jobList = new ArrayList<>();
        jobRepository.findAll()
                .iterator()
                .forEachRemaining(jobList::add);
        return jobList;
    }

    @Override
    public JobStatusResponse getJobStatus(long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        JobStatusResponse jobStatusResponse = new JobStatusResponse();
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            jobStatusResponse.setJobId(id);
            jobStatusResponse.setStatus(job.getStatus());
            return jobStatusResponse;
        }
        return null;
    }


    @Override
    public void saveJob(ScheduleRequest scheduleRequest) {

        Job job = new Job();
        job.setPriority(scheduleRequest.getPriority());
        job.setType(scheduleRequest.getType());
        job.setData(scheduleRequest.getData());
        job.setPeriod(scheduleRequest.getPeriod());
        job.setTimeLapse(scheduleRequest.getTimeLapse());
        job.setTrigger(scheduleRequest.getTrigger());

        Job savedJob = jobRepository.save(job);

        scheduler.queueImmediateJobs(savedJob);
    }

}
