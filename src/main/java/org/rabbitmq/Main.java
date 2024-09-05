package org.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        String message = "Hello world!";

        // Abrir conexión AMQ y establecer canal
        ConnectionFactory connectionFactory = new ConnectionFactory();

        try (
                Connection connection = connectionFactory.newConnection(); // Inicializar conexión
                Channel channel = connection.createChannel(); // Establecer canal
        ) {
            // Crear Cola
            channel.queueDeclare("primera-cola", false, false, false, null);
            // Enviar mensaje al exchange""
            channel.basicPublish("", "primera-cola", null, message.getBytes());
        }
    }
}