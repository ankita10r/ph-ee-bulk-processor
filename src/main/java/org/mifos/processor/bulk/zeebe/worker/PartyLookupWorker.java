package org.mifos.processor.bulk.zeebe.worker;

import static org.mifos.processor.bulk.zeebe.ZeebeVariables.PARTY_LOOKUP_FAILED;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class PartyLookupWorker extends BaseWorker {

    private static final int TIMEOUT_SECONDS = 3;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void setup() {
        newWorker(Worker.PARTY_LOOKUP, (client, job) -> {
            long jobKey = job.getKey();
            logger.debug("Job '{}' started from process '{}' with key {}", job.getType(), job.getBpmnProcessId(), jobKey);
            Map<String, Object> variables = job.getVariablesAsMap();
            ScheduledFuture<?> timeoutHandler = scheduler.schedule(() -> {
                logger.error("Job '{}' with key {} is stuck.", job.getType(), jobKey);
                logger.info("Job stuck terminating ----------");
            }, 3, TimeUnit.SECONDS);

            try {
                if (workerConfig.isPartyLookUpWorkerEnabled) {
                    variables.put(PARTY_LOOKUP_FAILED, false);
                }
                client.newCompleteCommand(job.getKey()).variables(variables).send().join();
                timeoutHandler.cancel(false);  // Cancel the timeout if the job completes successfully
                logger.info("Job '{}' with key {} completed successfully.", job.getType(), jobKey);
            } catch (Exception e) {
                timeoutHandler.cancel(false);  // Cancel the timeout on error as well
                logger.error("Error processing job '{}': {}", job.getType(), e.getMessage(), e);
            }
        });
    }
}
