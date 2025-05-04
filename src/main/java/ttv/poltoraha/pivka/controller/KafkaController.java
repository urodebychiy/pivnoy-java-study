package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ttv.poltoraha.pivka.producer.KafkaProducer;

@RestController
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaProducer kafkaProducer;

    @PostMapping("/send")
    public void sendMessage(@RequestBody String message) {
        kafkaProducer.sendMessage("your-topic", message);
    }
}
