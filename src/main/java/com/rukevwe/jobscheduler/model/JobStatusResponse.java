package com.rukevwe.jobscheduler.model;

import com.rukevwe.jobscheduler.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobStatusResponse {
    private long jobId;
    private Status status;
}
