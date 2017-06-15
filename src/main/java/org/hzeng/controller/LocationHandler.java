package org.hzeng.controller;

import com.rabbitmq.client.*;
import org.hzeng.config.BusTrackerSettings;
import org.hzeng.model.BusTracker;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by hzeng on 6/10/17.
 */
@Component
public class LocationHandler extends TextWebSocketHandler {

    @Value("${bustracker.exchange-name}")
    String EXCHANGE_NAME; // = "BUS_LOCATION";

    @Value("${spring.rabbitmq.host}")
    String RABBITMQ_HOST;

    @Value("${bustracker.session-timeout ?: 300}")
    Long TIME_OUT;

    @Autowired
    BusTrackerSettings settings;

    Map<WebSocketSession, BusTracker> sessionMap;
    
    public LocationHandler(){
        sessionMap = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Connection established");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_HOST);

        try{
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String queueName = channel.queueDeclare().getQueue();
            BusTracker busTracker = new BusTracker(channel, queueName);
            for (BusTrackerSettings.RouteSetting r : settings.getRouteSettings()){
                if (r.isEnabled())
                    busTracker.addRouteName(r.getId());
            }
            Set<String> routeNames = busTracker.getRouteNames();

            for (String routingKey : routeNames) {
                channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
            }
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (Exception e) {
                        e.printStackTrace();
                        session.close(CloseStatus.SERVER_ERROR);
                        return;
                    }
                    System.out.println(" [-] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
                }
            };

            String consumerTag = channel.basicConsume(queueName, true, consumer);
            busTracker.setConsumerTag(consumerTag);

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(TIME_OUT * 1000);
                        closeSession(session);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            sessionMap.put(session, busTracker);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void closeSession(WebSocketSession session){
        // System.out.println("--> cleanup");
        if (!sessionMap.containsKey(session))
            return;
        BusTracker busTracker = sessionMap.get(session);
        sessionMap.remove(session);
        if (busTracker == null) {
            // sessionMap.remove(session);
            return;
        }
        Channel channel = busTracker.getChannel();
        try {
            channel.basicCancel(busTracker.getConsumerTag());
            Connection connection = channel.getConnection();
            channel.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (session.isOpen()) {
            try {
                // session.sendMessage(new TextMessage("session closed"));
                System.out.println("session closed");
                // TODO check why after session.close(), the program goes to the beginning of this function
                session.close();
                System.out.println("after close");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        // System.out.println("<-- cleanup");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        System.out.println("--> handleTextMessage");
        BusTracker busTracker = sessionMap.get(session);
        String queueName = busTracker.getQueueName();
        Channel channel = busTracker.getChannel();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(message.getPayload());
//        String a = (String) obj.get("checked");
        boolean checked = (boolean) obj.get("checked");
        String routeName = (String) obj.get("route");
        Set<String> routeNames = sessionMap.get(session).getRouteNames();
        if (checked) {
            routeNames.add(routeName);
            channel.queueBind(queueName, EXCHANGE_NAME, routeName);
        }
        else {
            routeNames.remove(routeName);
            channel.queueUnbind(queueName, EXCHANGE_NAME, routeName);
        }
        System.out.println("<-- handleTextMessage");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                         CloseStatus status){
        closeSession(session);
    }
}
