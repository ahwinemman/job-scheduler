package com.rukevwe.jobscheduler.model;

import com.rukevwe.jobscheduler.enums.Period;
import com.rukevwe.jobscheduler.enums.Priority;
import com.rukevwe.jobscheduler.enums.Trigger;
import com.rukevwe.jobscheduler.worker.JobType;
import lombok.Data;

@Data
public class ScheduleRequest {
    private Priority priority;
    private String data;
    private JobType type;
    private Trigger trigger;
    private Period period;
    private long timeLapse;
}
