package com.rukevwe.jobscheduler.worker;

import com.rukevwe.jobscheduler.data.Job;
import com.rukevwe.jobscheduler.enums.Trigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class FileWorker extends IWorker {

    @Override
    public void executeJob(Job job) {
        String fileName = Trigger.ONE_TIME_TRIGGER == job.getTrigger() ? "./file-one-time.txt" : "./file-cron-time.txt";

        Path filePath = Paths.get(fileName);
        try {
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);

            try (FileWriter writer = new FileWriter(fileName, true)) {
                writer.write(job.getData());
            }
        } catch (IOException e) {
            log.error("Error outputting to file");
            e.printStackTrace();
        }
    }

    @Override
    public JobType getWorkerType() {
        return JobType.FILE;
    }
}
