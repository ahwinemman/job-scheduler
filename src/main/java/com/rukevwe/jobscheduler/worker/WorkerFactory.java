package com.rukevwe.jobscheduler.worker;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;


@Component
public class WorkerFactory {

    private EnumMap<JobType, IWorker> jobTypeIWorkerEnumMap = new EnumMap<>(JobType.class);

    public WorkerFactory(List<IWorker> workerList) {
        workerList.forEach(worker -> jobTypeIWorkerEnumMap.put(worker.getWorkerType(), worker));
    }

    public IWorker getWorker(JobType type) {
        if (jobTypeIWorkerEnumMap.containsKey(type)) {
            return jobTypeIWorkerEnumMap.get(type);
        }
        throw new UnsupportedOperationException();
    }
}
