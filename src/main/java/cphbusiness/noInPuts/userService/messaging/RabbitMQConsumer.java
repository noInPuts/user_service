package cphbusiness.noInPuts.userService.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // TODO: Implementation
        System.out.println("Consuming Message - " + new String(message.getBody()));
    }
}
