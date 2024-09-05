package org.rabbitmq.consumidor;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ConsumidorEventos {

    public static final String EVENTOS = "eventos";

    public static void main(String[] args) throws IOException, TimeoutException {

        // Abrir conexión AMQ
        ConnectionFactory cf = new ConnectionFactory();

        // Abrir conexión
        Connection connection = cf.newConnection();
        Channel channel = connection.createChannel(); // Establecer canal

        // Declara exchange "eventos"
        channel.exchangeDeclare(EVENTOS, BuiltinExchangeType.FANOUT);

        // Crear cola y asociarla al exchange "eventos"
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EVENTOS, "");

        // Crear suscripción a una cola asociada al exchange "eventos"
        channel.basicConsume(
                queueName,
                true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Mensaje '" + messageBody + "'");
                },
                consumerTag -> {
                    System.out.println("Consumidor '" + consumerTag + "' cancelado");
                }
        );

        // Cerrar conexiones abiertas
        //channel.close();
        //connection.close();
    }
}
