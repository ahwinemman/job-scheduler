package com.rukevwe.jobscheduler.configuration;

import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "schedule")
@Component
@Data
@Validated
public class ScheduleConfiguration {

    @NotNull
    private Integer threadPoolSize;
}
