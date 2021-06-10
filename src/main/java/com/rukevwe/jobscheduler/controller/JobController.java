package com.rukevwe.jobscheduler.controller;

import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.model.JobStatusResponse;
import com.rukevwe.jobscheduler.model.ScheduleRequest;
import com.rukevwe.jobscheduler.service.JobService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/v1/jobs")
public class JobController {

    private final JobService jobService;

    @GetMapping
    public List<Job> getJobList() {
        return jobService.getJobList();
    }

    @GetMapping("{id}/status")
    public ResponseEntity<JobStatusResponse> getJobStatus(@PathVariable("id") long id) {

        JobStatusResponse jobStatusResponse =  jobService.getJobStatus(id);
        if (jobStatusResponse != null) {
            return new ResponseEntity<>(jobStatusResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public void saveJob(ScheduleRequest scheduleRequest) {
        jobService.saveJob(scheduleRequest);
    }

}
