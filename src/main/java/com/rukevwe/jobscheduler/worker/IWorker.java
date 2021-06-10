package com.rukevwe.jobscheduler.worker;

import com.rukevwe.jobscheduler.data.Job;

public abstract class IWorker {

    abstract void executeJob(Job job);

    abstract JobType getWorkerType();
}
