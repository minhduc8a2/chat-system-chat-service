package com.ducle.chat_service.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class DelayExecutorService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public void executeAfterDelay(Runnable task, long delayInSeconds) {
        scheduler.schedule(task, delayInSeconds, TimeUnit.SECONDS);
    }
}
