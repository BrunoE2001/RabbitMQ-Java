package org.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MainTest {

    @Test
    public void testNullConnectionFactory() {
        ConnectionFactory nullConnectionFactory = null;
        String message = "Hello world!";

        assertThrows(NullPointerException.class, () -> {
            try (
                    Connection connection = nullConnectionFactory.newConnection();
                    Channel channel = connection.createChannel();
            ) {
                channel.queueDeclare("primera-cola", false, false, false, null);
                channel.basicPublish("", "primera-cola", null, message.getBytes());
            }
        });
    }
}