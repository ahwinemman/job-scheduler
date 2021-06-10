package com.rukevwe.jobscheduler.event.model;

import com.rukevwe.jobscheduler.data.Job;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JobToBeQueuedEvent {

    private final Job job;
}
