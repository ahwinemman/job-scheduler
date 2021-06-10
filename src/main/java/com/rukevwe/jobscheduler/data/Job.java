package com.rukevwe.jobscheduler.data;

import com.rukevwe.jobscheduler.enums.Period;
import com.rukevwe.jobscheduler.enums.Priority;
import com.rukevwe.jobscheduler.enums.Status;
import com.rukevwe.jobscheduler.enums.Trigger;
import com.rukevwe.jobscheduler.worker.JobType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(name = "job")
@Data
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date nextStartTime;

    private int failedCount;

    private JobType type;

    private Priority priority;

    private Trigger trigger;

    private Period period;

    private long timeLapse;

    private String data;

}
