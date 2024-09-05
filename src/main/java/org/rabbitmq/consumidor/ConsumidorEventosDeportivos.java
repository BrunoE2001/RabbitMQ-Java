package org.rabbitmq.consumidor;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ConsumidorEventosDeportivos {

    public static final String EXCHANGE = "eventos-deportivos";

    public static void main(String[] args) throws IOException, TimeoutException {

        // Abrir conexión AMQ
        ConnectionFactory cf = new ConnectionFactory();

        // Abrir conexión
        Connection connection = cf.newConnection();
        Channel channel = connection.createChannel(); // Establecer canal

        // Declara exchange "eventos-deportivos"
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);

        // Crear cola y asociarla al exchange "eventos-deportivos"
        String queueName = channel.queueDeclare().getQueue();
        // Patron routing-key -> country.sport.eventType
        // * -> identifica una palabra
        // # -> identifica multiples palabras delimitadas por .
        // eventos tenis -> *.tenis.*
        // eventos en españa -> es.# / es.*.*
        // todos los eventos -> #
        System.out.println("Introduzca routing-key: ");
        Scanner scanner = new Scanner(System.in);
        String routingKey = scanner.nextLine();
        scanner.close();
        channel.queueBind(queueName, EXCHANGE, routingKey);

        // Crear suscripción a una cola asociada al exchange "eventos-deportivos"
        channel.basicConsume(
                queueName,
                true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Mensaje '" + messageBody + "'");
                    System.out.println(" [x] Routing key '" + message.getEnvelope().getRoutingKey() + "'");
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
