package com.rukevwe.jobscheduler.service;

import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.model.JobStatusResponse;
import com.rukevwe.jobscheduler.model.ScheduleRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobService {

    List<Job> getJobList();

    JobStatusResponse getJobStatus(long id);

    void saveJob(ScheduleRequest scheduleRequest);
}
