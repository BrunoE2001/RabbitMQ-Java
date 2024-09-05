package org.rabbitmq.consumidor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ConsumidorSimple {
    public static void main(String[] args) throws IOException, TimeoutException {
        // Abrir conexión AMQ
        ConnectionFactory cf = new ConnectionFactory();
        Connection connection = cf.newConnection();

        // Crear canal de comunicación
        Channel channel = connection.createChannel();
        // Crear subscripción a la cola "primera-cola
        channel.queueDeclare("primera-cola", false, false, false, null);
        // Crear subscripción a la cola "primera-cola" usando el comando Basic.consume
        channel.basicConsume(
                "primera-cola",
                true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Mensaje '" + messageBody + "'");
                    System.out.println(" [x] Exchange '" + message.getEnvelope().getExchange() + "'");
                    System.out.println(" [x] Routing key '" + message.getEnvelope().getRoutingKey() + "'");
                    System.out.println(" [x] Delivery tag '" + message.getEnvelope().getDeliveryTag() + "'");
                },
                consumerTag -> {
                    System.out.println("Consumidor '" + consumerTag + "' cancelado");
                }
                );
        channel.close();
        connection.close();
    }
}
