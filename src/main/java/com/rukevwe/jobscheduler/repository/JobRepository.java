package com.rukevwe.jobscheduler.repository;

import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByStatus(Status status);

    List<Job> findByNextStartTimeAfterAndStatusNotInOrLastStartTimeIsNull(Date date, List<Status> statuses);

}
