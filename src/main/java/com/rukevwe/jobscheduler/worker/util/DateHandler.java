package com.rukevwe.jobscheduler.worker.util;

import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.enums.Period;

import java.util.Date;

public class DateHandler {

    public static Date getNextStartTime(Job job) {

        Period jobPeriod = job.getPeriod();

        long delay;
        long nextStartTime;

        if (Period.HOURS == jobPeriod) {
            delay = 60 * 60 * job.getTimeLapse();
            nextStartTime = job.getLastEndTime().getTime() + delay * 1000;
        } else if (Period.MINUTES == jobPeriod) {
            delay = 60 * job.getTimeLapse();
            nextStartTime = job.getLastEndTime().getTime() + delay * 1000;
        } else if (Period.SECONDS == jobPeriod) {
            delay = job.getTimeLapse();
            nextStartTime = job.getLastEndTime().getTime() + delay * 1000;
        } else {
            delay = 60 * 60 * 24 * job.getTimeLapse();
            nextStartTime = job.getLastEndTime().getTime() + delay * 1000;
        }
        return new Date(nextStartTime);
    }
}
