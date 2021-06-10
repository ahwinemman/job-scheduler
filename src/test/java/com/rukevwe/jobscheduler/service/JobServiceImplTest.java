package com.rukevwe.jobscheduler.service;

import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.enums.Period;
import com.rukevwe.jobscheduler.enums.Status;
import com.rukevwe.jobscheduler.enums.Trigger;
import com.rukevwe.jobscheduler.model.JobStatusResponse;
import com.rukevwe.jobscheduler.model.ScheduleRequest;
import com.rukevwe.jobscheduler.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Job Service Tests")
@ExtendWith(MockitoExtension.class)
public class JobServiceImplTest {

    @InjectMocks
    private JobServiceImpl jobService;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private Scheduler scheduler;

    private Job job;
    private ScheduleRequest scheduleRequest;

    @BeforeEach
    void setUp() {
        scheduleRequest = new ScheduleRequest();
        scheduleRequest.setPeriod(Period.HOURS);
        scheduleRequest.setTrigger(Trigger.ONE_TIME_TRIGGER);

        job = new Job();
        job.setPeriod(scheduleRequest.getPeriod());
        job.setTrigger(scheduleRequest.getTrigger());
        job.setStatus(Status.SUCCESS);
    }

    @Test
    public void saveJobShouldCallSchedulerQueueImmediateJobs() {

        when(jobRepository.save(any())).thenReturn(job);

        jobService.saveJob(scheduleRequest);

        verify(scheduler, times(1)).queueImmediateJobs(job);
    }

    @Test
    public void getJobStatusShouldReturnExpectedJobStatusResponse() {

        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(job));

        JobStatusResponse jobStatusResponse = jobService.getJobStatus(2);
        assertEquals(Status.SUCCESS, jobStatusResponse.getStatus());
    }
}
