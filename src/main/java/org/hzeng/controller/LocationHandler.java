package org.hzeng.controller;

import com.rabbitmq.client.*;
import org.hzeng.model.BusTracker;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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

    Map<WebSocketSession, BusTracker> sessionMap;

    // Set<String> routes;

    public LocationHandler(){
        sessionMap = new HashMap<>();
    }

//    public void sendGreeting(){
//        if (session != null && session.isOpen()){
//            try{
//                String message = new Date().toString();
//                System.out.println("Now sending : " + message);
//                JSONObject obj = new JSONObject();
//                obj.put("from", "server");
//                obj.put("topic", "topic");
//                String msg = "Hello: ";
//                for (String route : routes)
//                    msg += route + " ";
//                obj.put("message", msg);
//                obj.put("time", message);
//                session.sendMessage(new TextMessage(obj.toJSONString()));
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Connection established");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try{
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            BusTracker busTracker = new BusTracker(channel);
            Set<String> routeNames = busTracker.getRouteNames();

            String queueName = channel.queueDeclare().getQueue();

            for (String routingKey : routeNames) {
                channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
            }
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    //                BaltimoreCoordinateController.this.message = message;
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
                        Thread.sleep(10000);
                        cleanup(session);
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

    private void cleanup(WebSocketSession session){
        System.out.println("--> cleanup");
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
        System.out.println("<-- cleanup");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        System.out.println("--> handleTextMessage");
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(message.getPayload());
//        String a = (String) obj.get("checked");
        boolean checked = (boolean) obj.get("checked");
        String route = (String) obj.get("route");
        Set<String> routeNames = sessionMap.get(session).getRouteNames();
        if (checked)
            routeNames.add(route);
        else
            routeNames.remove(route);
        System.out.println("<-- handleTextMessage");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                         CloseStatus status){
        cleanup(session);
    }
}
