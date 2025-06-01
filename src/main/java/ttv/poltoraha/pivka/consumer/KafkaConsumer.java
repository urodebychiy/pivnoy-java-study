package ttv.poltoraha.pivka.consumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Setter
@Getter
@RequiredArgsConstructor
public class KafkaConsumer {

    private static final long DELAY_BETWEEN_MESSAGES_MS = 1000 / 15;
    private static final long DB_SAVE_TIME_MS = 1000;

    @KafkaListener(topics = "your-topic", containerFactory = "kafkaListenerContainerFactory", groupId = "my-group")
    public void listen(String message, Acknowledgment acknowledgment) {

        long startTime = System.currentTimeMillis();

        try {
            saveToDatabase(message);
            acknowledgment.acknowledge();

            long processingTime = System.currentTimeMillis() - startTime;
            long remainingDelay = DELAY_BETWEEN_MESSAGES_MS - processingTime;

            if (remainingDelay > 0) {
                TimeUnit.MILLISECONDS.sleep(remainingDelay);
            }
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            throw new RuntimeException("Processing interrupted", e);

        } catch (Exception e) {

            System.err.println("Processing failed: " + e.getMessage());

        }
    }

    private void saveToDatabase(String message) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(DB_SAVE_TIME_MS);
        System.out.println("Saving to database: " + message);
    }
}
