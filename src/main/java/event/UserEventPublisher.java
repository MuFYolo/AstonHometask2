package event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisher {
    private final KafkaTemplate<String, UserEvent> kafka;
    @Value("${app.kafka.topic.user-events}") // = user-events
    private String topic;

    public void send(UserEvent event) {
        kafka.send(topic, event.email(), event);
    }
}