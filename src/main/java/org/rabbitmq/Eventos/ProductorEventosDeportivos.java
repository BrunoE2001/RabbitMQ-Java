package org.rabbitmq.Eventos;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ProductorEventosDeportivos {
    private static final String EXCHANGE = "eventos-deportivos";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        // Abrir conexión AMQ y establecer canal
        try(
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
        ){
            // Crear topic exchange "eventos-deportivos"
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);

            // CLASIFICACIÓN
            List<String> countries = Arrays.asList("es", "fr", "usa", "mx");
            List<String> sports = Arrays.asList("futbol", "tenis", "voleibol", "basquetbol");
            List<String> eventTypes = Arrays.asList("enVivo", "noticia");

            // Enviar mensajes al topic exchange "eventos-deportivos"
            int i = 0;
            while (true) {
                shuffle(countries, sports, eventTypes);
                String country = countries.get(i % countries.size());
                String sport = sports.get(i % sports.size());
                String eventType = eventTypes.get(i % eventTypes.size());
                // routing-key -> country.sport.eventType
                String routingKey = country + "." + sport + "." + eventType;

                String message = "Evento " + i;
                System.out.println(" [x] Produciendo mensaje ('" + country + "," + sport + "," + eventType + "')");
                channel.basicPublish(EXCHANGE, routingKey, null, message.getBytes());
                Thread.sleep(1000);
                i++;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void shuffle(List<String> countries, List<String> sports, List<String> eventTypes) {
        Collections.shuffle(countries);
        Collections.shuffle(sports);
        Collections.shuffle(eventTypes);
    }
}
