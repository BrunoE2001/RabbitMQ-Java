package org.rabbitmq.Eventos;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProductorEventos {
    private static final String EVENTOS_NAME = "eventos";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        // Abrir conexión AMQ y establecer canal
        try(
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
        ){
            // Crear fan out exchange "eventos"
            channel.exchangeDeclare(EVENTOS_NAME, BuiltinExchangeType.FANOUT);

            // Enviar mensajes al fan out exchange "eventos"
            int i = 0;
            while (true) {
                String message = "Evento " + i;
                channel.basicPublish(EVENTOS_NAME, "", null, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
                Thread.sleep(1000);
                i++;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
