package com.bardlind.dragdrop.managed;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ManagedThreadPool implements Managed {

    private static final Logger log = LoggerFactory.getLogger(ManagedThreadPool.class);

    private ExecutorService executorService;

    @Override
    public void start() throws Exception {
        log.info("Starting managed thread-pool...");
        executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public void stop() throws Exception {
        log.info("Shutting down managed thread-pool...");
        executorService.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
                    log.warn("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        log.info("Successfully shut down managed thread-pool.");
    }
}
