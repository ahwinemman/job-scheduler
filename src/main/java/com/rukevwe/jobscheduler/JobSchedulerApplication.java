package com.rukevwe.jobscheduler;

import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.enums.Period;
import com.rukevwe.jobscheduler.enums.Priority;
import com.rukevwe.jobscheduler.enums.Trigger;
import com.rukevwe.jobscheduler.repository.JobRepository;
import com.rukevwe.jobscheduler.worker.JobType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJms
@SpringBootApplication
public class JobSchedulerApplication implements CommandLineRunner {

    @Autowired
    private JobRepository jobRepository;

    public static void main(String[] args) {
        SpringApplication.run(JobSchedulerApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        //Test 2
        Job job2 = new Job();
        job2.setData("Test Cron time File generation");
        job2.setType(JobType.FILE);
        job2.setPriority(Priority.HIGH);
        job2.setTrigger(Trigger.CRON_TRIGGER);
        job2.setTimeLapse(2);
        job2.setPeriod(Period.MINUTES);

        jobRepository.save(job2);

        // Test 1
        Job job1 = new Job();
        job1.setData("Test One time File generation");
        job1.setType(JobType.FILE);
        job1.setPriority(Priority.MEDIUM);
        job1.setTrigger(Trigger.ONE_TIME_TRIGGER);

        jobRepository.save(job1);


        System.out.println("saved job");
    }
}
